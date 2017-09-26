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
    private boolean isFirstLoad = true;

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

        initCalendarConfig();

        adapter = new CalendarViewAdapter(activity);
        this.setAdapter(adapter);
        currentPosition = CalendarConfig.COUNT / 2;
        setCurrentItem(currentPosition, false);

        setCalendarData(currentPosition);

        this.addOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(final int position) {
                if (position > currentPosition) {
                    diffPosition = 1;
                } else {
                    diffPosition = -1;
                }
                currentPosition = position;
                CalendarConfig.iSScroll = true;
                setData(position);
                if (monthListener != null) {
                    monthListener.scrollMonth(CalendarConfig.SELECT_MONTH.year, CalendarConfig.SELECT_MONTH.month);
                }
            }
        });
    }

    private void initCalendarConfig() {
        Resources resources = activity.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        CalendarConfig.CELL_WIDTH = width / 7;
        CalendarConfig.CELL_HEIGHT = width / 7;
        CalendarConfig.TODAY = new DateData(
                Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        CalendarConfig.SELECT_DAY = CalendarConfig.TODAY;
        CalendarConfig.SELECT_MONTH = CalendarConfig.TODAY;
        CalendarConfig.IS_WEEK = true;
        CalendarConfig.iSScroll = false;
    }

    private void setData(int position) {
        dateDataList = MonthWeekData.getInstance().getData(diffPosition);
        adapter.getCalendarView(position).setData(dateDataList);
        adapter.getCalendarView(position).setMonthListener(monthListener);
    }

    private void setCalendarData(final int position) {
        if (adapter.getCalendarView(position) != null) {
            setData(position);
            this.requestLayout();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setCalendarData(position);
                }
            }, 50);
        }
    }

    /**
     * 展开
     */
    public void expand() {
        if (CalendarConfig.IS_WEEK) {
            isFirstLoad = false;
            CalendarConfig.IS_WEEK = false;
            CalendarConfig.iSScroll = false;
            List<DateData> list = MonthWeekData.getInstance().getData(0);
            setPagerHeight(CalendarConfig.CELL_HEIGHT, CalendarConfig.MONTH_ROW * CalendarConfig.CELL_HEIGHT);
            adapter.getCalendarView(currentPosition).setData(list);
        }
    }

    /**
     * 折叠
     */
    public void shrink() {
        if (!CalendarConfig.IS_WEEK) {
            isFirstLoad = false;
            CalendarConfig.IS_WEEK = true;
            CalendarConfig.iSScroll = false;
            setPagerHeight(CalendarConfig.MONTH_ROW * CalendarConfig.CELL_HEIGHT, CalendarConfig.CELL_HEIGHT);
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
                if (monthListener != null) {
                    monthListener.scrollMonth(CalendarConfig.SELECT_MONTH.year, CalendarConfig.SELECT_MONTH.month);
                }
            }
        });
    }

    private MonthListener monthListener;

    public void setMonthListener(MonthListener monthListener) {
        this.monthListener = monthListener;
        if (monthListener != null) {
            monthListener.scrollMonth(CalendarConfig.SELECT_MONTH.year, CalendarConfig.SELECT_MONTH.month);
            monthListener.clickDay(CalendarConfig.SELECT_DAY.year, CalendarConfig.SELECT_DAY.month, CalendarConfig.SELECT_DAY.day);
        }
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
                            List<DateData> dateDataList = CalendarConfig.SELECT_WEEK;
                            if (dateDataList != null) {
                                DateData today = CalendarConfig.TODAY;
                                boolean hasDay = false;
                                for (DateData dateData : dateDataList) {
                                    if (dateData.year == today.year && dateData.month == today.month && dateData.day == today.day) {
                                        hasDay = true;
                                        break;
                                    }
                                }
                                if (hasDay)
                                    return true;
                            }
                        } else {
                            DateData selectData = CalendarConfig.SELECT_MONTH;
                            DateData today = CalendarConfig.TODAY;
                            if (selectData.year == today.year && selectData.month == today.month)
                                return true;
                        }
                    } else {
                        // 右滑
                        if (CalendarConfig.IS_WEEK) {

                        } else {

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
        if (CalendarConfig.iSScroll || isFirstLoad) {
            int height = measureHeight();
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int measureHeight() {
        if (CalendarConfig.IS_WEEK) {
            return CalendarConfig.CELL_HEIGHT;
        } else
            return CalendarConfig.CELL_HEIGHT * CalendarConfig.MONTH_ROW;
    }

}
