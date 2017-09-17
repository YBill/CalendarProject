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

        int aa = thisMonthDayNumber - 29;
        // -1 0 1 2
        if (aa == -1) {

        } else {
            int bb = firstDayWeek + aa;
            if (bb > 7) {
                CellConfig.MONTH_ROW = 5;
            }
        }

        for (int day = 1; day < thisMonthDayNumber + 1; day++) {
            DateData addDate = new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, day);
            monthContent.add(addDate);
        }
        Log.e("Bill", "+++++++++++++");
    }

    public List<DateData> getData() {
        return monthContent;
    }

    public List<DateData> getData(int position) {
        initMonthArray(position);
        return monthContent;
    }

}
