package com.taohdao.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.ImageView;

public class CheckableImageView extends ImageView implements Checkable {

    private boolean mChecked;

    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};

    private OnCheckedChangeListener mOnCheckedChangeListener;

    public CheckableImageView(Context context) {
        this(context, null);
    }

    public CheckableImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int[] onCreateDrawableState(final int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void setChecked(final boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            refreshDrawableState();
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this,mChecked);
            }
        }

    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener mOnCheckedChangeListener) {
        this.mOnCheckedChangeListener = mOnCheckedChangeListener;
    }

    public static interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param buttonView The compound button view whose state has changed.
         * @param isChecked  The new checked state of buttonView.
         */
        void onCheckedChanged(CheckableImageView buttonView, boolean isChecked);
    }
}
