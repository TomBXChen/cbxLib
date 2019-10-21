package com.taohdao.utils;

import android.content.Context;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.jess.arms.integration.AppManager;
import com.taohdao.base.BaseApp;
import com.taohdao.base.R;
import com.taohdao.library.common.widget.span.THDTouchableSpan;
import com.taohdao.library.utils.EventBusUtils;

import static com.taohdao.library.GlobalConfig.APP_TIMEOUT;
import static com.taohdao.library.GlobalConfig.NEW_ACTIVITY_TIEZI_USER_LIST;

/**
 * @author KCrason
 * @date 2018/5/2
 */
public class SpanUtils {

    private static int highlightTextNormalColor;
    private static int highlightTextPressedColor;
    private static int highlightBgNormalColor;
    private static int highlightBgPressedColor;

    static{
        highlightTextNormalColor =   ContextCompat.getColor(BaseApp.getInstance(), R.color.basic_blue);
        highlightTextPressedColor = ContextCompat.getColor(BaseApp.getInstance(), R.color.basic_blue_light);
        highlightBgNormalColor =  ContextCompat.getColor(BaseApp.getInstance(), android.R.color.transparent);
        highlightBgPressedColor =  ContextCompat.getColor(BaseApp.getInstance(), R.color.gray_300);
    }

    public static SpannableStringBuilder makeSingleCommentSpan(Context context, String parentUser, String parentUserId,String commentContent) {
        String richText = String.format("%s:%s", parentUser, commentContent);
        SpannableStringBuilder builder = new SpannableStringBuilder(richText);
        if (!TextUtils.isEmpty(parentUser)) {
            builder.setSpan(new THDTouchableSpan(highlightTextNormalColor, highlightTextPressedColor, highlightBgNormalColor, highlightBgPressedColor) {
                @Override
                public void onSpanClick(View widget) {
                    Message message = new Message();
                    message.what = NEW_ACTIVITY_TIEZI_USER_LIST;
                    message.obj = parentUserId;
                    AppManager.post(message);
                }
            }, 0, parentUser.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }

    public static SpannableStringBuilder makeReplyCommentSpan(Context context, String parentUserName, String childUserName,String parentUserId,String chidUserId, String commentContent) {
        String richText = String.format("%s 回复 %s: %s", MyStringUtils.checkNull(parentUserName,"未定义"), MyStringUtils.checkNull(childUserName,"未定义"), commentContent);
        SpannableStringBuilder builder = new SpannableStringBuilder(richText);
        int parentEnd = 0;
        if (!TextUtils.isEmpty(parentUserName)) {
            parentEnd = parentUserName.length();
            builder.setSpan(new THDTouchableSpan(highlightTextNormalColor, highlightTextPressedColor, highlightBgNormalColor, highlightBgPressedColor) {
                @Override
                public void onSpanClick(View widget) {
                    Message message = new Message();
                    message.what = NEW_ACTIVITY_TIEZI_USER_LIST;
                    message.obj = parentUserId;
                    AppManager.post(message);
//                    ARouterUtils.navigation(A_TIEZI_OTHERS, new ARouterUtils.Builder().put("ids", mBean.userId).build());
                }
            }, 0, parentEnd, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        if (!TextUtils.isEmpty(childUserName)) {
            int childStart = parentEnd + 4;
            int childEnd = childStart + childUserName.length();
            builder.setSpan(new THDTouchableSpan(highlightTextNormalColor, highlightTextPressedColor, highlightBgNormalColor, highlightBgPressedColor) {
                @Override
                public void onSpanClick(View widget) {
                    Message message = new Message();
                    message.what = NEW_ACTIVITY_TIEZI_USER_LIST;
                    message.obj = chidUserId;
                    AppManager.post(message);
                }
            }, childStart, childEnd, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }


    public static SpannableString makeReplyCommentSpan2(Context context, String parentUserName, String childUserName, String commentContent) {
        String richText = String.format("%s回复%s: %s", parentUserName, childUserName, commentContent);
        SpannableString builder = new SpannableString(richText);
        int parentEnd = 0;
        if (!TextUtils.isEmpty(parentUserName)) {
            parentEnd = parentUserName.length();
            builder.setSpan(new THDTouchableSpan(highlightTextNormalColor, highlightTextPressedColor, highlightBgNormalColor, highlightBgPressedColor) {
                @Override
                public void onSpanClick(View widget) {
                    Toast.makeText(context, "click @qmui", Toast.LENGTH_SHORT).show();
                }
            }, 0, parentEnd, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        if (!TextUtils.isEmpty(childUserName)) {
            int childStart = parentEnd + 2;
            int childEnd = childStart + childUserName.length();
            builder.setSpan(new THDTouchableSpan(highlightTextNormalColor, highlightTextPressedColor, highlightBgNormalColor, highlightBgPressedColor) {
                @Override
                public void onSpanClick(View widget) {
                    Toast.makeText(context, "click @qmui", Toast.LENGTH_SHORT).show();
                }
            }, childStart, childEnd, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }

}
