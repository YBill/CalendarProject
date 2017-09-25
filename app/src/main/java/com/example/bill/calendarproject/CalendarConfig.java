package com.example.bill.calendarproject;

import java.util.List;

/**
 * Created by Bill on 2017/9/16.
 */
public class CalendarConfig {
    public static int COUNT = 1000000000; // 总页数（总月数）
    public static int CELL_WIDTH; // 每个日的宽
    public static int CELL_HEIGHT; // 每个日的高
    public static int MONTH_ROW; // 每个月的总行数
    public static boolean IS_WEEK = false; // 是不是只显示星期
    public static DateData TODAY; // 今天
    public static DateData SELECT_DAY; // 当前焦点位置
    public static DateData SELECT_MONTH; // 当前选中的月
    public static List<DateData> MARK_DAY_LIST; // 需要标记的天
    public static boolean iSScroll; // 是否是滑动
}
