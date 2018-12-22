package com.bayanijulian.glasskoala.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Time implements Parcelable, Comparable<Time> {
    private int year;
    private int month;
    private int day;

    private int startHour;
    private int startMinutes;

    private int endHour;
    private int endMinutes;

    private Calendar startTime = Calendar.getInstance();
    private Calendar endTime = Calendar.getInstance();

    public Time() {

    }

    public void setYear(int year) {
        this.year = year;
        startTime.set(Calendar.YEAR, year);
        endTime.set(Calendar.YEAR, year);
    }

    public void setMonth(int month) {
        this.month = month;
        startTime.set(Calendar.MONTH, month);
        endTime.set(Calendar.MONTH, month);
    }

    public void setDay(int day) {
        this.day = day;
        startTime.set(Calendar.DATE, day);
        endTime.set(Calendar.DATE, day);
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
        startTime.set(Calendar.HOUR_OF_DAY,startHour);
    }

    public void setStartMinutes(int startMinutes) {
        this.startMinutes = startMinutes;
        startTime.set(Calendar.MINUTE, startMinutes);
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
        endTime.set(Calendar.HOUR_OF_DAY, endHour);
    }

    public void setEndMinutes(int endMinutes) {
        this.endMinutes = endMinutes;
        endTime.set(Calendar.MINUTE, endMinutes);
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getStartMinutes() {
        return startMinutes;
    }

    public int getEndHour() {
        return endHour;
    }

    public int getEndMinutes() {
        return endMinutes;
    }

    @Exclude
    public void setStartTime(int startHour, int startMinutes) {
        this.startHour = startHour;
        this.startMinutes = startMinutes;

        this.startTime.set(Calendar.HOUR_OF_DAY, startHour);
        this.startTime.set(Calendar.MINUTE, startMinutes);
    }
    @Exclude
    public void setEndTime(int endHour, int endMinutes) {
        this.endHour = endHour;
        this.endMinutes = endMinutes;

        this.endTime.set(Calendar.HOUR_OF_DAY, endHour);
        this.endTime.set(Calendar.MINUTE, endMinutes);
    }
    @Exclude
    public void setDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;

        this.startTime.set(year, month, day);
        this.endTime.set(year, month, day);
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


    @Override
    public int compareTo(Time that) {
        return this.startTime.compareTo(that.startTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.year);
        dest.writeInt(this.month);
        dest.writeInt(this.day);
        dest.writeInt(this.startHour);
        dest.writeInt(this.startMinutes);
        dest.writeInt(this.endHour);
        dest.writeInt(this.endMinutes);
        dest.writeSerializable(this.startTime);
        dest.writeSerializable(this.endTime);
    }

    protected Time(Parcel in) {
        this.year = in.readInt();
        this.month = in.readInt();
        this.day = in.readInt();
        this.startHour = in.readInt();
        this.startMinutes = in.readInt();
        this.endHour = in.readInt();
        this.endMinutes = in.readInt();
        this.startTime = (Calendar) in.readSerializable();
        this.endTime = (Calendar) in.readSerializable();
    }

    public static final Creator<Time> CREATOR = new Creator<Time>() {
        @Override
        public Time createFromParcel(Parcel source) {
            return new Time(source);
        }

        @Override
        public Time[] newArray(int size) {
            return new Time[size];
        }
    };
}
