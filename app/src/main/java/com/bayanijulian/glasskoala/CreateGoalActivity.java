package com.bayanijulian.glasskoala;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class CreateGoalActivity extends AppCompatActivity {
    private static final String TAG = CreateGoalActivity.class.getSimpleName();
    private static final int RC_PLACE_PICKER = 213;

    private Button locationBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_goal);

        locationBtn = findViewById(R.id.activity_create_goal_btn_location);
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Attempting to select a location");
                selectLocation();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PLACE_PICKER) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                Log.d(TAG, "Successfully selected a location, Name: " + place.getName()
                + " , ID: " + place.getId());

                loadLocation(place);
            } else {
                Log.d(TAG, "Failed to select a location.");
            }
        }
    }

    private void selectLocation() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), RC_PLACE_PICKER);
        } catch (GooglePlayServicesRepairableException e) {
            Log.d(TAG, "Error launching Place Picker Intent");
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.d(TAG, "Error launching Place Picker Intent");
            e.printStackTrace();
        }
    }

    private void loadLocation(Place place) {
        locationBtn.setText(place.getName());
    }
}
