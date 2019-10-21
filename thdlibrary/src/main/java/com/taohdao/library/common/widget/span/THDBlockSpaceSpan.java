package com.taohdao.library.common.widget.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.text.style.ReplacementSpan;

import com.taohdao.library.utils.THDDeviceHelper;


/**
 * 提供一个整行的空白的Span，可用来用于制作段间距
 *
 * @author cginechen
 * @date 2016-02-17
 */
public class THDBlockSpaceSpan extends ReplacementSpan {
    private int mHeight;

    public THDBlockSpaceSpan(int height) {
        mHeight = height;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        if (fm != null && !THDDeviceHelper.isMeizu()) {
            //return后宽度为0，因此实际空隙和段落开始在同一行，需要加上一行的高度
            fm.ascent = fm.top = -mHeight - paint.getFontMetricsInt(fm);
            fm.descent = fm.bottom = 0;
        }
        return 0;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {

    }
}
