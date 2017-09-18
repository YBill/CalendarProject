package com.example.bill.calendarproject;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Bill on 2017/9/16.
 */

public class MonthWeekData {

    private static MonthWeekData instance;

    private List<DateData> monthContent = new ArrayList<>();
    ;
    private List<DateData> weekContent;

    private Calendar calendar;
    private int realPosition;

    public static MonthWeekData getInstance() {
        if (instance == null)
            instance = new MonthWeekData();
        return instance;
    }

    public MonthWeekData() {
        calendar = Calendar.getInstance();
    }

    private void initMonthArray(int position) {
        monthContent.clear();

        Log.e("Bill", "position+:" + position);

        calendar.setTime(new Date());

        int d = position + 1 - CellConfig.COUNT;
        calendar.add(Calendar.MONTH, d);

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayWeek = calendar.get(Calendar.DAY_OF_WEEK);
        for (int i = 0; i < firstDayWeek - 1; i++) {
            monthContent.add(new DateData());
        }


        int thisMonthDayNumber = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        CellConfig.MONTH_ROW = calculateMonthRow(firstDayWeek, thisMonthDayNumber);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        for (int day = 1; day < thisMonthDayNumber + 1; day++) {
            DateData addDate = new DateData(year, month, day);
            monthContent.add(addDate);
        }
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

    public List<DateData> getData() {
        return monthContent;
    }

    public List<DateData> getData(int position) {
        initMonthArray(position);
        return monthContent;
    }

}
