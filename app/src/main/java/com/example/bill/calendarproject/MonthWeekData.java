package com.example.bill.calendarproject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Bill on 2017/9/16.
 */

public class MonthWeekData {

    private static MonthWeekData instance;

    private List<DateData> monthList = new ArrayList<>();
    private List<DateData> weekList = new ArrayList<>();
    private List<DateData> weekPreList = new ArrayList<>();

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

        if (CalendarConfig.iSScroll) {
            DateData selectData = CalendarConfig.SELECT_MONTH;
            calendar.set(selectData.year, selectData.month - 1, 1);
            calendar.add(Calendar.MONTH, position);

            CalendarConfig.SELECT_MONTH = new DateData(
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 0);

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

        } else {
            DateData selectData = CalendarConfig.SELECT_DAY;
            calendar.set(selectData.year, selectData.month - 1, selectData.day);

            CalendarConfig.SELECT_MONTH = new DateData(
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 0);

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
        }
        return monthList;
    }

    private List<DateData> initWeekData(int position) {
        if (CalendarConfig.iSScroll) {
            weekPreList.clear();
            weekPreList.addAll(weekList);
            weekList.clear();

            if (position > 0) {
                // 左滑，后一周
                DateData lastData = weekPreList.get(6);
                calendar.set(lastData.year, lastData.month - 1, 1);
                int thisMonthDayNumber = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                int diffDay = lastData.day + 7;
                int twoMonthDiff = diffDay - thisMonthDayNumber;
                if (twoMonthDiff <= 0) {
                    CalendarConfig.SELECT_MONTH = new DateData(lastData.year, lastData.month, 0);
                    for (int i = 0; i < 7; i++) {
                        DateData data = new DateData(lastData.year, lastData.month, lastData.day + 1 + i);
                        weekList.add(data);
                    }
                } else {
                    // 差月了
                    for (int i = 0; i < 7 - twoMonthDiff; i++) {
                        DateData data = new DateData(lastData.year, lastData.month, lastData.day + 1 + i);
                        weekList.add(data);
                    }
                    calendar.add(Calendar.MONTH, 1);
                    CalendarConfig.SELECT_MONTH = new DateData(
                            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 0);
                    for (int i = 0; i < twoMonthDiff; i++) {
                        DateData data = new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, i + 1);
                        weekList.add(data);
                    }
                }
            } else if (position < 0) {
                // 右滑，前一周
                DateData firstData = weekPreList.get(0);
                int diffDay = firstData.day - 7;
                if (diffDay > 0) {
                    CalendarConfig.SELECT_MONTH = new DateData(firstData.year, firstData.month, 0);
                    for (int i = 0; i < 7; i++) {
                        DateData data = new DateData(firstData.year, firstData.month, firstData.day - 7 + i);
                        weekList.add(data);
                    }
                } else {
                    // 差月了
                    calendar.set(firstData.year, firstData.month - 1, 1);
                    calendar.add(Calendar.MONTH, -1);
                    CalendarConfig.SELECT_MONTH = new DateData(
                            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 0);
                    int preMonthDayNumber = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    for (int i = 0; i < 1 - diffDay; i++) {
                        DateData data = new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, preMonthDayNumber + diffDay + i);
                        weekList.add(data);
                    }
                    for (int i = 0; i < 6 + diffDay; i++) {// 7-(1-diffDay)
                        DateData data = new DateData(firstData.year, firstData.month, i + 1);
                        weekList.add(data);
                    }
                }
            }
        } else {
            weekList.clear();
            DateData selectData = CalendarConfig.SELECT_DAY;
            calendar.set(selectData.year, selectData.month - 1, selectData.day);
            CalendarConfig.SELECT_MONTH = new DateData(
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 0);
            int thisMonthDayNumber = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            int thisDayWeek = calendar.get(Calendar.DAY_OF_WEEK);
            int firstDay = selectData.day - thisDayWeek + 1; // 选中的这周第一天
            if (firstDay > 0) {
                int diffTwoMonth = firstDay + 6 - thisMonthDayNumber;
                if (diffTwoMonth <= 0) {
                    // 本月数据
                    for (int i = 0; i < 7; i++) {
                        DateData data = new DateData(selectData.year, selectData.month, firstDay + i);
                        weekList.add(data);
                    }
                } else {
                    // + 下个月
                    for (int i = 0; i < 7 - diffTwoMonth; i++) {
                        DateData data = new DateData(selectData.year, selectData.month, firstDay + i);
                        weekList.add(data);
                    }
                    calendar.add(Calendar.MONTH, 1);
                    for (int i = 0; i < diffTwoMonth; i++) {
                        DateData data = new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, i + 1);
                        weekList.add(data);
                    }
                }
            } else {
                //+上个月
                calendar.add(Calendar.MONTH, -1);
                int preMonthDayNumber = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                int length = 1 - firstDay;
                for (int i = 0; i < length; i++) {
                    DateData data = new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, preMonthDayNumber - length + 1 + i);
                    weekList.add(data);
                }
                for (int i = 0; i < 7 - length; i++) {
                    DateData data = new DateData(selectData.year, selectData.month, i + 1);
                    weekList.add(data);
                }
            }
        }

        CalendarConfig.SELECT_WEEK = weekList;

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
            return initWeekData(position);
        }
        return initMonthData(position);
    }

}
