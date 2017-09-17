package com.example.bill.calendarproject;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Bill on 2017/9/17.
 */

public class CalendarViewAdapter extends PagerAdapter {

    private List<CalenderItemView> views;

    public CalendarViewAdapter(List<CalenderItemView> views) {
        this.views = views;
    }

    @Override
    public int getCount() {
        return views.size();
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
        container.addView(views.get(position));
        return views.get(position);
    }

    /**
     * 销毁 一个 页卡
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 删除
        container.removeView(views.get(position));
    }

}
