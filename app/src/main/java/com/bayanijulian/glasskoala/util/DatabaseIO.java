package com.bayanijulian.glasskoala.util;


import android.support.annotation.NonNull;
import android.util.Log;

import com.bayanijulian.glasskoala.model.Goal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DatabaseIO {
    private static final String TAG = DatabaseIO.class.getSimpleName();
    private static FirebaseFirestore database = FirebaseFirestore.getInstance();
    public interface Listener<T> {
        void onComplete(List<T> data);
    }

    public static void loadGoals(String userId, final Listener<Goal> listener) {
        Log.d(TAG, "Loading goals...");
        database.collection("goals")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    List<Goal> goals = new ArrayList<>();
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        Goal goal = document.toObject(Goal.class);
                        goals.add(goal);
                    }
                    Log.d(TAG, "Goals loaded.");
                    listener.onComplete(goals);
                } else {
                    Log.d(TAG, "Error loading goals...\n" + task.getException());
                }
            }
        });
    }

    public static void addGoal(Goal goal) {
        database.collection("goals").add(goal);
        Log.d(TAG, "Goal saved in database.");
    }
}
