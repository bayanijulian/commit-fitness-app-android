package com.bayanijulian.glasskoala.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Time {
    private Long year;
    private Long month;
    private Long day;

    private Long startHour;
    private Long startMinutes;

    private Long endHour;
    private Long endMinutes;

    private Calendar startTime;
    private Calendar endTime;
    private Calendar date;

    public Time() {

    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public Long getMonth() {
        return month;
    }

    public void setMonth(Long month) {
        this.month = month;
    }

    public Long getDay() {
        return day;
    }

    public void setDay(Long day) {
        this.day = day;
    }

    public Long getStartHour() {
        return startHour;
    }

    public void setStartHour(Long startHour) {
        this.startHour = startHour;
    }

    public Long getStartMinutes() {
        return startMinutes;
    }

    public void setStartMinutes(Long startMinutes) {
        this.startMinutes = startMinutes;
    }

    public Long getEndHour() {
        return endHour;
    }

    public void setEndHour(Long endHour) {
        this.endHour = endHour;
    }

    public Long getEndMinutes() {
        return endMinutes;
    }

    public void setEndMinutes(Long endMinutes) {
        this.endMinutes = endMinutes;
    }

    public void setStartTime(int startHour, int startMinutes) {
        this.startHour = convertToLong(startHour);
        this.startMinutes = convertToLong(startMinutes);

        this.startTime = Calendar.getInstance();
        this.startTime.set(Calendar.HOUR_OF_DAY, convertToInt(this.startHour));
        this.startTime.set(Calendar.MINUTE, convertToInt(this.startMinutes));
    }

    public void setEndTime(int endHour, int endMinutes) {
        this.endHour = convertToLong(endHour);
        this.endMinutes = convertToLong(endMinutes);

        this.endTime = Calendar.getInstance();
        this.endTime.set(Calendar.HOUR_OF_DAY, convertToInt(this.endHour));
        this.endTime.set(Calendar.MINUTE, convertToInt(this.endMinutes));
    }

    public void setDate(int year, int month, int day) {
        this.year = convertToLong(year);
        this.month = convertToLong(month);
        this.day = convertToLong(day);

        this.date = Calendar.getInstance();
        this.date.set(convertToInt(this.year), convertToInt(this.month), convertToInt(this.day));
    }

    public String getStartTime() {
        return SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT).format(startTime.getTime());
    }

    public String getEndTime() {
        return SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT).format(endTime.getTime());
    }

    public String getDate() {
        return SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT).format(date.getTime());
    }

    public String getDuration() {
        long duration = endTime.getTimeInMillis() - startTime.getTimeInMillis();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        return String.valueOf(duration) + " minutes";
    }

    private int convertToInt(Long val) {
        return (int)(long) val;
    }

    private Long convertToLong(int val) {
        return (Long)(long)val;
    }
}
