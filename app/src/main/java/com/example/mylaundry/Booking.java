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

    public String getDay() {
        return date.substring(0, 2);
    }

    public String getMonth() {
        String temp = date.substring(3, 5);
        if (temp == "01"){
            return "Jan";
        } else if (temp == "02"){
            return "Feb";
        } else if (temp == "03") {
            return "Mar";
        } else if (temp == "04") {
            return "April";
        } else if (temp == "05") {
            return "May";
        } else if (temp == "06") {
            return "June";
        } else if (temp == "07") {
            return "July";
        } else if (temp == "08") {
            return "Aug";
        } else if (temp == "09") {
            return "Sept";
        } else if (temp == "10") {
            return "Oct";
        } else if (temp == "11") {
            return "Nov";
        } else if (temp == "12") {
            return "Dec";
        }
        return null;
    }

}
