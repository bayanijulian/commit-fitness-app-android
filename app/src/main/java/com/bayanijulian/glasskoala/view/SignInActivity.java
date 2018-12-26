package com.bayanijulian.glasskoala.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bayanijulian.glasskoala.R;
import com.bayanijulian.glasskoala.SignUpActivity;
import com.bayanijulian.glasskoala.database.DatabaseIO;
import com.bayanijulian.glasskoala.database.UserIO;
import com.bayanijulian.glasskoala.model.User;
import com.bayanijulian.glasskoala.view.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = SignInActivity.class.getSimpleName();

    private Button loginBtn;
    private Button signUpBtn;
    private EditText emailInput;
    private EditText passwordInput;
    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        firebaseAuth = FirebaseAuth.getInstance();
        verifyCurrentUser();

        emailInput = findViewById(R.id.activity_sign_et_email);
        passwordInput = findViewById(R.id.activity_sign_in_et_password);

        loginBtn = findViewById(R.id.activity_sign_in_btn_login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticate();
            }
        });

        signUpBtn = findViewById(R.id.activity_sign_in_btn_sign_up);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignUpActivity();
            }
        });
    }

    private void startSignUpActivity() {
        Intent signUpActivity = new Intent(this, SignUpActivity.class);
        startActivity(signUpActivity);
    }

    /**
     * Sends the current text in email and password inputs
     * to check with FirebaseAuth
     */
    private void authenticate() {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Login success");
                    verifyCurrentUser();
                } else {
                    updateUIFailedLogin();
                }
            }
        });
    }

    /**
     * If there is a user currently signed in, skip the sign in activity
     * and goes straight to the MainActivity
     */
    private void verifyCurrentUser() {
        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            loadUser();
        }
    }

    private void updateUIFailedLogin() {
        Log.d(TAG, "Login Failed");
        //TODO: implement error checking and forgot password link
    }

    /**
     * Updates the user, just for testing, should be moved to SignUp Activity
     */
    private void updateUser() {
        currentUser = firebaseAuth.getCurrentUser();
        String id = Objects.requireNonNull(this.currentUser).getUid();
        String name = this.currentUser.getDisplayName();
        String phoneNumber = this.currentUser.getPhoneNumber();
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setPhoneNumber(phoneNumber);
        UserIO.create(FirebaseFirestore.getInstance(), user);
    }

    /**
     * Loads the user to our model, to send to the main activity as a parcelable
     */


    private void loadUser() {

        updateUser();
        UserIO.get(FirebaseFirestore.getInstance(), currentUser, new DatabaseIO.SingleListener<User>() {
            @Override
            public void onComplete(User data) {
                startMainActivity(data);
            }
        });
    }

    private void startMainActivity(User user) {
        // load current user data first and then go to main activity
        Intent mainActivity = new Intent(this, MainActivity.class);
        mainActivity.putExtra(User.LABEL, user);
        startActivity(mainActivity);
    }
}
