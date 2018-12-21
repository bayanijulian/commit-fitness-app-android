package com.bayanijulian.glasskoala.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.Exclude;

public class Location implements Parcelable {
    private static final String TAG = Location.class.getSimpleName();
    private String placeId;
    private String name;
    private double longitude;
    private double latitude;

    public Location() {

    }

    public Location(Place place) {
        this.placeId = place.getId();
        this.name = String.valueOf(place.getName());

        LatLng coordinates = place.getLatLng();
        this.latitude = coordinates.latitude;
        this.longitude = coordinates.longitude;
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

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Exclude
    public android.location.Location getUserLocation() {
        android.location.Location location = new android.location.Location(this.name);
        location.setLatitude(this.latitude);
        location.setLongitude(this.longitude);
        return location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.placeId);
        dest.writeString(this.name);
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.latitude);
    }

    protected Location(Parcel in) {
        this.placeId = in.readString();
        this.name = in.readString();
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel source) {
            return new Location(source);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}
