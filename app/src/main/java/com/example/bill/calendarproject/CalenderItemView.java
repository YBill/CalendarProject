package com.example.bill.calendarproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
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
        mPaintNormal.setColor(Color.BLACK);
        mPaintNormal.setTextSize(getResources().getDimension(R.dimen.si_default_text_size));

        mPaintNormalBg = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintNormalBg.setColor(Color.RED);
    }

    public void setData(List<DateData> list) {
        this.list = list;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (list.size() < 1)
            return;
        int length = list.size();
        int row, column = 0;
        float x, y;
        for (int i = 0; i < length; i++) {
            DateData date = list.get(i);

            String content = String.valueOf(date.day);
            Paint.FontMetrics fontMetrics = mPaintNormal.getFontMetrics();
            float fontHeight = fontMetrics.bottom - fontMetrics.top;
            float textWidth = mPaintNormal.measureText(content);

            column++;
            row = i / 7;
            if (i % 7 == 0) {
                column = 0;
            }

            x = (CellConfig.WIDTH - textWidth) / 2 + column * CellConfig.WIDTH;
            y = (CellConfig.WIDTH - fontHeight) / 2 + row * CellConfig.WIDTH+ getResources().getDimension(R.dimen.activity_horizontal_margin);

            /*float left = column * CellConfig.WIDTH;
            float top = row * CellConfig.WIDTH;
            float right = (column+1) * CellConfig.WIDTH;
            float bottom = (row+1) * CellConfig.WIDTH;
            canvas.drawOval(left, top, right, bottom, mPaintNormalBg);*/

            canvas.drawText("" + date.day, x, y, mPaintNormal);
        }
    }
}
