package com.example.bill.calendarproject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Bill on 2017/9/15.
 */

public class CalendarView extends ViewPager {

    private CalendarViewAdapter adapter;
    private FragmentActivity activity;
    private List<DateData> dateDataList;
    private int currentPosition;
    private int diffPosition = 0;

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

    private void init(final FragmentActivity activity) {
        this.activity = activity;
        Resources resources = activity.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        CalendarConfig.CELL_WIDTH = width / 7;
        CalendarConfig.TODAY = new DateData(
                Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        CalendarConfig.SELECT_DAY = CalendarConfig.TODAY;
        CalendarConfig.SELECT_MONTH = CalendarConfig.TODAY;
        CalendarConfig.IS_WEEK = false;

        adapter = new CalendarViewAdapter(activity);
        this.setAdapter(adapter);
        currentPosition = CalendarConfig.COUNT / 2;
        setCurrentItem(currentPosition, false);

        CalendarConfig.iSScroll = false;
        dateDataList = MonthWeekData.getInstance().getData(0);
        setCalendarData(currentPosition);
        setCallback();

        this.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                if (position > currentPosition) {
                    diffPosition = 1;
                } else {
                    diffPosition = -1;
                }
                currentPosition = position;
                CalendarConfig.iSScroll = true;
                dateDataList = MonthWeekData.getInstance().getData(diffPosition);
                adapter.getCalendarView(position).setData(dateDataList);
                if (monthScrollListener != null) {
                    monthScrollListener.onMonthChange(CalendarConfig.SELECT_MONTH.year, CalendarConfig.SELECT_MONTH.month);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    // 为日历设置数据
    private void setCalendarData(final int position) {
        if (adapter.getCalendarView(position) != null) {
            adapter.getCalendarView(position).setData(dateDataList);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setCalendarData(position);
                }
            }, 50);
        }
    }

    private void setCallback() {
        if (monthScrollListener != null) {
            monthScrollListener.onMonthChange(CalendarConfig.SELECT_MONTH.year, CalendarConfig.SELECT_MONTH.month);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setCallback();
                }
            }, 50);
        }
    }

    /**
     * 展开
     */
    public void expand() {
        if (CalendarConfig.IS_WEEK) {
            CalendarConfig.IS_WEEK = false;
            CalendarConfig.iSScroll = false;
            setPagerHeight(CalendarConfig.CELL_WIDTH, CalendarConfig.MONTH_ROW * CalendarConfig.CELL_WIDTH);
            adapter.getCalendarView(currentPosition).setData(MonthWeekData.getInstance().getData(0));
        }
    }

    /**
     * 折叠
     */
    public void shrink() {
        if (!CalendarConfig.IS_WEEK) {
            CalendarConfig.IS_WEEK = true;
            CalendarConfig.iSScroll = false;
            setPagerHeight(CalendarConfig.MONTH_ROW * CalendarConfig.CELL_WIDTH, CalendarConfig.CELL_WIDTH);
        }
    }

    private void setPagerHeight(int startHeight, int endHeight) {
        final ViewGroup.LayoutParams params = this.getLayoutParams();
        ValueAnimator animator = ValueAnimator.ofInt(startHeight, endHeight);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                params.height = value;
                setLayoutParams(params);
            }
        });
        animator.setDuration(200);
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (CalendarConfig.IS_WEEK)
                    adapter.getCalendarView(currentPosition).setData(MonthWeekData.getInstance().getData(0));
                if (monthScrollListener != null) {
                    monthScrollListener.onMonthChange(CalendarConfig.SELECT_MONTH.year, CalendarConfig.SELECT_MONTH.month);
                }
            }
        });
    }

    private MonthScrollListener monthScrollListener;

    public void setMonthScrollListener(MonthScrollListener monthScrollListener) {
        this.monthScrollListener = monthScrollListener;
    }

    public interface MonthScrollListener {
        void onMonthChange(int year, int month);
    }

    private float beforeX;
    private float beforeY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                beforeX = ev.getX();
                beforeY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float motionXValue = ev.getX() - beforeX;
                float motionYValue = ev.getY() - beforeY;
                double degree = Math.atan2(Math.abs(motionYValue), Math.abs(motionXValue));
                if ((degree * 180 / Math.PI) > 60) {
                    if (motionYValue < 0) {
                        // 上滑
                        shrink();
                    } else {
                        // 下滑
                        expand();
                    }
                } else {
                    if (motionXValue < 0) {
                        // 左滑
                        if (CalendarConfig.IS_WEEK) {
                            return true;
                        }
                        DateData selectData = CalendarConfig.SELECT_MONTH;
                        DateData today = CalendarConfig.TODAY;
                        if (selectData.year == today.year && selectData.month == today.month)
                            return true;
                    } else {
                        // 右滑
                        if (CalendarConfig.IS_WEEK) {
                            return true;
                        }
                    }
                }
                beforeX = ev.getX();
                beforeY = ev.getY();
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (CalendarConfig.iSScroll) {
            int height = measureHeight();
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int measureHeight() {
        if (CalendarConfig.IS_WEEK)
            return CalendarConfig.CELL_WIDTH;
        else
            return CalendarConfig.CELL_WIDTH * CalendarConfig.MONTH_ROW;
    }

}
