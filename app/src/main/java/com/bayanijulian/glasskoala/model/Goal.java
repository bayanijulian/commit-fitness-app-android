package com.bayanijulian.glasskoala.model;

import com.google.android.gms.location.places.Place;

/**
 * Represents the goals that a user has set for themselves.
 * Goals are when they chose to go to the gym at a certain time
 * and the amount of time they plan to stay there
 */
public class Goal {
    /**
     * Unique Identifier used in Firebase Realtime Database
     */
    private String id;
    private Location location;
    private Time time;

    public Goal() {
        id = null;
        time = new Time();
        location = new Location();
    }

    public String getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public Time getTime() {
        return time;
    }

    public void setStartTime(int startHour, int startMinutes) {
        time.setStartTime(startHour, startMinutes);
    }

    public void setEndTime(int endHour, int endMinutes) {
        time.setEndTime(endHour, endMinutes);
    }

    public String getStartTime() {
        return time.getStartTime();
    }

    public String getEndTime() {
        return time.getEndTime();
    }

    public void setDate(int year, int hour, int day) {
        time.setDate(year, hour, day);
    }

    public String getDate() {
        return time.getDate();
    }

    public void setLocation(Place place) {
        this.location = new Location(place);
    }

    public String getDuration() {
        return this.time.getDuration();
    }
}
