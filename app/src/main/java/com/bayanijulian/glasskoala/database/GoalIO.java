package com.bayanijulian.glasskoala.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bayanijulian.glasskoala.model.Goal;
import com.bayanijulian.glasskoala.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class GoalIO extends DatabaseIO{
    private static final String TAG = GoalIO.class.getSimpleName();


    public static void create(final FirebaseFirestore database, final Goal data) {
        database.collection("goals").add(data);
        Log.d(TAG, "Created goal.");
    }


    public static void getAll(final FirebaseFirestore database,
                       final User user,
                       final ListListener<Goal> listener) {
        Log.d(TAG, "Loading goals...");
        String userId = user.getId();
        database.collection("goals")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Loading goals successful.");
                            if(task.getResult() == null) {
                                Log.d(TAG, "No results for goals");
                                return;
                            }
                            // add all results to list
                            List<Goal> goals = new ArrayList<>();
                            for (QueryDocumentSnapshot document: task.getResult()) {
                                Goal goal = document.toObject(Goal.class);
                                goals.add(goal);
                            }
                            // notify listener
                            Log.d(TAG, "Goals loaded.");
                            listener.onComplete(goals);
                        } else {
                            Log.d(TAG, "Error loading goals...\n" + task.getException());
                        }
                    }
                });
    }

}
