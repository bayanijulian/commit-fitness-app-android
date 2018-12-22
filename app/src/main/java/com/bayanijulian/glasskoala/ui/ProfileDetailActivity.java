package com.bayanijulian.glasskoala.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bayanijulian.glasskoala.R;
import com.bayanijulian.glasskoala.database.DatabaseIO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

public class ProfileDetailActivity extends AppCompatActivity {
    private static final String TAG = ProfileDetailActivity.class.getSimpleName();
    private static final int RC_PICK_IMAGE = 431;

    private Button profileImgBtn;
    private TextInputEditText nameInput;
    private Button saveBtn;
    private FirebaseUser currentUser;
    private ImageView profileImg;
    private Uri newImageData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        nameInput = findViewById(R.id.activity_profile_detail_input_name);
        profileImg = findViewById(R.id.activity_profile_detail_img_profile);

        profileImgBtn = findViewById(R.id.activity_profile_detail_btn_image);
        profileImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        saveBtn = findViewById(R.id.activity_profile_detail_btn_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });
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
            String newName = nameInput.getText().toString();
            if (!newName.isEmpty()) {
                DatabaseIO.getUserIO().updateName(newName);
            }
        }
        if (newImageData != null) {
            DatabaseIO.getUserIO().updateProfileImg(newImageData);
        }
        // kills the activity and should go back to the last activity
        finish();
    }

    private void loadImage(Uri imageData) {
        Picasso.get().load(imageData).into(profileImg);
    }
}
