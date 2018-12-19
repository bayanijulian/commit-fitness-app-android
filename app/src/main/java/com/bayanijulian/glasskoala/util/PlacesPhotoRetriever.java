package com.bayanijulian.glasskoala.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class PlacesPhotoRetriever {
    private static final String TAG = PlacesPhotoRetriever.class.getSimpleName();

    public interface Listener {
        void onComplete(Bitmap bitmap);
    }

    public static void getPhotoById(final Context context, final String placeId,
                                    final Listener listener) {
        getPhotos(context, placeId, listener);
    }

    // Request photos and metadata for the specified place.
    private static void getPhotos(final Context context, final String placeId, final Listener listener) {
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
