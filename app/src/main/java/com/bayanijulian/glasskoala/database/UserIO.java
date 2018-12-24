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
import java.util.Objects;

public class UserIO extends DatabaseIO {
    private static final String TAG = UserIO.class.getSimpleName();


    public static void create(final FirebaseFirestore database, final User user) {
        database.collection("users").document(user.getId()).set(user);
        Log.d(TAG, "Created user.");
    }

    public static void get(final FirebaseFirestore database,
                           final FirebaseUser user,
                           final SingleListener<User> listener) {
        Log.d(TAG, "Getting current user");
        database.collection("users")
                .document(user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() == null) {
                                Log.d(TAG, "Current user did not have data");
                            }
                            DocumentSnapshot document = task.getResult();

                            User data = document.toObject(User.class);
                            Objects.requireNonNull(data).setId(document.getId());
                            listener.onComplete(data);
                            Log.d(TAG, "Read in current user");
                        } else {
                            Log.d(TAG, "Error reading in current user.");
                        }
                    }
                });
    }

    public static void getAll(final FirebaseFirestore database, final ListListener<User> listListener) {
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
                        user.setId(document.getId());
                        users.add(user);
                    }
                    listListener.onComplete(users);
                    Log.d(TAG, "Read in users");
                } else {
                    Log.d(TAG, "Error reading in users.");
                }
            }
        });
    }

    public static void updateName(final User currentUser) {
        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        Log.d(TAG, "Updating Name");
        database.collection("users")
                .document(currentUser.getId())
                .update("name", currentUser.getName());
    }

    public static void updateProfileImg(final User currentUser, final Uri image) {
        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageReference = storage
                .getReference(currentUser.getId() + "/profileImages");
        final UploadTask uploadTask = storageReference.putFile(image);

        // first tasks uploads image to FirebaseStorage
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    Log.d(TAG, "Failed to upload image");
                    throw task.getException();
                }
                return storageReference.getDownloadUrl();
        // second task gets the download url to update FirebaseFirestore
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    String downloadUrl = task.getResult().toString();
                    Log.d(TAG, "Upload Image Task success with ref at " + downloadUrl);
                    database.collection("users")
                            .document(currentUser.getId())
                            .update("profileImg", downloadUrl);

                } else {
                    Log.d(TAG, "Failed to get download url for image");
                }
            }
        });
    }

    public static void updateFriendList(final User currentUser, final User newUser, boolean isFollowing) {
        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        Log.d(TAG, "Updating friend list");
        if (isFollowing) {
            currentUser.addFriend(newUser);
        } else {
            if(!currentUser.removeFriend(newUser)) {
                // if there was no one to remove then there's nothing to update
                return;
            }
        }
        database.collection("users")
                .document(currentUser.getId())
                .update("friendIds", currentUser.getFriendIds());
    }

}
