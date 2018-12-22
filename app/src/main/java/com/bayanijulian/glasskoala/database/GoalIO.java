package com.bayanijulian.glasskoala.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bayanijulian.glasskoala.model.Goal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class GoalIO extends DatabaseIO<Goal>{
    private static final String TAG = GoalIO.class.getSimpleName();

    @Override
    public void create(final Goal data) {
        database.collection("goals").add(data);
        Log.d(TAG, "Created goal.");
    }

    @Override
    public void read(final Listener<Goal> listener) {
        Log.d(TAG, "Loading goals...");
        String userId = currentUser.getUid();
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

}
