package com.example.bill.calendarproject;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bill on 2017/9/15.
 */

public class CalendarView extends ViewPager {

    private CalendarViewAdapter adapter;
    private int currentPosition = 0;

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

    private List<CalenderItemView> list;

    private void init(FragmentActivity activity) {
        Resources resources = activity.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        CellConfig.WIDTH = width / 7;

        list = new ArrayList<>();
        for (int i = 0; i < CellConfig.COUNT; i++) {
            CalenderItemView view = new CalenderItemView(activity);
            list.add(view);
        }

        this.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                currentPosition = position;

                List<DateData> dateDataList = MonthWeekData.getInstance().getData(position);
                list.get(position).setData(dateDataList);

                DateData dateData = dateDataList.get(dateDataList.size() - 1);
                if (monthScrollListener != null)
                    monthScrollListener.onMonthChange(dateData.year, dateData.month);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        adapter = new CalendarViewAdapter(list);
        this.setAdapter(adapter);
        this.setCurrentItem(CellConfig.COUNT);
    }

    public void expand() {
        CellConfig.IS_WEEK = false;
        this.requestLayout();
    }

    public void shrink() {
        CellConfig.IS_WEEK = true;
        this.requestLayout();
    }

    private MonthScrollListener monthScrollListener;

    public void setMonthScrollListener(MonthScrollListener monthScrollListener) {
        this.monthScrollListener = monthScrollListener;
    }

    public interface MonthScrollListener {
        void onMonthChange(int year, int month);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = measureHeight();
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int measureHeight() {
        Log.e("Bill", "CellConfig.IS_WEEK:" + CellConfig.IS_WEEK);
        if (CellConfig.IS_WEEK)
            return CellConfig.WIDTH;
        else
            return CellConfig.WIDTH * CellConfig.MONTH_ROW;
    }

}
