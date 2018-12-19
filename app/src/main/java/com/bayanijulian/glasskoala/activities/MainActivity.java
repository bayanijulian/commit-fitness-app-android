package com.bayanijulian.glasskoala.activities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.bayanijulian.glasskoala.R;
import com.bayanijulian.glasskoala.model.Goal;
import com.bayanijulian.glasskoala.util.DatabaseIO;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int RC_LOGIN = 43278;
    private static final int RC_CREATE_GOAL = 3245;
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

        createGoalFab = findViewById(R.id.activity_main_fab_create_goal);
        createGoalFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startCreateGoalActivity = new Intent(v.getContext(), CreateGoalActivity.class);
                startActivityForResult(startCreateGoalActivity, RC_CREATE_GOAL);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_LOGIN) {
            if (resultCode == RESULT_OK) {
                authenticate();
                Log.d(TAG, "Login Success, User ID is " + currentUser.getUid());
            } else {
                Log.d(TAG, "Login Failure");
            }
        } else if (requestCode == RC_CREATE_GOAL) {
            if (resultCode == RESULT_OK && data != null) {
                Log.d(TAG, "New goal created. Attempting to write to database.");
                Goal newGoal = data.getParcelableExtra(Goal.LABEL);
                DatabaseIO.addGoal(currentUser.getUid(), newGoal);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateGoalsList();
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

    private void updateGoalsList() {
        DatabaseIO.loadGoals(currentUser.getUid(), new DatabaseIO.Listener<Goal>() {
            @Override
            public void onComplete(List<Goal> goals) {
                GoalsAdapter goalsAdapter = new GoalsAdapter(goals);
                goalsList.setAdapter(goalsAdapter);
            }
        });
    }
}
