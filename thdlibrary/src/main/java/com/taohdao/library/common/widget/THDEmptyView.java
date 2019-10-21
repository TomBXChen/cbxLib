package com.taohdao.library.common.widget;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.taohdao.library.R;
import com.taohdao.library.common.widget.dialog.THDLoadingView;

/**
 * Created by admin on 2018/6/27.
 */

public class THDEmptyView extends FrameLayout {


    private THDLoadingView mLoadingView;
    private TextView mTitleTextView;
    private TextView mDetailTextView;
    private ImageView mIcon;
    protected Button mButton;

    public THDEmptyView(@NonNull Context context) {
        this(context, null);
    }

    public THDEmptyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public THDEmptyView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.thd_empty_view, this, true);
        mLoadingView = (THDLoadingView) findViewById(R.id.empty_view_loading);
        mTitleTextView = (TextView) findViewById(R.id.empty_view_title);
        mIcon = (ImageView) findViewById(R.id.empty_view_icon);
        mDetailTextView = (TextView) findViewById(R.id.empty_view_detail);
        mButton = (Button) findViewById(R.id.empty_view_button);
    }

    /**
     * 显示emptyView
     *
     * @param loading               是否要显示loading
     * @param titleText             标题的文字，不需要则传null
     * @param detailText            详情文字，不需要则传null
     * @param buttonText            按钮的文字，不需要按钮则传null
     * @param onButtonClickListener 按钮的onClick监听，不需要则传null
     */
    public void show(boolean loading, String titleText, String detailText, String buttonText, OnClickListener onButtonClickListener) {
        setLoadingShowing(loading);
        setTitleText(titleText);
        setDetailText(detailText);
        setButton(buttonText, onButtonClickListener);
        show();
    }

    public void show(boolean loading){
        setLoadingShowing(loading);
        setTitleText(null);
        setIconRes(0);
        setDetailText(null);
        setButton(null, null);
        show();
    }

    public void showEmptyIcon(String titleText, @DrawableRes int iconRes) {
        setLoadingShowing(false);
        setTitleText(titleText);
        setIconRes(iconRes);
        setDetailText(null);
        setButton(null, null);
        show();
    }

    /**
     * 显示emptyView，不建议直接使用，建议调用带参数的show()方法，方便控制所有子View的显示/隐藏
     */
    public void show() {
        setVisibility(VISIBLE);
    }

    /**
     * 隐藏emptyView
     */
    public void hide() {
        setVisibility(GONE);
        setLoadingShowing(false);
        setTitleText(null);
        setIconRes(0);
        setDetailText(null);
        setButton(null, null);
    }

    public boolean isShowing() {
        return getVisibility() == VISIBLE;
    }

    public boolean isLoading() {
        return mLoadingView.getVisibility() == VISIBLE;
    }

    public void setLoadingShowing(boolean show) {
        mLoadingView.setVisibility(show ? VISIBLE : GONE);
    }

    public void setTitleText(String text) {
        mTitleTextView.setText(text);
        mTitleTextView.setVisibility(text != null ? VISIBLE : GONE);
    }

    public void setIconRes(@DrawableRes int iconRes) {
        if (iconRes > 0) {
            mIcon.setImageResource(iconRes);
            mIcon.setVisibility(View.VISIBLE);
        } else {
            mIcon.setVisibility(View.GONE);
        }
    }

    public void setDetailText(String text) {
        mDetailTextView.setText(text);
        mDetailTextView.setVisibility(text != null ? VISIBLE : GONE);
    }

    public void setTitleColor(int color) {
        mTitleTextView.setTextColor(color);
    }

    public void setDetailColor(int color) {
        mDetailTextView.setTextColor(color);
    }

    public void setButton(String text, OnClickListener onClickListener) {
        mButton.setText(text);
        mButton.setVisibility(text != null ? VISIBLE : GONE);
        mButton.setOnClickListener(onClickListener);
    }
}
