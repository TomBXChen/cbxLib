package com.taohdao.library.common.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by fangxiaomi on 2016/3/10.
 * 文本左右对齐(分散对齐)的TextView
 */
public class DistributedTextView  extends TextView {
    public DistributedTextView(Context context) {
        super(context);
    }

    public DistributedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DistributedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DistributedTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 获取默认的Paint
        Paint textPaint = getPaint();

        // 获取默认的文本颜色
        textPaint.setColor(getCurrentTextColor());

        // 获取TextView宽度
        int width = getMeasuredWidth();

        // 获取文本内容
        String text = getText().toString();

        // 获取绘制时的 y 坐标，既TextView的BaseLine
        float drawY = getBaseline();

        // 排除冒号
        if (text.endsWith(":") || text.endsWith("：")) {
            // 获取冒号字符
            String colonChar = text.substring(text.length() - 1);
            // 获取冒号字符所占的宽度
            float colonWidth = textPaint.measureText(colonChar);
            // 在原文本的基础上移除冒号，得到没有冒号的内容
            text = text.substring(0, text.length() - 1);
            // 在原本宽度的基础上移除冒号所占的宽度
            width = width - (int) colonWidth;
            // 绘制冒号字符
            float colonX = width;
            canvas.drawText(colonChar, colonX, drawY, textPaint);
        }

        // 处理左边的字符（下标为0的字符）
        // 获取左边的字符
        String startChar = text.substring(0, 1);
        // 绘制左边字符
        canvas.drawText(startChar, 0.0f, drawY, textPaint);

        // 处理右边的字符（下标为内容长度-1）
        // 获取右边的字符
        String endChar = text.substring(text.length() - 1, text.length());
        // 获取绘制右边字符时的 x 坐标
        float endCharX = width - textPaint.measureText(endChar);
        // 绘制右边字符
        canvas.drawText(endChar, endCharX, drawY, textPaint);

        // 处理中间字符串
        if (text.length() == 3) {
            // 中间字符串只有一个字符
            // 获取字符
            String middleChar = text.substring(1, 2);
            // 获取绘制时的 x 坐标
            float middleX = (width - textPaint.measureText(middleChar)) / 2;
            // 绘制字符
            canvas.drawText(middleChar, middleX, drawY, textPaint);

        } else if (text.length() > 3) {
            // 中间字符串不止一个字符
            // 获取中间空间（可绘制空间）的宽度
            float spaceMaxWidth = width;
            // 排除字符串所占的宽度，得到空白部分的总宽度
            for (int i = 0 ; i < text.length() ; i ++) {
                spaceMaxWidth -= textPaint.measureText(text.substring(i, i + 1));
            }

            // 计算单个间隔的宽度
            float spaceWidth = spaceMaxWidth / ((text.length() - 2) + 1);

            // 循环绘制字符
            for (int i =1  ; i < text.length() - 1 ; i ++) {
                // 获取字符
                String drawChar = text.substring(i, i + 1);
                // 获取字符宽度
                float charWidth = textPaint.measureText(drawChar);
                // 获取绘制时的 x 坐标
                float charX = textPaint.measureText(startChar) + spaceWidth * i + charWidth * (i - 1);
                // 绘制字符
                canvas.drawText(drawChar, charX, drawY, textPaint);

            }
        }

    }
}
