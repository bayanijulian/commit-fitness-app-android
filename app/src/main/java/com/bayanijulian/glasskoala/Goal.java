package com.bayanijulian.glasskoala;

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
    /**
     * Start date in the format of MM/DD/YYYY
     */
    private String date;
    /**
     * Start time in the format of HH:MM AM
     */
    private String time;
    /**
     * The name of the gym
     */
    private String locationName;
    /**
     * The number of minutes spent
     */
    private long duration;
    /**
     * The Place ID to retrieve more information from Places SDK by Google
     */
    private String placeId;

    public Goal() {
        this.date = "";
        this.time = "";
        this.locationName = "";
        this.duration = 0;
    }

    public Goal(String date, String time, String locationName, long duration) {
        this.date = date;
        this.time = time;
        this.locationName = locationName;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;

    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
