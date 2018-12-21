package com.bayanijulian.glasskoala.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

public class LocationValidator {
    private static final String TAG = LocationValidator.class.getSimpleName();
    private static final float MAX_METERS = 400;

    public interface Listener {
        void onComplete(boolean isNear);
    }

    public static void updateUserLocation(final Context context, final Location destination, final Listener listener) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Location Permissions need to be enabled.");
                listener.onComplete(false);
        } else {
            // Permission has already been granted
            Log.d(TAG, "Location Permissions are enabled.");
            FusedLocationProviderClient fusedLocationProviderClient =
                    new FusedLocationProviderClient(context);
            LocationCallback locationCallback = new LocationCallback(){
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    Log.d(TAG, "Checking locations...");
                    boolean isNear = isNear(locationResult, destination);
                    listener.onComplete(isNear);
                }
            };

            Log.d(TAG, "Attempting to get location.");
            fusedLocationProviderClient.requestLocationUpdates(LocationRequest.create(),
                    locationCallback, null);
        }
    }

    private static boolean isNear(final LocationResult locationResult,
                                     final Location destination) {
        for (Location location : locationResult.getLocations()) {
            float metersAway = location.distanceTo(destination);
            Log.d(TAG, "Distance to location is " + metersAway + " meters");
            if (metersAway < MAX_METERS) {
                return true;
            }
        }
        return false;
    }
}
