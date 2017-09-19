package com.example.bill.calendarproject;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;

import java.util.List;

/**
 * Created by Bill on 2017/9/15.
 */

public class CalendarView extends ViewPager {

    private CalendarViewAdapter adapter;
    private FragmentActivity activity;
    private List<DateData> dateDataList;
    private int currentPosition;

    public CalendarView(Context context) {
        super(context);
        if (context instanceof FragmentActivity) {
            init((FragmentActivity) context);
        }
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (context instanceof FragmentActivity) {
            init((FragmentActivity) context);
        }
    }

    private void init(final FragmentActivity activity) {
        this.activity = activity;
        Resources resources = activity.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        CalendarConfig.CELL_WIDTH = width / 7;

        this.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                Log.e("Bill", "position:" + position);
                currentPosition = position;
                dateDataList = MonthWeekData.getInstance().getData(position);
                setCalendarData(position);

                setCallback();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        adapter = new CalendarViewAdapter(activity);
        this.setAdapter(adapter);
        setCurrentItem(CalendarConfig.COUNT / 2, false);
    }

    // 为日历设置数据
    private void setCalendarData(final int position) {
        if (adapter.getCalendarView(position) != null) {
            adapter.getCalendarView(position).setData(dateDataList);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setCalendarData(position);
                }
            }, 50);
        }
    }

    private void setCallback() {
        if (monthScrollListener != null) {
            DateData dateData = dateDataList.get(dateDataList.size() - 1);
            monthScrollListener.onMonthChange(dateData.year, dateData.month);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setCallback();
                }
            }, 50);
        }
    }

    public void expand() {
        CalendarConfig.IS_WEEK = false;
        this.requestLayout();
    }

    public void shrink() {
        CalendarConfig.IS_WEEK = true;
        this.requestLayout();
//        adapter.getCalendarView(currentPosition).setData(MonthWeekData.getInstance().getData(0));
    }

    private MonthScrollListener monthScrollListener;

    public void setMonthScrollListener(MonthScrollListener monthScrollListener) {
        this.monthScrollListener = monthScrollListener;
    }

    public interface MonthScrollListener {
        void onMonthChange(int year, int month);
    }

    private float beforeX;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                beforeX = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float motionValue = ev.getX() - beforeX;
                if (motionValue < 0) { // 左滑
                    DateData selectData = CalendarConfig.SELECT_DAY;
                    DateData today = CalendarConfig.TODAY;
                    if (selectData.year == today.year && selectData.month == today.month)
                        return true;
                }
                beforeX = ev.getX();
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = measureHeight();
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int measureHeight() {
        if (CalendarConfig.IS_WEEK)
            return CalendarConfig.CELL_WIDTH;
        else
            return CalendarConfig.CELL_WIDTH * CalendarConfig.MONTH_ROW;
    }

}
