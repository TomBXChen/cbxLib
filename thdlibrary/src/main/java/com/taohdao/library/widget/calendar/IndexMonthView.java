package com.taohdao.library.widget.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.taohdao.library.widget.calendarview.Calendar;
import com.taohdao.library.widget.calendarview.MonthView;


/**
 * 下标标记的日历控件
 * Created by huanghaibin on 2017/11/15.
 */

public class IndexMonthView extends MonthView {
    private Paint mSchemeBasicPaint = new Paint();
    private int mPadding;
    private int mH, mW;

    public IndexMonthView(Context context) {
        super(context);

        mSchemeBasicPaint.setAntiAlias(true);
        mSchemeBasicPaint.setStyle(Paint.Style.FILL);
        mSchemeBasicPaint.setTextAlign(Paint.Align.CENTER);
        mSchemeBasicPaint.setColor(0xff333333);
        mSchemeBasicPaint.setFakeBoldText(true);
        mPadding = dipToPx(getContext(), 4);
        mH = dipToPx(getContext(), 2);
        mW = dipToPx(getContext(), 8);
    }


    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        mSelectedPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(x + mPadding, y + mPadding, x + mItemWidth - mPadding, y + mItemHeight - mPadding, mSelectedPaint);
        return true;
    }

    /**
     * onDrawSelected
     * @param canvas   canvas
     * @param calendar 日历calendar
     * @param x        日历Card x起点坐标
     * @param y        日历Card y起点坐标
     */
    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
        mSchemeBasicPaint.setColor(calendar.getSchemeColor());
        float baselineY = mTextBaseLine + y - dipToPx(getContext(),1);
        canvas.drawRect(x + mItemWidth / 2 - mW / 2,
                baselineY + mH*4 - mPadding,
                x + mItemWidth / 2 + mW / 2,
                baselineY + mH*6 - mPadding, mSchemeBasicPaint);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        int cx = x + mItemWidth / 2;
        int top = y - mItemHeight / 6;
        float baselineY = mTextBaseLine + y - dipToPx(getContext(),1);
        if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);

        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
        }
    }

    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
