package com.taohdao.library.utils;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by admin on 2018/3/19.
 */

public class AlphaViewHelper {
    private View mTarget;

    /**
     * 设置是否要在 press 时改变透明度
     */
    private boolean mChangeAlphaWhenPress = true;

    /**
     * 设置是否要在 disabled 时改变透明度
     */
    private boolean mChangeAlphaWhenDisable = true;

    private float mNormalAlpha = 1f;
    private float mPressedAlpha = 0.5f;
    private float mDisabledAlpha = 0.5f;

    public AlphaViewHelper(@NonNull View target) {
        mTarget = target;
    }

    public void onPressedChanged(View target, boolean pressed) {
        if (mTarget.isEnabled()) {
            mTarget.setAlpha(mChangeAlphaWhenPress && pressed && target.isClickable()? mPressedAlpha : mNormalAlpha);
        } else {
            if (mChangeAlphaWhenDisable) {
                target.setAlpha(mDisabledAlpha);
            }
        }
    }

    public void onEnabledChanged(View target, boolean enabled) {
        float alphaForIsEnable;
        if (mChangeAlphaWhenDisable) {
            alphaForIsEnable = enabled ? mNormalAlpha : mDisabledAlpha;
        } else {
            alphaForIsEnable = mNormalAlpha;
        }
        target.setAlpha(alphaForIsEnable);
    }

    /**
     * 设置是否要在 press 时改变透明度
     *
     * @param changeAlphaWhenPress 是否要在 press 时改变透明度
     */
    public void setChangeAlphaWhenPress(boolean changeAlphaWhenPress) {
        mChangeAlphaWhenPress = changeAlphaWhenPress;
    }

    /**
     * 设置是否要在 disabled 时改变透明度
     *
     * @param changeAlphaWhenDisable 是否要在 disabled 时改变透明度
     */
    public void setChangeAlphaWhenDisable(boolean changeAlphaWhenDisable) {
        mChangeAlphaWhenDisable = changeAlphaWhenDisable;
        onEnabledChanged(mTarget, mTarget.isEnabled());
    }
}
