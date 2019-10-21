package com.taohdao.library.common.widget.roundwidget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.taohdao.library.R;
import com.taohdao.library.common.widget.dialog.THDViewHelper;
import com.taohdao.library.utils.AlphaViewHelper;

/**
 * 见 {@link THDRoundButton} 与 {@link THDRoundButtonDrawable}
 */
public class THDRoundRelativeLayout extends RelativeLayout {

    private AlphaViewHelper mAlphaViewHelper;


    public THDRoundRelativeLayout(Context context) {
        super(context);
        init(context, null, 0);
    }

    public THDRoundRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, R.attr.THDButtonStyle);
    }

    public THDRoundRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        THDRoundButtonDrawable bg = THDRoundButtonDrawable.fromAttributeSet(context, attrs, defStyleAttr);
        THDViewHelper.setBackgroundKeepingPadding(this, bg);
    }

    private AlphaViewHelper getAlphaViewHelper() {
        if (mAlphaViewHelper == null) {
            mAlphaViewHelper = new AlphaViewHelper(this);
        }
        return mAlphaViewHelper;
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        getAlphaViewHelper().onPressedChanged(this, pressed);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        getAlphaViewHelper().onEnabledChanged(this, enabled);
    }

    /**
     * 设置是否要在 press 时改变透明度
     *
     * @param changeAlphaWhenPress 是否要在 press 时改变透明度
     */
    public void setChangeAlphaWhenPress(boolean changeAlphaWhenPress) {
        getAlphaViewHelper().setChangeAlphaWhenPress(changeAlphaWhenPress);
    }

    /**
     * 设置是否要在 disabled 时改变透明度
     *
     * @param changeAlphaWhenDisable 是否要在 disabled 时改变透明度
     */
    public void setChangeAlphaWhenDisable(boolean changeAlphaWhenDisable) {
        getAlphaViewHelper().setChangeAlphaWhenDisable(changeAlphaWhenDisable);
    }
}
