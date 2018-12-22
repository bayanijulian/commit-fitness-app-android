package com.bayanijulian.glasskoala.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bayanijulian.glasskoala.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class UserIO extends DatabaseIO<User>{
    private static final String TAG = UserIO.class.getSimpleName();

    @Override
    public void create(User data) {
        database.collection("users").add(data);
        Log.d(TAG, "Created user.");
    }

    @Override
    public void read(Listener<User> listener) {
        database.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Success read in users");
                    if (task.getResult() == null) {
                        Log.d(TAG, "No results for reading in users");
                        return;
                    }
                    for (DocumentSnapshot document : task.getResult()) {

                    }
                } else {
                    Log.d(TAG, "Error reading in users.");
                }
            }
        });
    }
}
