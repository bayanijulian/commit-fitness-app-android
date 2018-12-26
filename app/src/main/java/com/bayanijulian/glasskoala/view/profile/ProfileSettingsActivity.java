package com.bayanijulian.glasskoala.view.profile;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import com.bayanijulian.glasskoala.R;
import com.bayanijulian.glasskoala.database.UserIO;
import com.bayanijulian.glasskoala.model.User;

import com.bayanijulian.glasskoala.view.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class ProfileSettingsActivity extends AppCompatActivity {
    public static final String TAG = ProfileSettingsActivity.class.getSimpleName();
    private static final int RC_PICK_IMAGE = 431;

    private Button profileImgBtn;
    private TextInputEditText nameInput;
    private Button saveBtn;
    private User currentUser;
    private ImageView profileImg;
    private Uri newImageData;
    private Button logoutBtn;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        // gets the currentUser from the calling activity
        Intent fromActivity = getIntent();
        currentUser = fromActivity.getParcelableExtra(User.LABEL);

        nameInput = findViewById(R.id.activity_profile_settings_input_name);
        nameInput.setText(currentUser.getName());

        profileImg = findViewById(R.id.activity_profile_settings_img_profile);
        Picasso.get().load(currentUser.getProfileImg()).noPlaceholder().into(profileImg);

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        profileImgBtn = findViewById(R.id.activity_profile_settings_btn_image);
        profileImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        saveBtn = findViewById(R.id.activity_profile_settings_btn_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Save button pressed.");
                updateUser();
            }
        });

        logoutBtn = findViewById(R.id.activity_profile_settings_btn_logout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Logging out currentUser.");
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(v.getContext(), SignInActivity.class);
                startActivity(intent);
            }
        });

        toolbar = findViewById(R.id.activity_profile_settings_toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data == null || data.getData() == null) {
                    Log.d(TAG, "Image chosen returned null");
                    return;
                }
                Log.d(TAG, "Success choosing image");
                newImageData = data.getData();
                loadImage(newImageData);
            } else {
                Log.d(TAG, "Error selecting image");
            }
        }
    }

    private void selectImage() {
        Intent getImage = new Intent();
        // all types of images accepted
        getImage.setType("image/*");
        getImage.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(getImage, RC_PICK_IMAGE);
    }

    private void updateUser() {
        if (nameInput.getText() != null) {
            Log.d(TAG, "Checking name...");
            String newName = nameInput.getText().toString();
            if (!newName.isEmpty()) {
                Log.d(TAG, "Updating name...");
                UserIO.updateName(currentUser);
            }
        }
        if (newImageData != null) {
            UserIO.updateProfileImg(currentUser, newImageData);
        }
        // go back to main activity
        finish();
    }

    private void loadImage(Uri imageData) {
        Picasso.get().load(imageData).noPlaceholder().into(profileImg);
    }
}
