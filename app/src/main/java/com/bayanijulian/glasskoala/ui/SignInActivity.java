package com.bayanijulian.glasskoala.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bayanijulian.glasskoala.R;
import com.bayanijulian.glasskoala.database.DatabaseIO;
import com.bayanijulian.glasskoala.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = SignInActivity.class.getSimpleName();

    private Button loginBtn;
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
                    updateUser();
                    startMainActivity();
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
            startMainActivity();
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
        String id = this.currentUser.getUid();
        String name = this.currentUser.getDisplayName();
        String phoneNumber = this.currentUser.getPhoneNumber();
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setPhoneNumber(phoneNumber);
        DatabaseIO.getUserIO().create(user);
    }

    private void startMainActivity() {
        // load current user data first and then go to main activity

        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
    }
}
