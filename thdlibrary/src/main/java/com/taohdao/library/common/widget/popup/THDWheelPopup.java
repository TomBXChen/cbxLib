package com.taohdao.library.common.widget.popup;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.jzxiang.pickerview.TimeWheel;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.data.WheelCalendar;
import com.taohdao.library.R;

import java.util.Calendar;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by admin on 2018/9/19.
 */

public class THDWheelPopup extends THDPopup implements View.OnClickListener {

    PopPickerConfig mPickerConfig;
    private TimeWheel mTimeWheel;
    private long mCurrentMillSeconds;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;


    private  int mInitialHourOfDay;
    private  int mInitialMinute;
    private  boolean mIs24HourView;

    private static THDWheelPopup newIntance(PopPickerConfig pickerConfig, Context context) {
        THDWheelPopup thdWheelPopup = new THDWheelPopup(context);
        thdWheelPopup.initialize(pickerConfig);
        return thdWheelPopup;
    }

    private void initialize(PopPickerConfig pickerConfig) {
        this.mPickerConfig = pickerConfig;
        initView();
    }


    public THDWheelPopup(Context context) {
        super(context);
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.timepicker_layout, null);
        final ViewGroup.LayoutParams layoutParams = generateLayoutParam(SizeUtils.dp2px(350),WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        TextView cancel = (TextView) view.findViewById(R.id.tv_cancel);
        cancel.setOnClickListener(this);
        TextView sure = (TextView) view.findViewById(R.id.tv_sure);
        sure.setOnClickListener(this);
        TextView title = (TextView) view.findViewById(R.id.tv_title);
        View toolbar = view.findViewById(R.id.toolbar);

        title.setText(mPickerConfig.mTitleString);
        cancel.setText(mPickerConfig.mCancelString);
        sure.setText(mPickerConfig.mSureString);
        toolbar.setBackgroundColor(mPickerConfig.mThemeColor);

        mTimeWheel = new TimeWheel(view, mPickerConfig);

        setContentView(view);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == com.jzxiang.pickerview.R.id.tv_cancel) {
            dismiss();
        } else if (i == com.jzxiang.pickerview.R.id.tv_sure) {
            sureClicked();
        }
    }

    void sureClicked() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();

        calendar.set(Calendar.YEAR, mTimeWheel.getCurrentYear());
        calendar.set(Calendar.MONTH, mTimeWheel.getCurrentMonth() - 1);
        calendar.set(Calendar.DAY_OF_MONTH, mTimeWheel.getCurrentDay());
        calendar.set(Calendar.HOUR_OF_DAY, mTimeWheel.getCurrentHour());
        calendar.set(Calendar.MINUTE, mTimeWheel.getCurrentMinute());

        mCurrentMillSeconds = calendar.getTimeInMillis();
        if (mPickerConfig.mCallBack != null) {
            mPickerConfig.mCallBack.onDateSet(this, mCurrentMillSeconds);
        }
        dismiss();
    }

    public interface OnDateSetListener {

        void onDateSet(THDPopup timePickerView, long millseconds);
    }

    public static class Builder {
        PopPickerConfig mPickerConfig;

        public Builder() {
            mPickerConfig = new PopPickerConfig();
        }

        public Builder setType(Type type) {
            mPickerConfig.mType = type;
            return this;
        }

        public Builder setThemeColor(int color) {
            mPickerConfig.mThemeColor = color;
            return this;
        }

        public Builder setCancelStringId(String left) {
            mPickerConfig.mCancelString = left;
            return this;
        }

        public Builder setSureStringId(String right) {
            mPickerConfig.mSureString = right;
            return this;
        }

        public Builder setTitleStringId(String title) {
            mPickerConfig.mTitleString = title;
            return this;
        }

        public Builder setToolBarTextColor(int color) {
            mPickerConfig.mToolBarTVColor = color;
            return this;
        }

        public Builder setWheelItemTextNormalColor(int color) {
            mPickerConfig.mWheelTVNormalColor = color;
            return this;
        }

        public Builder setWheelItemTextSelectorColor(int color) {
            mPickerConfig.mWheelTVSelectorColor = color;
            return this;
        }

        public Builder setWheelItemTextSize(int size) {
            mPickerConfig.mWheelTVSize = size;
            return this;
        }

        public Builder setCyclic(boolean cyclic) {
            mPickerConfig.cyclic = cyclic;
            return this;
        }

        public Builder setMinMillseconds(long millseconds) {
            mPickerConfig.mMinCalendar = new WheelCalendar(millseconds);
            return this;
        }

        public Builder setMaxMillseconds(long millseconds) {
            mPickerConfig.mMaxCalendar = new WheelCalendar(millseconds);
            return this;
        }

        public Builder setCurrentMillseconds(long millseconds) {
            mPickerConfig.mCurrentCalendar = new WheelCalendar(millseconds);
            return this;
        }

        public Builder setYearText(String year){
            mPickerConfig.mYear = year;
            return this;
        }

        public Builder setMonthText(String month){
            mPickerConfig.mMonth = month;
            return this;
        }

        public Builder setDayText(String day){
            mPickerConfig.mDay = day;
            return this;
        }

        public Builder setHourText(String hour){
            mPickerConfig.mHour = hour;
            return this;
        }

        public Builder setMinuteText(String minute){
            mPickerConfig.mMinute = minute;
            return this;
        }

        public Builder setCallBack(OnDateSetListener listener) {
            mPickerConfig.mCallBack = listener;
            return this;
        }

        public THDWheelPopup build(Context context) {
            return newIntance(mPickerConfig,context);
        }

    }
}
