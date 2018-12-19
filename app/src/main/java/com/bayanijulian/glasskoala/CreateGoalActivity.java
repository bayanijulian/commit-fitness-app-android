package com.bayanijulian.glasskoala;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.bayanijulian.glasskoala.model.Goal;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Calendar;

public class CreateGoalActivity extends AppCompatActivity {
    private static final String TAG = CreateGoalActivity.class.getSimpleName();
    private static final int RC_PLACE_PICKER = 213;


    private Button locationBtn;
    private Button dateBtn;
    private Button startTimeBtn;
    private Button endTimeBtn;
    private Button saveBtn;
    private ImageView imgView;
    private Goal goal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_goal);

        locationBtn = findViewById(R.id.activity_create_goal_btn_location);
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectLocation();
            }
        });

        dateBtn = findViewById(R.id.activity_create_goal_btn_date);
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate();
            }
        });

        startTimeBtn = findViewById(R.id.activity_create_goal_btn_start_time);
        startTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTime(v);
            }
        });

        endTimeBtn = findViewById(R.id.activity_create_goal_btn_end_time);
        endTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTime(v);
            }
        });

        saveBtn = findViewById(R.id.activity_create_goal_btn_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        imgView = findViewById(R.id.imageView);

        goal = new Goal();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PLACE_PICKER) {
            if (resultCode == RESULT_OK && data != null) {

                Place place = PlacePicker.getPlace(this, data);
                Log.d(TAG, "Successfully selected a location, Name: " + place.getName()
                + " , ID: " + place.getId());
                loadLocation(place);
            } else {
                Log.d(TAG, "Failed to select a location.");
            }
        }
    }

    private void save() {
        Log.d(TAG, "Sending Goal to Main Activity");
        Intent goalData = new Intent();
        goalData.putExtra(Goal.LABEL, goal);
        setResult(RESULT_OK, goalData);
        finish();
    }

    private void selectLocation() {
        Log.d(TAG, "Attempting to select a location");
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

    private void selectDate() {
        Log.d(TAG, "Attempting to select a date");
        DialogFragment datePickerFragment = new DatePickerFragment();
        ((DatePickerFragment) datePickerFragment).setListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                loadDate(year, month, dayOfMonth);
            }
        });
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void selectTime(final View v) {
        Log.d(TAG, "Attempting to select a time");
        DialogFragment timePickerFragment = new TimePickerFragment();
        ((TimePickerFragment) timePickerFragment).setListener(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                loadTime(v, hourOfDay, minute);
            }
        });
        timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    private void loadLocation(Place place) {
        Log.d(TAG, "Location Loaded");
        goal.setLocationPlace(place);
        locationBtn.setText(goal.getLocation().getName());
    }

    private void loadDate(int year, int month, int day) {
        Log.d(TAG, "Date Loaded");
        goal.setDate(year, month, day);
        dateBtn.setText(goal.getDate());
    }

    private void loadTime(View v, int hour, int minutes) {
        Log.d(TAG, "Time Loaded");
        if (v.getId() == R.id.activity_create_goal_btn_start_time) {
            goal.setStartTime(hour, minutes);
            startTimeBtn.setText(goal.getStartTime());
        } else if (v.getId() == R.id.activity_create_goal_btn_end_time) {
            goal.setEndTime(hour, minutes);
            endTimeBtn.setText(goal.getEndTime());
        }
    }

    public static class DatePickerFragment extends DialogFragment {
        private DatePickerDialog.OnDateSetListener listener;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), listener, year, month, day);
        }

        public void setListener(DatePickerDialog.OnDateSetListener listener) {
            this.listener = listener;
        }
    }

    public static class TimePickerFragment extends DialogFragment {
        private TimePickerDialog.OnTimeSetListener listener;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), listener, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void setListener(TimePickerDialog.OnTimeSetListener listener) {
            this.listener = listener;
        }
    }

    // Request photos and metadata for the specified place.
    private void getPhotos(final String placeId) {
        final GeoDataClient mGeoDataClient = Places.getGeoDataClient(this);
        //final String placeId = "ChIJa147K9HX3IAR-lwiGIQv9i4";
        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(placeId);
        photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                // Get the list of photos.
                PlacePhotoMetadataResponse photos = task.getResult();
                // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                // Get the first photo in the list.
                PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(0);
                // Get the attribution text.
                CharSequence attribution = photoMetadata.getAttributions();
                // Get a full-size bitmap for the photo.
                Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);
                photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                        PlacePhotoResponse photo = task.getResult();
                        Bitmap bitmap = photo.getBitmap();
                        imgView.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }
}
