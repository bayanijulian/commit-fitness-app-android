package com.bayanijulian.glasskoala;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.bayanijulian.glasskoala.model.User;
import com.bayanijulian.glasskoala.view.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SignUpActivity extends AppCompatActivity implements GetNameFragment.Listener,
                                                                 GetPhoneFragment.Listener,
                                                                 VerifyPhoneFragment.Listener {
    public static final String PAGER_NUMBER_KEY = "PAGER NUMBER KEY";
    private static final String TAG = SignUpActivity.class.getSimpleName();
    private User newUser;
    private ViewPager viewPager;
    private Toolbar toolbar;

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private boolean verifyInProgress = false;
    private String verificationCode;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verifyListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        viewPager = findViewById(R.id.activity_sign_up_view_pager);
        SignUpPagerAdapter signUpPagerAdapter = new SignUpPagerAdapter(getSupportFragmentManager());
        signUpPagerAdapter.setGetNameFragment(GetNameFragment.getInstance());
        signUpPagerAdapter.setGetPhoneFragment(GetPhoneFragment.getInstance());
        signUpPagerAdapter.setVerifyPhoneFragment(VerifyPhoneFragment.getInstance());
        viewPager.setAdapter(signUpPagerAdapter);

        newUser = new User();

        toolbar = findViewById(R.id.activity_sign_up_toolbar);
        setSupportActionBar(toolbar);

        verifyListener = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
                verifyInProgress = false;
                // [END_EXCLUDE]

                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential
                //updateUI(STATE_VERIFY_SUCCESS, credential);
                // [END_EXCLUDE]
                signInWithPhoneAuthCredential(credential);
                startMainActivity(newUser);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                // [START_EXCLUDE silent]
                verifyInProgress = false;
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    //mPhoneNumberField.setError("Invalid phone number.");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }

                // Show a message and update the UI
                // [START_EXCLUDE]
                //updateUI(STATE_VERIFY_FAILED);
                // [END_EXCLUDE]
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                verificationCode = verificationId;
                resendToken = token;

                // [START_EXCLUDE]
                // Update UI
                //updateUI(STATE_CODE_SENT);
                // [END_EXCLUDE]
            }
        };
    }

    private void startMainActivity(User user) {
        // load current user data first and then go to main activity
        Intent mainActivity = new Intent(this, MainActivity.class);
        mainActivity.putExtra(User.LABEL, user);
        startActivity(mainActivity);
    }

    @Override
    public void onNext(Bundle bundle) {
        int pageNumber = bundle.getInt(PAGER_NUMBER_KEY);
        Log.d(TAG, "Next pressed from " + pageNumber);
        switch (pageNumber) {
            case GetNameFragment.PAGER_NUMBER:
                Log.d(TAG, "Going to Get Phone");
                updateName(bundle);
                viewPager.setCurrentItem(GetPhoneFragment.PAGER_NUMBER);
                break;
            case GetPhoneFragment.PAGER_NUMBER:
                Log.d(TAG, "Going to Verify Phone");
                updatePhone(bundle);
                viewPager.setCurrentItem(VerifyPhoneFragment.PAGER_NUMBER);
                break;
            case VerifyPhoneFragment.PAGER_NUMBER:
                Log.d(TAG, "Verifying Phone");
                verifyPhone(bundle);
                break;
        }
    }

    private void updateName(Bundle bundle) {
        String name = bundle.getString(GetNameFragment.NAME_KEY);
        newUser.setName(name);
    }

    private void updatePhone(Bundle bundle) {
        String phone = bundle.getString(GetPhoneFragment.PHONE_KEY);
        newUser.setPhoneNumber(phone);
        startPhoneNumberVerification(phone);
    }

    private void verifyPhone(Bundle bundle) {
        String code = bundle.getString(VerifyPhoneFragment.CODE_KEY);
        verifyPhoneNumberWithCode(verificationCode, code);
    }

    public class SignUpPagerAdapter extends FragmentPagerAdapter {
        private GetNameFragment getNameFragment;
        private GetPhoneFragment getPhoneFragment;
        private VerifyPhoneFragment verifyPhoneFragment;



        public SignUpPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int currentPage) {
            switch (currentPage) {
                case GetNameFragment.PAGER_NUMBER:
                    return this.getNameFragment;
                case GetPhoneFragment.PAGER_NUMBER:
                    return this.getPhoneFragment;
                case VerifyPhoneFragment.PAGER_NUMBER:
                    return this.verifyPhoneFragment;
                default:
                    throw new IllegalStateException();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        public void setGetNameFragment(GetNameFragment getNameFragment) {
            this.getNameFragment = getNameFragment;
        }

        public void setGetPhoneFragment(GetPhoneFragment getPhoneFragment) {
            this.getPhoneFragment = getPhoneFragment;
        }

        public void setVerifyPhoneFragment(VerifyPhoneFragment verifyPhoneFragment) {
            this.verifyPhoneFragment = verifyPhoneFragment;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, verifyInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        verifyInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                verifyListener);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        verifyInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,            // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,       // Unit of timeout
                this,               // Activity (for callback binding)
                verifyListener,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // [START_EXCLUDE]
                            //updateUI(STATE_SIGNIN_SUCCESS, user);
                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                //mVerificationField.setError("Invalid code.");
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                            //updateUI(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }
    // [END sign_in_with_phone]
}
