package com.bayanijulian.glasskoala.view.goal;

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
import com.bayanijulian.glasskoala.model.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;


public class GoalsFragment extends Fragment {
    private  RecyclerView goalsList;
    private FirebaseFirestore database;
    private User user;

    public GoalsFragment() {
        // Required empty public constructor
    }

    public static GoalsFragment getInstance(User user) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(User.LABEL, user);

        GoalsFragment goalsFragment = new GoalsFragment();
        goalsFragment.setArguments(bundle);

        return goalsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_goals, container, false);

        goalsList = view.findViewById(R.id.fragment_goals_rv_goals);
        goalsList.setLayoutManager(new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.VERTICAL, false));

        user = Objects.requireNonNull(getArguments()).getParcelable(User.LABEL);

        database = FirebaseFirestore.getInstance();


        return view;
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
