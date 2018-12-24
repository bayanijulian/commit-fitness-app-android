package com.bayanijulian.glasskoala.view.goal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.bayanijulian.glasskoala.R;
import com.bayanijulian.glasskoala.database.DatabaseIO;
import com.bayanijulian.glasskoala.database.GoalIO;
import com.bayanijulian.glasskoala.model.Goal;
import com.bayanijulian.glasskoala.model.User;
import com.bayanijulian.glasskoala.view.goal.GoalsAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class GoalsActivity extends AppCompatActivity {

    private RecyclerView goalsList;
    private FirebaseFirestore database;
    private Toolbar toolbar;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        Intent getUser = getIntent();
        user = getUser.getParcelableExtra(User.LABEL);

        goalsList = findViewById(R.id.activity_goals_list);
        goalsList.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

        database = FirebaseFirestore.getInstance();

        toolbar = findViewById(R.id.activity_goals_toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateGoalsList();
    }

    private void updateGoalsList() {
        GoalIO.getAll(database, user, new DatabaseIO.ListListener<Goal>() {
            @Override
            public void onComplete(List<Goal> goals) {
                GoalsAdapter goalsAdapter = new GoalsAdapter(goals);
                goalsList.setAdapter(goalsAdapter);
            }
        });
    }
}
