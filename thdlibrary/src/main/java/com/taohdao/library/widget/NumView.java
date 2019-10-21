package com.taohdao.library.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.text.Selection;
import android.text.Spannable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taohdao.library.R;
import com.taohdao.library.common.widget.alpha.AlphaTextView;

import java.text.DecimalFormat;

/**
 * Created by admin on 2016/8/15.
 * * [xml]
 * <com.taohdao.library.widget.NumView
 * android:id="@+id/nv"
 * android:layout_width="wrap_content"
 * android:layout_height="wrap_content" />
 * <p/>
 * [activity]
 * NumView nv = (NumView) findViewById(R.id.nv);
 * nv.init(0, 30, 0, true);
 */
public class NumView extends LinearLayout {

    private AlphaTextView mIvAdd;
    private AlphaTextView mIvReduce;
    private EditText mTvNum;
    // 不要设置成静态的，每一个NumView，都应该有一个最大值和最小值；
    private int mMaxNum;
    private int mMinNum;

    private OnNumChangeListener mChangeListener;
    private OnNumInterceptor mOnNumInterceptor;
    private OnNumBeforeChangeListener mOnNumBeforeChangeListener;

    // 是否循环
    private boolean mCycleable;

    public NumView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public NumView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumView(Context context) {
        super(context, null);
    }

    public int getMaxNum() {
        return mMaxNum;
    }

    public void setMaxNum(int maxNum) {
        mMaxNum = maxNum;
    }

    public int getMinNum() {
        return mMinNum;
    }

    public void setMinNum(int minNum) {
        mMinNum = minNum;
    }

    public boolean isCycleable() {
        return mCycleable;
    }

    public void setCycleable(boolean cycleable) {
        mCycleable = cycleable;
    }

    public void setOnChangeListener(OnNumChangeListener changeListener) {
        mChangeListener = changeListener;
    }

    public void setOnNumBeforeChangeListener(OnNumBeforeChangeListener mOnNumBeforeChangeListener) {
        this.mOnNumBeforeChangeListener = mOnNumBeforeChangeListener;
    }

    public void setOnNumInterceptor(OnNumInterceptor mOnNumInterceptor) {
        this.mOnNumInterceptor = mOnNumInterceptor;
    }

    private void initData() {
        // initial
        init(0, 1000, 10, false);
    }

    /**
     * @param minNum     the min number in the field;
     * @param maxNum     the max number in the field;
     * @param defaultNum the default number to show;
     * @param cycleable  is or not cycle;
     */
    public void init(int minNum, int maxNum, int defaultNum, boolean cycleable) {
        setMinNum(minNum);
        setMaxNum(maxNum);
        setCycleable(cycleable);

        setNum(defaultNum);
    }

    public void change(int minNum, int maxNum, int defaultNum, boolean cycleable) {
        setMinNum(minNum);
        setMaxNum(maxNum);
        setCycleable(cycleable);

        setNumber(defaultNum);
    }

    private void initView(Context context) {
        setOrientation(HORIZONTAL);
        setBackgroundResource(R.drawable.bg_solid_gray);
        initData();
        View view = View.inflate(context, R.layout.view_num, this);

        mIvAdd = (AlphaTextView) view.findViewById(R.id.iv_num_add);
        mIvReduce = (AlphaTextView) view.findViewById(R.id.iv_num_reduce);
        mTvNum = (EditText) view.findViewById(R.id.tv_num);

        mIvAdd.setOnClickListener(listener);
        mIvReduce.setOnClickListener(listener);
        autoChangeWithLongClick(mIvAdd, mIvReduce);
    }


    private void autoChangeWithLongClick(TextView... ivs) {
        for (TextView iv : ivs) {
//            iv.setOnLongClickListener(mLongClickListener);
//            iv.setOnTouchListener(mTouchListener);
        }
    }

    public void setFocusable(boolean isFocusable) {
        if (isFocusable) {
            mTvNum.setFocusable(true);
            mTvNum.setFocusableInTouchMode(true);
            mTvNum.requestFocus();
        } else {
            mTvNum.setFocusableInTouchMode(false);
            mTvNum.setFocusable(false);
        }
    }

    public void setNumColor(int color) {
        mTvNum.setTextColor(color);
    }


    // 自动变化的 Task；
    private Handler mHandler = new Handler();
    private static final int DELAY_TIME = 160;
    private int mUnit = 1;
    private Runnable mTask = new Runnable() {
        @Override
        public void run() {
            setNum(getNum() + mUnit);
            mHandler.postDelayed(mTask, DELAY_TIME);
        }
    };

    // 长按开启自动变化；
    private boolean mLongClicked;
    OnLongClickListener mLongClickListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            mLongClicked = true;
            mHandler.postDelayed(mTask, DELAY_TIME);
            return mLongClicked;
        }

    };

    // 实现：非长按状态下拖动一定距离开启自动变化；
    private float mYLast;
    private boolean mChanging;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        getParent().requestDisallowInterceptTouchEvent(true);
//        float y = ev.getY();
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mYLast = y;
//                break;
//            case MotionEvent.ACTION_UP:
//                mChanging = false;
//                break;
//            case MotionEvent.ACTION_MOVE:
//
//                float delta = Math.abs(y - mYLast);
//                if (delta > 100 && !mChanging && !mLongClicked) {
//                    mHandler.postDelayed(mTask, DELAY_TIME);
//                    mChanging = true;
//                    return true;
//                }
//                break;
//        }
        return super.dispatchTouchEvent(ev);
    }


    @SuppressLint("ClickableViewAccessibility")
    OnTouchListener mTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.setAlpha(0.2f);
                    v.setBackgroundColor(0x88ffffff);
                    if (v.getId() == R.id.iv_num_reduce) {
                        mUnit = -1 * Math.abs(mUnit);
                    } else if (v.getId() == R.id.iv_num_add) {
                        mUnit = Math.abs(mUnit);
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    // alpha animation
                    v.setBackgroundResource(0);
                    v.animate().alpha(1f).setDuration(200).start();

                    // recover
                    mLongClicked = false;
                    mHandler.removeCallbacks(mTask);
                    break;
            }
            return false;
        }
    };

    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int num = getNum();

            int i = v.getId();
            if (i == R.id.iv_num_add) {
                num = num + 1;

            } else if (i == R.id.iv_num_reduce) {
                num = num - 1;
            }

            setNum(num);
        }
    };

    public void setNum(int num) {
        // default pattern:00
        setNum(num, "0");
    }

    public void setNum(int num, String pattern) {
        if (num > mMaxNum) {
            if (mCycleable) {
                num = mMinNum;
            } else {
                num = num - 1;
            }
        }

        if (num < mMinNum) {
            if (mCycleable) {
                num = mMaxNum;
            } else {
                num = num + 1;
            }
        }

        String str = getFormatNumWithPattern(num, pattern);

        if (mTvNum != null && str != null) {
            if (mOnNumBeforeChangeListener != null) {
                mOnNumBeforeChangeListener.numBeforeChange(str, num);
                return;
            }
            if (mOnNumInterceptor != null) {
                if (mOnNumInterceptor.numInterceptor(str, num)) {//是否拦截
                    return;
                }
            }
            if (mChangeListener != null) {
                mChangeListener.numChange(str, num);
            }
            mTvNum.setText(str);
            afterCursor(mTvNum);
        }
    }

    public void setNumber(int num) {
        if (num > mMaxNum) {
            num = mMaxNum;
        }
        if (num < mMinNum) {
            num = mMinNum;
        }
        String str = getFormatNumWithPattern(num, "0");

        if (mTvNum != null && str != null) {
            if (mChangeListener != null) {
                mChangeListener.numChange(str, num);
            }
            mTvNum.setText(str);
            afterCursor(mTvNum);
        }
    }

    public int getNum() {
        try {
            return Integer.valueOf(mTvNum.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public EditText getEditText() {
        return mTvNum;
    }

    public String getNumText() {
        return mTvNum.getText().toString();
    }


    public interface OnNumChangeListener {
        void numChange(String numStr, int num);
    }

    public interface OnNumInterceptor {
        boolean numInterceptor(String numStr, int num);
    }

    public interface OnNumBeforeChangeListener {
        void numBeforeChange(String numStr, int num);
    }

    public static String getFormatNumWithPattern(Object object, String pattern) {
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(object);
    }

    public void afterCursor(TextView view) {
        CharSequence charSequence = view.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
    }
}
