package com.taohdao.library.common.widget.roundwidget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.taohdao.library.R;
import com.taohdao.library.common.widget.dialog.THDViewHelper;
import com.taohdao.library.utils.AlphaViewHelper;


/**
 * 使按钮能方便地指定圆角、边框颜色、边框粗细、背景色
 * <p>
 * 注意: 因为该控件的圆角采用 View 的 background 实现, 所以与原生的 <code>android:background</code> 有冲突。
 * <ul>
 * <li>如果在 xml 中用 <code>android:background</code> 指定 background, 该 background 不会生效。</li>
 * <li>如果在该 View 构造完后用 {@link #setBackgroundResource(int)} 等方法设置背景, 该背景将覆盖圆角效果。</li>
 * </ul>
 * </p>
 * <p>
 * </p>
 * <p>
 * 如需在 Java 中指定以上属性, 需要通过 {@link #getBackground()} 获取 {@link THDRoundButtonDrawable} 对象,
 * 然后使用 {@link THDRoundButtonDrawable} 提供的方法进行设置。
 * </p>
 * <p>
 * @see THDRoundButtonDrawable
 * </p>
 */
public class THDRoundButton extends Button {

    private AlphaViewHelper mAlphaViewHelper;

    public THDRoundButton(Context context) {
        super(context);
        init(context, null, 0);
    }

    public THDRoundButton(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.THDButtonStyle);
        init(context, attrs, R.attr.THDButtonStyle);
    }

    public THDRoundButton(Context context, AttributeSet attrs, int defStyleAttr) {
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
