package com.taohdao.library.common.widget.span;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

/**
 * 支持以 Typeface 的方式设置 span 的字体，实现自定义字体的效果
 */
public class THDCustomTypefaceSpan extends TypefaceSpan {

    /* http://stackoverflow.com/questions/6612316/how-set-spannable-object-font-with-custom-font#answer-10741161 */

    public static final Creator<THDCustomTypefaceSpan> CREATOR = new Creator<THDCustomTypefaceSpan>() {
        @Override
        public THDCustomTypefaceSpan createFromParcel(Parcel source) {
            return null;
        }

        @Override
        public THDCustomTypefaceSpan[] newArray(int size) {
            return new THDCustomTypefaceSpan[size];
        }
    };
    private final @Nullable Typeface newType;

    /**
     * @param family Typeface 字体的字体名
     * @param type 该字体的 Typeface 对象
     */
    public THDCustomTypefaceSpan(String family, @Nullable Typeface type) {
        super(family);
        newType = type;
    }

    private static void applyCustomTypeFace(Paint paint, @Nullable Typeface tf) {
        if (tf == null) {
            return;
        }

        int oldStyle;
        Typeface old = paint.getTypeface();
        if (old == null) {
            oldStyle = 0;
        } else {
            oldStyle = old.getStyle();
        }

        int fake = oldStyle & ~tf.getStyle();
        if ((fake & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }

        if ((fake & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
        }

        paint.setTypeface(tf);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        applyCustomTypeFace(ds, newType);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        applyCustomTypeFace(paint, newType);
    }
}