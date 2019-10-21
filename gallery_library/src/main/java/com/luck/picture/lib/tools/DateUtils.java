package com.luck.picture.lib.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * author：luck
 * project：PictureSelector
 * package：com.luck.picture.lib.tool
 * email：893855882@qq.com
 * data：2017/5/25
 */

public class DateUtils {
    private static SimpleDateFormat msFormat = new SimpleDateFormat("mm:ss");

    /**
     * MS turn every minute
     *
     * @param duration Millisecond
     * @return Every minute
     */
    public static String timeParse(long duration) {
        String time = "";
        if (duration > 1000) {
            time = timeParseMinute(duration);
        } else {
            long minute = duration / 60000;
            long seconds = duration % 60000;
            long second = Math.round((float) seconds / 1000);
            if (minute < 10) {
                time += "0";
            }
            time += minute + ":";
            if (second < 10) {
                time += "0";
            }
            time += second;
        }
        return time;
    }

    /**
     * MS turn every minute
     *
     * @param duration Millisecond
     * @return Every minute
     */
    public static String timeParseMinute(long duration) {
        try {
            return msFormat.format(duration);
        } catch (Exception e) {
            e.printStackTrace();
            return "0:00";
        }
    }

    /**
     * 判断两个时间戳相差多少秒
     *
     * @param d
     * @return
     */
    public static int dateDiffer(long d) {
        try {
            long l1 = Long.parseLong(String.valueOf(System.currentTimeMillis()).substring(0, 10));
            long interval = l1 - d;
            return (int) Math.abs(interval);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 判断目标日期距离当前日期多长时间
     *
     * @param timeStamp
     * @return
     */
    public static String convertTimeToFormat(long timeStamp) {
        long curTime = System.currentTimeMillis() / (long) 1000;
        long time = curTime - timeStamp;

        if (time < 60 && time >= 0) {
            return "刚刚";
        } else if (time >= 60 && time < 3600) {
            return time / 60 + "分钟前";
        } else if (time >= 3600 && time < 3600 * 24) {
            return time / 3600 + "小时前";
        } else if (time >= 3600 * 24 && time < 3600 * 24 * 30) {
            return time / 3600 / 24 + "天前";
        } else if (time >= 3600 * 24 * 30 && time < 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 + "月前";
        } else if (time >= 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 / 12 + "年前";
        } else {
            return "刚刚";
        }
    }

    /**
     * 字符串转换为对应日期
     *
     * @param source
     * @param pattern "yyyy-MM-dd"
     * @return
     */
    public static Date stringToDate(String source, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = simpleDateFormat.parse(source);
        } catch (Exception e) {
        }
        return date;
    }

    /**
     * 字符串转换为对应时间戳
     *
     * @param source
     * @param pattern "yyyy-MM-dd"
     * @return
     */
    public static long stringToLong(String source, String pattern) {
        return stringToDate(source,pattern).getTime()/1000;
    }

    public static long stringToRealLong(String source, String pattern) {
        return stringToDate(source,pattern).getTime();
    }
}
