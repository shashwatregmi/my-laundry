package com.example.mylaundry;

public class Booking {

    int washer, hour, minute;
    String date;

    public Booking(int washer, int hour, int minute, String date){
        this.washer = washer;
        this.hour = hour;
        this.minute = minute;
        this.date = date;
    }

    public void setWasher(int washer) {
        this.washer = washer;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getWasher() {
        return washer;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String getDate() {
        return date;
    }
}
