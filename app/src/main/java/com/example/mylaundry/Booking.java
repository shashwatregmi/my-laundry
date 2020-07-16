package com.example.mylaundry;

public class Booking {

    int washer, hour, minute, endhr;
    String date;


    public Booking(int washer, int hour, int minute, String date, int endhr){
        this.washer = washer;
        this.hour = hour;
        this.minute = minute;
        this.date = date;
        this.endhr = endhr;
    }

    public Booking(){}

    public void setWasher(int washer) {
        this.washer = washer;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setEndHr(int endhr) {
        this.endhr = endhr;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDay(String day) {}
    public void setMonth(String day) {}

    public int getWasher() {
        return washer;
    }

    public int getHour() {
        return hour;
    }

    public int getEndHr() {
        return endhr;
    }

    public int getMinute() {
        return minute;
    }

    public String getDate() {
        return date;
    }

    public String getDay() {
        return date.substring(0, 2);
    }

    public String getMonth() {
        String temp = date.substring(3, 5);
        switch (temp) {
            case "01":
                return "Jan";
            case "02":
                return "Feb";
            case "03":
                return "Mar";
            case "04":
                return "April";
            case "05":
                return "May";
            case "06":
                return "June";
            case "07":
                return "July";
            case "08":
                return "Aug";
            case "09":
                return "Sept";
            case "10":
                return "Oct";
            case "11":
                return "Nov";
            case "12":
                return "Dec";
        }
        return null;
    }

}
