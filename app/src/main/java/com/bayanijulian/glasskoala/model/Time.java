package com.bayanijulian.glasskoala.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Time implements Parcelable, Comparable<Time> {
    private Long year;
    private Long month;
    private Long day;

    private Long startHour;
    private Long startMinutes;

    private Long endHour;
    private Long endMinutes;

    private Calendar startTime = Calendar.getInstance();
    private Calendar endTime = Calendar.getInstance();


    public Time() {

    }

    public void setYear(Long year) {
        this.year = year;
        startTime.set(Calendar.YEAR, convertToInt(year));
        endTime.set(Calendar.YEAR, convertToInt(year));
    }

    public void setMonth(Long month) {
        this.month = month;
        startTime.set(Calendar.MONTH, convertToInt(month));
        endTime.set(Calendar.MONTH, convertToInt(month));
    }

    public void setDay(Long day) {
        this.day = day;
        startTime.set(Calendar.DATE, convertToInt(day));
        endTime.set(Calendar.DATE, convertToInt(day));
    }

    public void setStartHour(Long startHour) {
        this.startHour = startHour;
        startTime.set(Calendar.HOUR_OF_DAY, convertToInt(startHour));
    }

    public void setStartMinutes(Long startMinutes) {
        this.startMinutes = startMinutes;
        startTime.set(Calendar.MINUTE, convertToInt(startMinutes));
    }

    public void setEndHour(Long endHour) {
        this.endHour = endHour;
        endTime.set(Calendar.HOUR_OF_DAY, convertToInt(endHour));
    }

    public void setEndMinutes(Long endMinutes) {
        this.endMinutes = endMinutes;
        endTime.set(Calendar.MINUTE, convertToInt(endMinutes));
    }

    public Long getYear() {
        return year;
    }

    public Long getMonth() {
        return month;
    }

    public Long getDay() {
        return day;
    }

    public Long getStartHour() {
        return startHour;
    }

    public Long getStartMinutes() {
        return startMinutes;
    }

    public Long getEndHour() {
        return endHour;
    }

    public Long getEndMinutes() {
        return endMinutes;
    }

    @Exclude
    public void setStartTime(int startHour, int startMinutes) {
        this.startHour = convertToLong(startHour);
        this.startMinutes = convertToLong(startMinutes);

        this.startTime.set(Calendar.HOUR_OF_DAY, convertToInt(this.startHour));
        this.startTime.set(Calendar.MINUTE, convertToInt(this.startMinutes));
    }
    @Exclude
    public void setEndTime(int endHour, int endMinutes) {
        this.endHour = convertToLong(endHour);
        this.endMinutes = convertToLong(endMinutes);

        this.endTime.set(Calendar.HOUR_OF_DAY, convertToInt(this.endHour));
        this.endTime.set(Calendar.MINUTE, convertToInt(this.endMinutes));
    }
    @Exclude
    public void setDate(int year, int month, int day) {
        this.year = convertToLong(year);
        this.month = convertToLong(month);
        this.day = convertToLong(day);

        this.startTime.set(convertToInt(this.year), convertToInt(this.month), convertToInt(this.day));
        this.endTime.set(convertToInt(this.year), convertToInt(this.month), convertToInt(this.day));
    }

    @Exclude
    public String getStartTime() {
        return SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT).format(startTime.getTime());
    }

    @Exclude
    public String getEndTime() {
        return SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT).format(endTime.getTime());
    }

    @Exclude
    public String getDate() {
        return SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT).format(startTime.getTime());
    }

    @Exclude
    public String getDuration() {
        long duration = endTime.getTimeInMillis() - startTime.getTimeInMillis();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        return String.valueOf(minutes) + " minutes";
    }

    private int convertToInt(Long val) {
        return (int)(long) val;
    }

    private Long convertToLong(int val) {
        return (Long)(long)val;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.year);
        dest.writeValue(this.month);
        dest.writeValue(this.day);
        dest.writeValue(this.startHour);
        dest.writeValue(this.startMinutes);
        dest.writeValue(this.endHour);
        dest.writeValue(this.endMinutes);
        dest.writeSerializable(this.startTime);
        dest.writeSerializable(this.endTime);
    }

    protected Time(Parcel in) {
        this.year = (Long) in.readValue(Long.class.getClassLoader());
        this.month = (Long) in.readValue(Long.class.getClassLoader());
        this.day = (Long) in.readValue(Long.class.getClassLoader());
        this.startHour = (Long) in.readValue(Long.class.getClassLoader());
        this.startMinutes = (Long) in.readValue(Long.class.getClassLoader());
        this.endHour = (Long) in.readValue(Long.class.getClassLoader());
        this.endMinutes = (Long) in.readValue(Long.class.getClassLoader());
        this.startTime = (Calendar) in.readSerializable();
        this.endTime = (Calendar) in.readSerializable();
    }

    public static final Parcelable.Creator<Time> CREATOR = new Parcelable.Creator<Time>() {
        @Override
        public Time createFromParcel(Parcel source) {
            return new Time(source);
        }

        @Override
        public Time[] newArray(int size) {
            return new Time[size];
        }
    };

    @Override
    public int compareTo(Time that) {
        return this.startTime.compareTo(that.startTime);
    }
}
