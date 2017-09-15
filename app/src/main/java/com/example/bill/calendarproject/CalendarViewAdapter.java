package com.example.bill.calendarproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Bill on 2017/9/15.
 */

public class CalendarViewAdapter extends FragmentStatePagerAdapter {

    public CalendarViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }
}
