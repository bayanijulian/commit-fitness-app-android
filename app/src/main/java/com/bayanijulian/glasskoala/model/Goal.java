package com.bayanijulian.glasskoala.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.firestore.Exclude;

/**
 * Represents the goals that a user has set for themselves.
 * Goals are when they chose to go to the gym at a certain time
 * and the amount of time they plan to stay there
 */
public class Goal implements Parcelable, Comparable<Goal>{
    public static final String LABEL = "com.bayanijulian.glasskoala.model.Goal";
    /**
     * Unique Identifier used in Firebase Firestore
     */
    private String id;
    private String userId;
    private Location location = new Location();
    private Time time = new Time();

    public Goal() {
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    @Exclude
    public void setId(String id) {
        this.id = id;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Location getLocation() {
        return location;
    }

    public Time getTime() {
        return time;
    }

    @Exclude
    public void setStartTime(int startHour, int startMinutes) {
        time.setStartTime(startHour, startMinutes);
    }

    @Exclude
    public void setEndTime(int endHour, int endMinutes) {
        time.setEndTime(endHour, endMinutes);
    }

    @Exclude
    public String getStartTime() {
        return time.getStartTime();
    }
    @Exclude
    public String getEndTime() {
        return time.getEndTime();
    }
    @Exclude
    public void setDate(int year, int hour, int day) {
        time.setDate(year, hour, day);
    }

    @Exclude
    public String getDate() {
        return time.getDate();
    }

    @Exclude
    public String getDuration() {
        return this.time.getDuration();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeParcelable(this.location, flags);
        dest.writeParcelable(this.time, flags);
    }

    protected Goal(Parcel in) {
        this.id = in.readString();
        this.location = in.readParcelable(Location.class.getClassLoader());
        this.time = in.readParcelable(Time.class.getClassLoader());
    }

    public static final Parcelable.Creator<Goal> CREATOR = new Parcelable.Creator<Goal>() {
        @Override
        public Goal createFromParcel(Parcel source) {
            return new Goal(source);
        }

        @Override
        public Goal[] newArray(int size) {
            return new Goal[size];
        }
    };

    @Override
    public int compareTo(Goal that) {
        return this.time.compareTo(that.getTime());
    }
}
