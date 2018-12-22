package com.bayanijulian.glasskoala.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bayanijulian.glasskoala.R;
import com.bayanijulian.glasskoala.database.GoalIO;
import com.bayanijulian.glasskoala.model.Goal;
import com.bayanijulian.glasskoala.database.DatabaseIO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class GoalsFragment extends Fragment {
    private RecyclerView goalsList;

    public GoalsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_goals, container, false);
        goalsList = view.findViewById(R.id.fragment_goals_rv_goals);
        goalsList.setLayoutManager(new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.VERTICAL, false));


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateGoalsList();
    }

    private void updateGoalsList() {
        DatabaseIO.getGoalIO().read(new DatabaseIO.Listener<Goal>() {
            @Override
            public void onComplete(List<Goal> goals) {
                GoalsAdapter goalsAdapter = new GoalsAdapter(goals);
                goalsList.setAdapter(goalsAdapter);
            }
        });
    }
}
