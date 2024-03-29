package com.taohdao.library.common.widget.roundwidget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.taohdao.library.R;
import com.taohdao.library.utils.CommonUtils;


/**
 * 可以方便地生成圆角矩形/圆形 {@link android.graphics.drawable.Drawable}。
 * <p>
 * <ul>
 * <li>使用 {@link #setBgData(ColorStateList)} 设置背景色。</li>
 * <li>使用 {@link #setStrokeData(int, ColorStateList)} 设置描边大小、描边颜色。</li>
 * <li>使用 {@link #setIsRadiusAdjustBounds(boolean)} 设置圆角大小是否自动适应为 {@link android.view.View} 的高度的一半, 默认为 true。</li>
 * </ul>
 */
public class THDRoundButtonDrawable extends GradientDrawable {

    /**
     * 圆角大小是否自适应为 View 的高度的一般
     */
    private boolean mRadiusAdjustBounds = true;
    private ColorStateList mFillColors;
    private int mStrokeWidth = 0;
    private ColorStateList mStrokeColors;

    /**
     * 设置按钮的背景色(只支持纯色,不支持 Bitmap 或 Drawable)
     */
    public void setBgData(@Nullable ColorStateList colors) {
        if (hasNativeStateListAPI()) {
            super.setColor(colors);
        } else {
            mFillColors = colors;
            final int currentColor;
            if (colors == null) {
                currentColor = Color.TRANSPARENT;
            } else {
                currentColor = colors.getColorForState(getState(), 0);
            }
            setColor(currentColor);
        }
    }




    /**
     * 设置按钮的描边粗细和颜色
     */
    public void setStrokeData(int width, @Nullable ColorStateList colors) {
        if (hasNativeStateListAPI()) {
            super.setStroke(width, colors);
        } else {
            mStrokeWidth = width;
            mStrokeColors = colors;
            final int currentColor;
            if (colors == null) {
                currentColor = Color.TRANSPARENT;
            } else {
                currentColor = colors.getColorForState(getState(), 0);
            }
            setStroke(width, currentColor);
        }
    }

    private boolean hasNativeStateListAPI() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * 设置圆角大小是否自动适应为 View 的高度的一半
     */
    public void setIsRadiusAdjustBounds(boolean isRadiusAdjustBounds) {
        mRadiusAdjustBounds = isRadiusAdjustBounds;
    }

    @Override
    protected boolean onStateChange(int[] stateSet) {
        boolean superRet = super.onStateChange(stateSet);
        if (mFillColors != null) {
            int color = mFillColors.getColorForState(stateSet, 0);
            setColor(color);
            superRet = true;
        }
        if (mStrokeColors != null) {
            int color = mStrokeColors.getColorForState(stateSet, 0);
            setStroke(mStrokeWidth, color);
            superRet = true;
        }
        return superRet;
    }

    @Override
    public boolean isStateful() {
        return (mFillColors != null && mFillColors.isStateful())
                || (mStrokeColors != null && mStrokeColors.isStateful())
                || super.isStateful();
    }

    @Override
    protected void onBoundsChange(Rect r) {
        super.onBoundsChange(r);
        if (mRadiusAdjustBounds) {
            // 修改圆角为短边的一半
            setCornerRadius(Math.min(r.width(), r.height()) / 2);
        }
    }

    public static THDRoundButtonDrawable fromAttributeSet(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.THDRoundButton, defStyleAttr, 0);
        ColorStateList colorBg = typedArray.getColorStateList(R.styleable.THDRoundButton_thd_backgroundColor);
        ColorStateList colorBorder = typedArray.getColorStateList(R.styleable.THDRoundButton_thd_borderColor);
        int borderWidth = typedArray.getDimensionPixelSize(R.styleable.THDRoundButton_thd_borderWidth, 0);
        boolean isRadiusAdjustBounds = typedArray.getBoolean(R.styleable.THDRoundButton_thd_isRadiusAdjustBounds, false);
        int mRadius = typedArray.getDimensionPixelSize(R.styleable.THDRoundButton_thd_radius, 0);
        int mRadiusTopLeft = typedArray.getDimensionPixelSize(R.styleable.THDRoundButton_thd_radiusTopLeft, 0);
        int mRadiusTopRight = typedArray.getDimensionPixelSize(R.styleable.THDRoundButton_thd_radiusTopRight, 0);
        int mRadiusBottomLeft = typedArray.getDimensionPixelSize(R.styleable.THDRoundButton_thd_radiusBottomLeft, 0);
        int mRadiusBottomRight = typedArray.getDimensionPixelSize(R.styleable.THDRoundButton_thd_radiusBottomRight, 0);
        typedArray.recycle();

        THDRoundButtonDrawable bg = new THDRoundButtonDrawable();
        bg.setBgData(colorBg);
        bg.setStrokeData(borderWidth, colorBorder);
        if (mRadiusTopLeft > 0 || mRadiusTopRight > 0 || mRadiusBottomLeft > 0 || mRadiusBottomRight > 0) {
            float[] radii = new float[]{
                    mRadiusTopLeft, mRadiusTopLeft,
                    mRadiusTopRight, mRadiusTopRight,
                    mRadiusBottomRight, mRadiusBottomRight,
                    mRadiusBottomLeft, mRadiusBottomLeft
            };
            bg.setCornerRadii(radii);
            isRadiusAdjustBounds = false;
        } else {
            bg.setCornerRadius(mRadius);
        }
        bg.setIsRadiusAdjustBounds(isRadiusAdjustBounds);
        return bg;
    }

    public int getStrokeWidth() {
        return mStrokeWidth;
    }

    public static THDRoundButtonDrawable create(Builder builder){
        THDRoundButtonDrawable bg = new THDRoundButtonDrawable();
        if(builder.backgroundColor>0){
            bg.setBgData(ContextCompat.getColorStateList(builder.context,builder.backgroundColor));
        }
        ColorStateList colorBorder = null;
        if(builder.borderColor>0){
            colorBorder =   ContextCompat.getColorStateList(builder.context,builder.borderColor);
        }

        bg.setStrokeData(CommonUtils.getInt(builder.borderWidth,0), colorBorder);

        bg.setCornerRadius(CommonUtils.getInt(builder.radius,0));

        bg.setIsRadiusAdjustBounds(false);

        return bg;
    }

    public static class Builder{
        private int backgroundColor;
        private int borderWidth;
        private int borderColor;
        private int radius;
        private Context context;

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder setBorderWidth(int borderWidth) {
            this.borderWidth = borderWidth;
            return this;
        }

        public Builder setBorderColor(int borderColor) {
            this.borderColor = borderColor;
            return this;
        }

        public Builder setRadius(int radius) {
            this.radius = radius;
            return this;
        }

        public THDRoundButtonDrawable build(){
            return create(this);
        }
    }
}
