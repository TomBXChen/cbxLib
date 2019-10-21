package com.taohdao.utils;

import android.text.TextUtils;

import java.io.File;

import static com.taohdao.library.GlobalConfig.API;
import static com.taohdao.library.GlobalConfig.IMG_URL;


/**
 * Created by admin on 2018/3/16.
 */

public class MyStringUtils {


    public static String getRealUrl(String url) {

//        return "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1563804472911&di=535f5ad0c0abc7659d8a700b8fab668e&imgtype=0&src=http%3A%2F%2Fpic.3h3.com%2Fup%2F2015-3%2F2015310112939885970.jpg";
        if (TextUtils.isEmpty(url)) {
            return "---";//不能空字符串是为了配合ARMS框架 框架会抛出异常
        }
        if (url.startsWith("http")) {
            return url;
        }
        return IMG_URL + File.separator + url;
    }

    public static String adjustPrefix(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        if (url.startsWith("http")) {
            return url;
        }
        return API + File.separator + url;
    }

    public static String checkNull(String content) {
        if (content == null || content.length() == 0) {
            return "";
        }
        return content.trim();
    }

    public static String checkNull(String content, String defaultValue) {
        if (content == null || content.length() == 0) {
            return defaultValue;
        }
        return content.trim();
    }

    public static String checkNullWithFormat(String format, Object content, String defaultValue) {
        if (content == null) {
            return defaultValue;
        }

        return String.format(format, content);
    }

    /**
     * @param args
     * @return true为都不为空 false 为有其中一个为空
     */
    public static boolean checkArgs(String... args) {
        if (args != null && args.length > 0) {
            boolean noEmpty = true;
            for (String arg : args) {
                if (TextUtils.isEmpty(arg)) {
                    noEmpty = false;
                    break;
                }
            }
            return noEmpty;
        }

        return false;
    }


    public static boolean checkTargetsArgs(String check,String... targets){
        if (!TextUtils.isEmpty(check)&&targets != null && targets.length > 0) {
            for (String arg : targets) {
                if (TextUtils.equals(check,arg)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String adjustGender(String sex) {
        return TextUtils.isEmpty(sex)?"未设置":"0".equals(sex.trim()) ? "女" : "男";
    }


    public static String hintMobile(String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            return "";
        }
        return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    public static double adjustDouble(String doubleStr){
        try {
            return Double.valueOf(doubleStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static float adjustFloat(String floatStr){
        try {
            return Float.valueOf(floatStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
