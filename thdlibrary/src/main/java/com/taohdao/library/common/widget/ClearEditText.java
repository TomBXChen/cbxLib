package com.taohdao.library.common.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.taohdao.library.R;


public class ClearEditText extends EditText implements
        OnFocusChangeListener, TextWatcher {
    /**
     * 提示字体大小
     * */
    private int hintsize;
    /**
     * 提示字体
     * */
    private String mhint;
    /**
     * 字体颜色
     * */
    private String mColor;
    /**
     * 删除按钮的引用
     */
    private Drawable mClearDrawable;

    /**
     * 可滑动的 即是srcollview下有该控件时，字数过多的时候可以避免与srcollview的冲突
     * */
    private boolean mSlideable;
    /**
     * 控件是否有焦点
     */
    private boolean hasFoucs;

    private int hintGravity;
    private int textGravity;
    private OnClearTextListener  mOnClearTextListener;

    public void setOnClearTextListener(OnClearTextListener ot){
        this.mOnClearTextListener = ot;
    }

    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        //这里构造方法也很重要，不加这个很多属性不能再XML里面定义
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EditText_other);
        hintsize = a.getInt(R.styleable.EditText_other_hintsize, 8);
        mhint = a.getString(R.styleable.EditText_other_hintcontent);
        mColor = a.getString(R.styleable.EditText_other_hintcolor);
        mSlideable = a.getBoolean(R.styleable.EditText_other_slideable, false);
        textGravity = getGravity();
        hintGravity = a.getInt(R.styleable.EditText_other_hintgravity, getGravity());
        if(hintGravity!=textGravity)setGravity(hintGravity);
        a.recycle();
        init();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if(!this.isFocusableInTouchMode())return false;

        return super.dispatchTouchEvent(event);
    }
    private void init() {
        //获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
//        	throw new NullPointerException("You can add drawableRight attribute in XML");
            mClearDrawable = getResources().getDrawable(R.drawable.ic_remove_nor);
        }

        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        //默认设置隐藏图标
        setClearIconVisible(false);
        //设置焦点改变的监听
        setOnFocusChangeListener(this);
        //设置输入框里面内容发生改变的监听
        addTextChangedListener(this);
    }


    /**
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件
     * 当我们按下的位置 在  EditText的宽度 - 图标到控件右边的间距 - 图标的宽度  和
     * EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //通知父控件不要干扰
               if(mSlideable)getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
                if (getCompoundDrawables()[2] != null) {
                    boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                            && (event.getX() < ((getWidth() - getPaddingRight())));

                    if (touchable) {
                        this.setText("");
                    }
                }
                break;
        }


        return super.onTouchEvent(event);
    }

    /**
     * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFoucs = hasFocus;
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
            setGravity(textGravity);
        } else {
            setClearIconVisible(false);
            setGravity(hintGravity);
        }
        if(mOnClearTextListener!=null){
            mOnClearTextListener.onFocusChange(v, hasFocus);
        }
    }


    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     * @param visible
     */
    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }


    /**
     * 当输入框里面内容发生变化的时候回调的方法
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int count,
                              int after) {
        if(hasFoucs){
            setClearIconVisible(s.length() > 0);
        }

        if(mOnClearTextListener!=null){
            mOnClearTextListener.onTextChanged(s, start, count, after);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(mOnClearTextListener!=null){
            mOnClearTextListener.afterTextChanged(s,getText().toString());
        }
    }


    /**
     * 设置晃动动画
     */
    public void setShakeAnimation(){
//    	this.setAnimation(shakeAnimation(10));
        this.startAnimation(shakeAnimation(5));
    }


    /**
     * 晃动动画
     * @param counts 1秒钟晃动多少下
     * @return
     */
    public static Animation shakeAnimation(int counts){
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(300);
        return translateAnimation;
    }
    /**
     * 加载XML之后回调该函数
     * */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setHint();
    }

    public void changeHint(String str){
        this.mhint = str;
        setHint();
    }
    private void setHint(){
        SpannableString hint = null;
        if (mhint != null && mhint.length() > 0)
            hint = new SpannableString(mhint);
        if (hint != null) {
            if (mColor != null && mColor.length() > 0) {
                ForegroundColorSpan backgroundColorSpan = new ForegroundColorSpan(
                        Color.parseColor(mColor));
                hint.setSpan(backgroundColorSpan, 0, hint.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (mhint != null && mhint.length() > 0) {
                AbsoluteSizeSpan ass = new AbsoluteSizeSpan(hintsize, true);
                hint.setSpan(ass, 0, hint.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            setHint(hint);
        }
    }

    public void setCustomHintSize(int hintsize) {
        this.hintsize = hintsize;
    }

    public void setCustomHint(String mhint) {
        this.mhint = mhint;
    }

    public void setCustomHinColor(String mColor) {
        this.mColor = mColor;
    }



    public interface OnClearTextListener{
        void onFocusChange(View v, boolean hasFocus);
        void onTextChanged(CharSequence s, int start, int count,
                           int after);
        void afterTextChanged(Editable s, String string);
    }
    public void cancelFocus(){
            ((InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(this.getWindowToken(), 0);
    }
}
