package com.example.bill.calendarproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bill on 2017/9/17.
 */

public class CalenderItemView extends View {

    private List<DateData> list = new ArrayList<>();

    private Paint mPaintNormal;
    private Paint mPaintNormalBg;
    private Paint mPaintSelectBg;

    public CalenderItemView(Context context) {
        super(context);
        init(context);
    }

    public CalenderItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CalenderItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaintNormal = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintNormal.setColor(Color.parseColor("#000000"));//dcdcdc
        mPaintNormal.setTextSize(getResources().getDimension(R.dimen.si_default_text_size));

        mPaintNormalBg = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintNormalBg.setColor(Color.RED);

        mPaintSelectBg = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintSelectBg.setColor(Color.parseColor("#ff4259"));

        clear();
    }

    public void setData(List<DateData> list) {
        if (list != null) {
            clear();
            this.list = list;
            invalidate();
        }
    }

    int currentX;
    int currentY;

    private void clear() {
        currentX = -1;
        currentY = -1;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            currentX = (int) event.getX();
            currentY = (int) event.getY();
            invalidate();
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        DateData today = CalendarConfig.TODAY;
        int length = list.size();
        int row, column = 0;
        float x, y;
        for (int i = 0; i < length; i++) {
            DateData data = list.get(i);

            String content = String.valueOf(data.day);
            Paint.FontMetrics fontMetrics = mPaintNormal.getFontMetrics();
            float fontHeight = fontMetrics.bottom - fontMetrics.top;
            float textWidth = mPaintNormal.measureText(content);

            column++;
            row = i / 7;
            if (i % 7 == 0) {
                column = 0;
            }


            if (currentX > 0 && currentY > 0) {
                int pressColumn = currentX / CalendarConfig.CELL_WIDTH;
                int pressRow = currentY / CalendarConfig.CELL_WIDTH;
                int selectDay = pressRow * 7 + (pressColumn + 1);
                if (selectDay - 1 == i) {
                    float left = pressColumn * CalendarConfig.CELL_WIDTH;
                    float top = pressRow * CalendarConfig.CELL_WIDTH;
                    float right = (pressColumn + 1) * CalendarConfig.CELL_WIDTH;
                    float bottom = (pressRow + 1) * CalendarConfig.CELL_WIDTH;
                    canvas.drawOval(left, top, right, bottom, mPaintSelectBg);
                    CalendarConfig.SELECT_DAY = new DateData(data.year, data.month, data.day);
                    Log.e("Bill", "::" + CalendarConfig.SELECT_DAY.toString());
                }
            } else {
                int selectDay; // 选中背景
                if (data.year == today.year && data.month == today.month) {
                    selectDay = today.day;
                } else {
                    selectDay = 1;
                }
                if (selectDay == data.day) {
                    float left = column * CalendarConfig.CELL_WIDTH;
                    float top = row * CalendarConfig.CELL_WIDTH;
                    float right = (column + 1) * CalendarConfig.CELL_WIDTH;
                    float bottom = (row + 1) * CalendarConfig.CELL_WIDTH;
                    canvas.drawOval(left, top, right, bottom, mPaintSelectBg);
                    CalendarConfig.SELECT_DAY = new DateData(data.year, data.month, data.day);
                    Log.e("Bill", "::" + CalendarConfig.SELECT_DAY.toString());
                }
            }


            /*float left = column * CalendarConfig.CELL_WIDTH;
            float top = row * CalendarConfig.CELL_WIDTH;
            float right = (column+1) * CalendarConfig.CELL_WIDTH;
            float bottom = (row+1) * CalendarConfig.CELL_WIDTH;
            canvas.drawOval(left, top, right, bottom, mPaintNormalBg);*/

            x = (CalendarConfig.CELL_WIDTH - textWidth) / 2 + column * CalendarConfig.CELL_WIDTH;
            y = (CalendarConfig.CELL_WIDTH - fontHeight) / 2 + row * CalendarConfig.CELL_WIDTH + getResources().getDimension(R.dimen.activity_horizontal_margin);
            canvas.drawText(content, x, y, mPaintNormal);


        }
    }
}
