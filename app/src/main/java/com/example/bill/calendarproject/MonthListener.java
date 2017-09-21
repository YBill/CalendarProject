package com.example.bill.calendarproject;

/**
 * Created by Bill on 2017/9/21.
 */

public interface MonthListener {

    void scrollMonth(int year, int month);

    void clickDay(int year, int month, int day);
}
