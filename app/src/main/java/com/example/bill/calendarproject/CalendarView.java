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
    private FragmentActivity activity;

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

//    private List<CalenderItemView> itemViewList = new ArrayList<>();

    private void init(final FragmentActivity activity) {
        this.activity = activity;
        Resources resources = activity.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        CalendarConfig.CELL_WIDTH = width / 7;

        /*for (int i = 0; i < CalendarConfig.COUNT; i++) {
            CalenderItemView view = new CalenderItemView(activity);
            itemViewList.add(view);
        }*/

        this.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                Log.e("Bill", "position:" + position);
                List<DateData> dateDataList = MonthWeekData.getInstance().getData(position);
//                itemViewList.get(position).setData(dateDataList);


                DateData dateData = dateDataList.get(dateDataList.size() - 1);
                if (monthScrollListener != null)
                    monthScrollListener.onMonthChange(dateData.year, dateData.month);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        adapter = new CalendarViewAdapter(activity);
        this.setAdapter(adapter);
        setCurrentItem(CalendarConfig.COUNT / 2, false);
    }

    /*private void refreshView() {
        itemViewList.clear();
        for (int i = 0; i < CalendarConfig.COUNT; i++) {
            CalenderItemView view = new CalenderItemView(activity);
            itemViewList.add(view);
        }
        adapter.setViews(itemViewList);
        adapter.notifyDataSetChanged();
    }*/

    public void expand() {
        CalendarConfig.IS_WEEK = false;
        this.requestLayout();
    }

    public void shrink() {
        CalendarConfig.IS_WEEK = true;
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
        if (CalendarConfig.IS_WEEK)
            return CalendarConfig.CELL_WIDTH;
        else
            return CalendarConfig.CELL_WIDTH * CalendarConfig.MONTH_ROW;
    }

}
