package com.bayanijulian.glasskoala;


import android.util.Log;
import com.bayanijulian.glasskoala.model.Goal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class DatabaseIO {
    private static final String TAG = DatabaseIO.class.getSimpleName();
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();

    public interface OnGoalsLoadListener {
        void onLoad(List<Goal> goals);
    }

    public static void loadGoals(String userId, final OnGoalsLoadListener listener) {
        String path = "goals/" + userId + "/goals";
        DatabaseReference reference = database.getReference(path);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Goal> goals = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Goal goal = data.getValue(Goal.class);
                    goal.setId(data.getKey());
                    goals.add(goal);
                }
                listener.onLoad(goals);
                Log.d(TAG, "Goals loaded into client");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled:" + databaseError.getMessage());
            }
        });

    }

    public static void addGoal(String userId, Goal goal) {
        String path = "goals/" + userId + "/goals";
        DatabaseReference reference = database.getReference(path).push();
        reference.setValue(goal);
        Log.d(TAG, "Goal saved in database.");
    }
}
