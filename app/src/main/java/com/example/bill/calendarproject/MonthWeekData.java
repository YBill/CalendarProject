package com.example.bill.calendarproject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Bill on 2017/9/16.
 */

public class MonthWeekData {

    private static MonthWeekData instance;

    private List<DateData> monthList = new ArrayList<>();
    private List<DateData> weekList;

    private Calendar calendar;

    public static MonthWeekData getInstance() {
        if (instance == null)
            instance = new MonthWeekData();
        return instance;
    }

    public MonthWeekData() {
        calendar = Calendar.getInstance();
    }

    private List<DateData> initMonthData(int position) {
        monthList.clear();

        calendar.setTime(new Date());
        DateData todayData = new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        CalendarConfig.TODAY = todayData;

        int diff = position + 1 - CalendarConfig.COUNT;
        calendar.add(Calendar.MONTH, diff);

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayWeek = calendar.get(Calendar.DAY_OF_WEEK);
        for (int i = 0; i < firstDayWeek - 1; i++) {
            monthList.add(new DateData());
        }

        int thisMonthDayNumber = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        CalendarConfig.MONTH_ROW = calculateMonthRow(firstDayWeek, thisMonthDayNumber);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        for (int day = 1; day < thisMonthDayNumber + 1; day++) {
            DateData addDate = new DateData(year, month, day);
            monthList.add(addDate);
        }

        return monthList;
    }

    private List<DateData> initWeekData(DateData selectData) {
        weekList.clear();

        if (monthList.size() > 0) {
            int day = selectData.day;
            int currentPosition = 0;
            for (int i = 0; i < monthList.size(); i++) {
                if (day == monthList.get(i).day) {
                    currentPosition = i;
                    break;
                }
            }
            int row = currentPosition / 7;

            weekList.addAll(monthList.subList(row * 7, (row + 1) * 7));

            monthList.clear();
        } else {

        }


        return weekList;
    }

    /**
     * 计算每个月占几行
     *
     * @param firstDayWeek
     * @param thisMonthDayNumber
     * @return
     */
    private int calculateMonthRow(int firstDayWeek, int thisMonthDayNumber) {
        int _28_week_num = firstDayWeek - 1; // 计算28号是星期几
        if (_28_week_num == 0)
            _28_week_num = 7;

        int diff_28 = thisMonthDayNumber - 28; // 总天数-28

        int total_28_diff = _28_week_num + diff_28; // 计算剩余天数有没有超过一行

        int row;
        if (_28_week_num == 7) {
            // 28号在周六上了
            if (diff_28 == 0) {
                row = 4;
            } else {
                row = 5;
            }
        } else {
            if (total_28_diff > 7) {
                row = 6;
            } else {
                row = 5;
            }
        }
        return row;
    }

    public List<DateData> getData(int position) {
        if (CalendarConfig.IS_WEEK) {
            return initWeekData(null);
        }
        return initMonthData(position);
    }

}
