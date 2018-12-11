package com.bayanijulian.glasskoala;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.Calendar;

public class CreateGoalActivity extends AppCompatActivity {
    private static final String TAG = CreateGoalActivity.class.getSimpleName();
    private static final int RC_PLACE_PICKER = 213;

    private Button locationBtn;
    private Button dateBtn;
    private Button timeBtn;
    private Spinner durationSpinner;

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

        timeBtn = findViewById(R.id.activity_create_goal_btn_time);
        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTime();
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
                loadDate(view);
            }
        });
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void selectTime() {
        Log.d(TAG, "Attempting to select a time");
        DialogFragment timePickerFragment = new TimePickerFragment();
        ((TimePickerFragment) timePickerFragment).setListener(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                loadTime(view);
            }
        });
        timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    private void loadLocation(Place place) {
        Log.d(TAG, "Location Loaded");
        locationBtn.setText(place.getName());
    }

    private void loadDate(DatePicker view) {
        Log.d(TAG, "Date Loaded");
        dateBtn.setText(view.getMonth() + "/" + view.getDayOfMonth() + "/" + view.getYear());
    }

    private void loadTime(TimePicker view) {
        Log.d(TAG, "Time Loaded");
        timeBtn.setText(view.getHour() + ":" + view.getMinute());
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
}
