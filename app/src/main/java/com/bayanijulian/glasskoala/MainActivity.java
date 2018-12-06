package com.bayanijulian.glasskoala;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private RecyclerView goalsList;
    private FloatingActionButton createGoalFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    private Goal[] mockGoalsData() {
        Goal goal1 = new Goal("12/05/18", "7:00 PM", "ARC", 90);
        Goal[] goals = new Goal[50];
        for (int i = 0; i < goals.length; i++) {
            goals[i] = goal1;
        }
        return goals;
    }
}
