package com.taohdao.library.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.view.Gravity;

import com.taohdao.library.R;


/**
 * Created by taohuadao on 2016/6/1.
 */
public class LabelView extends AppCompatTextView {

    private CharSequence mLeftText, mTopText, mRightText, mBottomText;
    private int mLeftTextAppearance, mTopTextAppearance, mRightTextAppearance,
            mBottomTextAppearance;
    private CharSequence mText;
    private boolean isUnderline = false;
    private boolean isDeleteline = false;

    public LabelView(Context context) {
        this(context, null);
    }


    public LabelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public LabelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.LabelView);
        mLeftText = a.getText(R.styleable.LabelView_leftText);
        mTopText = a.getText(R.styleable.LabelView_topText);
        mRightText = a.getText(R.styleable.LabelView_rightText);
        mBottomText = a.getText(R.styleable.LabelView_bottomText);

        mLeftTextAppearance = a.getResourceId(
                R.styleable.LabelView_leftTextAppearance, 0);
        mTopTextAppearance = a.getResourceId(
                R.styleable.LabelView_topTextAppearance, 0);
        mRightTextAppearance = a.getResourceId(
                R.styleable.LabelView_rightTextAppearance, 0);
        mBottomTextAppearance = a.getResourceId(
                R.styleable.LabelView_bottomTextAppearance, 0);
        int gravity = a.getInt(R.styleable.LabelView_android_gravity,
                Gravity.CENTER);
        isUnderline = a.getBoolean(R.styleable.LabelView_drawUnderline,false);
        isDeleteline = a.getBoolean(R.styleable.LabelView_drawDeleteline,false);
        a.recycle();

        setGravity(gravity);
        setText(super.getText());
    }
    public void setTopText(CharSequence topSequence){
        this.mTopText = topSequence;
    }
    public void setBottomText(CharSequence bottomSequence){
        this.mBottomText = bottomSequence;
    }
    public void setLeftText(CharSequence mLeftText){
        this.mLeftText = mLeftText;
    }

    public void setRightText(CharSequence mRightText){
        this.mRightText = mRightText;
    }
    public boolean isUnderline(){
        return isUnderline;
    }

    public void setUnderline(boolean isUnderline){
        this.isUnderline = isUnderline;
    }

    @Override public void setText(CharSequence mainText, BufferType type) {
            super.setText(mainText, type);
        mText = mainText;
        CharSequence text =  buildMainText(mainText);
        if (notNullOrEmpty(mLeftText)) {
            text = buildTextLeft(mLeftText.toString(), text,
                    mLeftTextAppearance);
        }
        if (notNullOrEmpty(mRightText)) {
            text = buildTextRight(text, mRightText.toString(),
                    mRightTextAppearance);
        }
        if (notNullOrEmpty(mTopText)) {
            text = new SpannableStringBuilder("\n").append(text);
            text = buildTextLeft(mTopText.toString(), text, mTopTextAppearance);
        }
        if (notNullOrEmpty(mBottomText)) {
            text = new SpannableStringBuilder(text).append("\n");
            text = buildTextRight(text, mBottomText.toString(),
                    mBottomTextAppearance);
        }
        if (notNullOrEmpty(text)) {
                super.setText(text, type);
        }
    }


    private CharSequence buildTextLeft(CharSequence head, CharSequence foot, int style) {
        SpannableString leftText = format(getContext(), head, style);
        SpannableStringBuilder builder = new SpannableStringBuilder(
                leftText).append(foot);
        return builder.subSequence(0, builder.length());
    }

    private CharSequence buildMainText(CharSequence mainText){
        SpannableString msp = new SpannableString(mainText);
        if(isUnderline) {
            msp.setSpan(new UnderlineSpan(), 0, msp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if(isDeleteline) {
            msp.setSpan(new StrikethroughSpan(), 0, msp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        SpannableStringBuilder builder = new SpannableStringBuilder(
                msp);
        return builder.subSequence(0, builder.length());
    }

    //todo
    private CharSequence buildTextRight(CharSequence head, CharSequence foot, int style) {
        SpannableString rightText = format(getContext(), foot, style);
        SpannableStringBuilder builder = new SpannableStringBuilder(
                head).append(rightText);
        return builder.subSequence(0, builder.length());
    }


    public SpannableString format(Context context, CharSequence text, int style) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new TextAppearanceSpan(context, style), 0,
                text.length(), 0);
        return spannableString;
    }


    private boolean notNullOrEmpty(CharSequence leftText) {
        return leftText != null && leftText.length() > 0;
    }


    @Override public CharSequence getText() {
        return mText;
    }
}
