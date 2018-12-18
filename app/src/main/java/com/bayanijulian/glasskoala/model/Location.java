package com.bayanijulian.glasskoala.model;

import com.google.android.gms.location.places.Place;

public class Location {
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
}
