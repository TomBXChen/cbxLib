package com.taohdao.library.common.widget.link;

import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.method.Touch;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 *
 * @author cginechen
 * @date 2017-03-20
 */

public class THDLinkTouchMovementMethod extends LinkMovementMethod {

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        return sHelper.onTouchEvent(widget, buffer, event)
                || Touch.onTouchEvent(widget, buffer, event);
    }

    public static MovementMethod getInstance() {
        if (sInstance == null)
            sInstance = new THDLinkTouchMovementMethod();

        return sInstance;
    }

    private static THDLinkTouchMovementMethod sInstance;
    private static THDLinkTouchDecorHelper sHelper = new THDLinkTouchDecorHelper();

}
