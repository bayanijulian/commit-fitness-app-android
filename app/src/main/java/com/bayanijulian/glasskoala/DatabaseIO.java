package com.bayanijulian.glasskoala;

import com.bayanijulian.glasskoala.model.Goal;

import java.util.List;

public class DatabaseIO {
    //public static FirebaseDatebase
    public interface OnGoalsLoadListener {
        void onLoad(List<Goal> goals);
    }

    public static void loadGoals(String userId, OnGoalsLoadListener listener) {
        String path = "goals/" + userId + "/goals";

    }

    public static void addGoal(String userId, Goal goal) {
        String path = "goals/" + userId + "/goals";
    }
}
