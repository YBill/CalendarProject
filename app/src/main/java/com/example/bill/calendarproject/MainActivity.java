package com.example.bill.calendarproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private TextView yearText;
    private TextView monthText;
    private ImageView image;
    private boolean isShrinking = false;//是否是收缩状态

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendarView = (CalendarView) findViewById(R.id.calendar_view);
        yearText = (TextView) findViewById(R.id.tv_year);
        monthText = (TextView) findViewById(R.id.tv_month);
        image = (ImageView) findViewById(R.id.image);

        calendarView.setMonthScrollListener(new CalendarView.MonthScrollListener() {
            @Override
            public void onMonthChange(int year, int month) {
                yearText.setText(year + "年");
                monthText.setText(month + "月");
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShrinking) {
                    image.setImageResource(R.mipmap.up);
                    isShrinking = false;
                    calendarView.expand();
                } else {
                    image.setImageResource(R.mipmap.down);
                    isShrinking = true;
                    calendarView.shrink();
                }
            }
        });

    }
}
