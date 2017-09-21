package com.example.bill.calendarproject;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Bill on 2017/9/21.
 */

public class WeekColumnView extends LinearLayout {

    private String[] week = {"日", "一", "二", "三", "四", "五", "六"};
    private TextView[] textView = new TextView[7];
    private int textSize = 13;
    private String textColor = "#9B9B9B";

    public WeekColumnView(Context context) {
        super(context);
        init(context);
    }

    public WeekColumnView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WeekColumnView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutParams lp = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
        for (int i = 0; i < 7; i++) {
            textView[i] = new TextView(context);
            textView[i].setGravity(Gravity.CENTER);
            textView[i].setTextSize(textSize);
            textView[i].setTextColor(Color.parseColor(textColor));
            textView[i].setText(week[i]);
            this.addView(textView[i], lp);
        }
    }

}
