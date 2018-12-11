package com.bayanijulian.glasskoala;

/**
 * Represents the goals that a user has set for themselves.
 * Goals are when they chose to go to the gym at a certain time
 * and the amount of time they plan to stay there
 */
public class Goal {
    /**
     * Unique Identifier in the database
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
    private String location;
    /**
     * The number of minutes spent
     */
    private long duration;


    public Goal() {
        this.date = "";
        this.time = "";
        this.location = "";
        this.duration = 0;
    }

    public Goal(String date, String time, String location, long duration) {
        this.date = date;
        this.time = time;
        this.location = location;
        this.duration = duration;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
