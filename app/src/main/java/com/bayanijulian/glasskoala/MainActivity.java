package com.bayanijulian.glasskoala;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int RC_LOGIN = 123;

    private RecyclerView goalsList;
    private FloatingActionButton createGoalFab;

    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authenticate();

        goalsList = findViewById(R.id.activity_main_rv_goals);
        goalsList.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

        GoalsAdapter goalsAdapter = new GoalsAdapter(mockGoalsData());
        goalsList.setAdapter(goalsAdapter);

        createGoalFab = findViewById(R.id.activity_main_fab_create_goal);
        createGoalFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newCreateGoalActivity = new Intent(v.getContext(), CreateGoalActivity.class);
                startActivity(newCreateGoalActivity);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_LOGIN) {
            if (resultCode == RESULT_OK) {
                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                Log.d(TAG, "Login Success, User ID is " + currentUser.getUid());
            } else {
                Log.d(TAG, "Login Failure");
            }
        }
    }

    private void login() {
        // choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build());

        // create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_LOGIN);
    }

    private void authenticate() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Log.d(TAG, "No current user. Going to login activity.");
            login();
        }
    }


    private Goal[] mockGoalsData() {
        Goal goal1 = new Goal("12/05/18", "7:00 PM", "ARC", 90);
        Goal[] goals = new Goal[50];

        for (int i = 0; i < goals.length; i++) {
            goals[i] = goal1;
        }

        return goals;
    }
}
