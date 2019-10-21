package com.taohdao.library.common.widget.roundwidget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.taohdao.library.R;
import com.taohdao.library.common.widget.dialog.THDViewHelper;

/**
 * 见 {@link THDRoundButton} 与 {@link THDRoundButtonDrawable}
 */
public class THDRoundLinearLayout extends LinearLayout {

    public THDRoundLinearLayout(Context context) {
        super(context);
        init(context, null, 0);
    }

    public THDRoundLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, R.attr.THDButtonStyle);
    }

    public THDRoundLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        THDRoundButtonDrawable bg = THDRoundButtonDrawable.fromAttributeSet(context, attrs, 0);
        THDViewHelper.setBackgroundKeepingPadding(this, bg);
    }
}
