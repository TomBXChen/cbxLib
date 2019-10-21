package com.taohdao.library.common.widget.dialog;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.ViewStub;
import android.widget.TextView;

import com.taohdao.library.R;
import com.taohdao.library.common.widget.alpha.AlphaLinearLayout;


/**
 * QMUIBottomSheet 的ItemView
 * @author zander
 * @date 2017-12-05
 */
public class THDBottomSheetItemView extends AlphaLinearLayout {

    private AppCompatImageView mAppCompatImageView;
    private ViewStub mSubScript;
    private TextView mTextView;


    public THDBottomSheetItemView(Context context) {
        super(context);
    }

    public THDBottomSheetItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public THDBottomSheetItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mAppCompatImageView = (AppCompatImageView) findViewById(R.id.grid_item_image);
        mSubScript = (ViewStub) findViewById(R.id.grid_item_subscript);
        mTextView = (TextView) findViewById(R.id.grid_item_title);
    }

    public AppCompatImageView getAppCompatImageView() {
        return mAppCompatImageView;
    }

    public TextView getTextView() {
        return mTextView;
    }

    public ViewStub getSubScript() {
        return mSubScript;
    }
}
