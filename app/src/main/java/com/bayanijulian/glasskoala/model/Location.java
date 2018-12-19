package com.bayanijulian.glasskoala.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

public class Location implements Parcelable {
    private static final String TAG = Location.class.getSimpleName();
    private String placeId;
    private String name;

    public Location() {

    }

    public Location(Place place) {
        this.placeId = place.getId();
        this.name = String.valueOf(place.getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.placeId);
        dest.writeString(this.name);
    }

    protected Location(Parcel in) {
        this.placeId = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel source) {
            return new Location(source);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    @Exclude
    public void getPhoto(Context context, OnPhotoLoadListener listener) {
        getPhotos(this.placeId, context, listener);
    }
    public interface OnPhotoLoadListener {
        void onComplete(Bitmap bitmap);
    }
    // Request photos and metadata for the specified place.
    private void getPhotos(final String placeId, final Context context, final OnPhotoLoadListener listener) {
        Log.d(TAG, "Retrieving photos...");
        final GeoDataClient mGeoDataClient = Places.getGeoDataClient(context);
        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(placeId);

        photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                // Get the list of photos.
                PlacePhotoMetadataResponse photos = task.getResult();
                // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                // Get the first photo in the list.
                if (photoMetadataBuffer.getCount() == 0) {
                    Log.d(TAG, "No photos available in photo metadata.");
                    return;
                }
                Log.d(TAG, String.valueOf(photoMetadataBuffer.getCount()));
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
                        if(bitmap != null) {
                            listener.onComplete(bitmap);
                        } else {
                            Log.d(TAG, "Unable to get photo from bitmap.");
                        }

                    }
                });
            }
        });
    }


}
