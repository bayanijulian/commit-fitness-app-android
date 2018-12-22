package com.bayanijulian.glasskoala.database;


import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public abstract class DatabaseIO<T> {
    private static final String TAG = DatabaseIO.class.getSimpleName();

    protected FirebaseFirestore database;
    protected FirebaseUser currentUser;

    protected DatabaseIO() {
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
        this.database = FirebaseFirestore.getInstance();
        if (currentUser == null) {
            Log.d(TAG, "Current User is NULL");
        }
        if (database == null) {
            Log.d(TAG, "Database Instance is NULL");
        }
    }

    public interface Listener<T> {
        void onComplete(List<T> data);
    }

    public abstract void create(final T data);

    public abstract void read(final Listener<T> listener);

    public static GoalIO getGoalIO() {
        return new GoalIO();
    }

    public static UserIO getUserIO() {
        return new UserIO();
    }

}
