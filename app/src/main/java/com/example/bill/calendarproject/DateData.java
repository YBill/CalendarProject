package com.example.bill.calendarproject;

/**
 * Created by Bill on 2017/9/16.
 */

public class DateData {

    public int year;
    public int month;
    public int day;
    public int hour;
    public int minute;

    public DateData(int year, int month, int day) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = 0;
        this.minute = 0;
    }

    public DateData() {
    }

}
