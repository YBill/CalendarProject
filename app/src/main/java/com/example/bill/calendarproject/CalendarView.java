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
                List<DateData> dateDataList = MonthWeekData.getInstance().getData(position);
                list.get(position).setData(dateDataList);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        adapter = new CalendarViewAdapter(list);
        this.setAdapter(adapter);
        this.setCurrentItem(CellConfig.COUNT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e("Bill", "onMeasure");
        int height = measureHeight();
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int measureHeight() {
//        if (CellConfig.ifMonth)
            return (int) (CellConfig.WIDTH * 5);
//        else
//            return (int) (CellConfig.cellHeight * density);
    }

}
