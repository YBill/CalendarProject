package com.example.bill.calendarproject;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Bill on 2017/9/17.
 */

public class CalendarViewAdapter extends PagerAdapter {

    private Context context;
    private LinkedList<CalenderItemView> cache = new LinkedList();
    private HashMap<Integer, CalenderItemView> views = new HashMap<>();

    public CalendarViewAdapter(Context context) {
        this.context = context;
    }

    public CalenderItemView getCalendarView(int position) {
        return views.get(position);
    }

    @Override
    public int getCount() {
        return CalendarConfig.COUNT;
    }

    /**
     * 判断 是 view 是否来自对象
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 实例化 一个 页卡
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // 添加一个 页卡
        CalenderItemView view;
        if (!cache.isEmpty()) {
            view = cache.removeFirst();
        } else {
            view = new CalenderItemView(context);
        }
        container.addView(view);
        views.put(position, view);
        return view;
    }

    /**
     * 销毁 一个 页卡
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 删除
        container.removeView((View) object);
        cache.addLast((CalenderItemView) object);
        views.remove(position);
    }

}
