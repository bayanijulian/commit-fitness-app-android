package com.bayanijulian.glasskoala;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    private Button loginBtn;
    private EditText phoneTxtInput;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = findViewById(R.id.activity_login_btn);
        phoneTxtInput = findViewById(R.id.activity_login_et_phone);
        auth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               verify();
            }
        });

    }

    private void verify() {
        String phoneNumber = phoneTxtInput.getText().toString();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        login(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {

                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, token.toString());
                        login(credential);
                    }
                });        // OnVerificationStateChangedCallbacks
    }

    private void login(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                startMainActivity();
            }
        });
    }

    private void startMainActivity() {
        Intent startMainActivity = new Intent(this, MainActivity.class);
        startActivity(startMainActivity);
    }


}
