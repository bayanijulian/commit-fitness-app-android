package com.bayanijulian.glasskoala.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bayanijulian.glasskoala.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserIO extends DatabaseIO<User>{
    private static final String TAG = UserIO.class.getSimpleName();

    @Override
    public void create(User user) {
        database.collection("users").document(user.getId()).set(user);
        Log.d(TAG, "Created user.");
    }

    @Override
    public void read(final Listener<User> listener) {
        database.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Success read in users");
                    if (task.getResult() == null) {
                        Log.d(TAG, "No results for reading in users");
                        return;
                    }
                    List<User> users = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        User user = document.toObject(User.class);
                        users.add(user);
                    }
                    listener.onComplete(users);
                    Log.d(TAG, "Read in users");
                } else {
                    Log.d(TAG, "Error reading in users.");
                }
            }
        });
    }
}
