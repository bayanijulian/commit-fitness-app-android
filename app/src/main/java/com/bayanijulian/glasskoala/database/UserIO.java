package com.bayanijulian.glasskoala.database;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bayanijulian.glasskoala.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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

    public void updateName(final String name) {
        database.collection("users")
                .document(currentUser.getUid())
                .update("name", name);
    }

    public void updateProfileImg(final Uri image) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageReference = storage
                .getReference(currentUser.getUid() + "/profileImages");
        final UploadTask uploadTask = storageReference.putFile(image);

        // first tasks uploads image to FirebaseStorage
        // second task gets the download url to update FirebaseFirestore
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    Log.d(TAG, "Failed to upload image");
                    throw task.getException();
                }
                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    String downloadUrl = task.getResult().toString();
                    Log.d(TAG, "Upload Image Task success with ref at " + downloadUrl);
                    database.collection("users")
                            .document(currentUser.getUid())
                            .update("profileImg", downloadUrl);

                } else {
                    Log.d(TAG, "Failed to get download url for image");
                }
            }
        });
    }



    public void getCurrentUser(final Listener<User> listener) {
        Log.d(TAG, "Getting current user");
        database.collection("users")
                .document(currentUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() == null) {
                                Log.d(TAG, "Current user did not have data");
                            }
                            // users list which should only contain one element
                            List<User> users = new ArrayList<>();
                            DocumentSnapshot document = task.getResult();

                            User user = document.toObject(User.class);
                            users.add(user);
                            listener.onComplete(users);
                            Log.d(TAG, "Read in current user");
                        } else {
                            Log.d(TAG, "Error reading in current user.");
                        }
                    }
                });
    }

}
