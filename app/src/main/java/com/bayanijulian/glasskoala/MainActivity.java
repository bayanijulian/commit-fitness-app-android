package com.bayanijulian.glasskoala;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private RecyclerView goalsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goalsList = findViewById(R.id.activity_main_rv_goals);
        goalsList.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

        GoalsAdapater goalsAdapater = new GoalsAdapater(mockGoalsData());
        goalsList.setAdapter(goalsAdapater);
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
