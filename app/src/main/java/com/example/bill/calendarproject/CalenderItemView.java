package com.example.bill.calendarproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
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
    private Paint mPaintSelectBg;
    private boolean isPoint;// 是不是选中的焦点

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
        mPaintNormal.setTextSize(getResources().getDimension(R.dimen.si_default_text_size));

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

    private MonthListener monthListener;

    public void setMonthListener(MonthListener monthListener) {
        this.monthListener = monthListener;
    }

    int downX;
    int downY;
    long currentMS;
    int pressColumn;
    int pressRow;

    private void clear() {
        pressRow = -1;
        pressColumn = -1;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            downX = (int) event.getX();
            downY = (int) event.getY();
            currentMS = System.currentTimeMillis();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            long moveTime = System.currentTimeMillis() - currentMS;
            if (moveTime < 200 && (Math.abs(event.getX() - downX) < 20 && Math.abs(event.getY() - downY) < 20)) {
                pressRow = (int) (event.getY() / CalendarConfig.CELL_HEIGHT);
                pressColumn = (int) (event.getX() / CalendarConfig.CELL_WIDTH);

                int selectPosition = 7 * pressRow + pressColumn;
                if ((selectPosition + 1 - list.size() > 0) || (list.get(selectPosition).day == 0)) {
                    clear();
                } else {
                    DateData todayData = CalendarConfig.TODAY;
                    DateData dateData = list.get(selectPosition);

                    if (dateData.year != todayData.year || dateData.month != todayData.month || dateData.day <= todayData.day) {
                        invalidate();
                        if (monthListener != null)
                            monthListener.clickDay(dateData.year, dateData.month, dateData.day);
                    } else clear();
                }
            }
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
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

            isPoint = false;
            if (pressRow >= 0 && pressColumn >= 0) {
                int selectPosition = pressRow * 7 + (pressColumn + 1) - 1;
                if (selectPosition == i) {
                    float left = pressColumn * CalendarConfig.CELL_WIDTH;
                    float top = pressRow * CalendarConfig.CELL_HEIGHT;
                    float right = (pressColumn + 1) * CalendarConfig.CELL_WIDTH;
                    float bottom = (pressRow + 1) * CalendarConfig.CELL_HEIGHT;
                    canvas.drawOval(left, top, right, bottom, mPaintSelectBg);
                    CalendarConfig.SELECT_DAY = new DateData(data.year, data.month, data.day);
                    isPoint = true;
                    clear();
                }
            } else {
                DateData selectData = CalendarConfig.SELECT_DAY;
                if (data.year == selectData.year && data.month == selectData.month && data.day == selectData.day) {
                    float left = column * CalendarConfig.CELL_WIDTH;
                    float top = row * CalendarConfig.CELL_HEIGHT;
                    float right = (column + 1) * CalendarConfig.CELL_WIDTH;
                    float bottom = (row + 1) * CalendarConfig.CELL_HEIGHT;
                    canvas.drawOval(left, top, right, bottom, mPaintSelectBg);
                    CalendarConfig.SELECT_DAY = new DateData(data.year, data.month, data.day);
                    isPoint = true;
                }
            }


            /*float left = column * CalendarConfig.CELL_WIDTH;
            float top = row * CalendarConfig.CELL_WIDTH;
            float right = (column+1) * CalendarConfig.CELL_WIDTH;
            float bottom = (row+1) * CalendarConfig.CELL_WIDTH;
            canvas.drawOval(left, top, right, bottom, mPaintNormalBg);*/

            if (data.day > 0) {
                x = (CalendarConfig.CELL_WIDTH - textWidth) / 2 + column * CalendarConfig.CELL_WIDTH;
                y = (CalendarConfig.CELL_HEIGHT - fontHeight) / 2 + row * CalendarConfig.CELL_HEIGHT + getResources().getDimension(R.dimen.activity_horizontal_margin);
                if (isPoint) {
                    mPaintNormal.setColor(Color.parseColor("#ffffff"));
                } else {
                    DateData todayData = CalendarConfig.TODAY;
                    if (data.year == todayData.year && data.month == todayData.month) {
                        if (data.day == todayData.day) {
                            mPaintNormal.setColor(Color.parseColor("#ff4259"));
                        } else if (data.day > todayData.day) {
                            mPaintNormal.setColor(Color.parseColor("#dcdcdc"));
                        } else {
                            mPaintNormal.setColor(Color.parseColor("#000000"));
                        }
                    } else {
                        mPaintNormal.setColor(Color.parseColor("#000000"));
                    }
                }
                canvas.drawText(content, x, y, mPaintNormal);
            }


        }
    }
}
