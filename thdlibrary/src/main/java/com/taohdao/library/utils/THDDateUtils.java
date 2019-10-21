package com.taohdao.library.utils;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v4.app.FragmentManager;

import com.blankj.utilcode.util.Utils;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.taohdao.library.common.widget.popup.THDWheelPopup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by admin on 2018/7/3.
 */

public class THDDateUtils {

    public static final DateFormat YEAR_MOTH_FORMAT = new SimpleDateFormat("yyyy-MM");

    public static final long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
    public static final long fiveYears = 5L * 365 * 1000 * 60 * 60 * 24L;
    public static final long ninetyYears = 90L * 365 * 1000 * 60 * 60 * 24L;

    public static void showYMDHM(FragmentManager manager,long currentMillseconds, @ColorRes int themeColor,
                               @ColorRes int normalColor, @ColorRes int selectorColor,
                                OnDateSetListener listener) {
        new TimePickerDialog.Builder()
                .setCallBack(listener)
                .setCancelStringId("取消")
                .setSureStringId("确认")
                .setTitleStringId("选择时间")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setHourText("时")
                .setMinuteText("分")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis())
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(currentMillseconds>0?currentMillseconds:System.currentTimeMillis())
                .setThemeColor(Utils.getApp().getResources().getColor(themeColor))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(Utils.getApp().getResources().getColor(normalColor))
                .setWheelItemTextSelectorColor(Utils.getApp().getResources().getColor(selectorColor))
                .setWheelItemTextSize(12)
                .build()
                .show(manager, "timePicker");
    }

    public static void showMDHM(FragmentManager manager,long currentMillseconds, @ColorRes int themeColor,
                               @ColorRes int normalColor, @ColorRes int selectorColor,
                                OnDateSetListener listener) {
        new TimePickerDialog.Builder()
                .setCallBack(listener)
                .setCancelStringId("取消")
                .setSureStringId("确认")
                .setTitleStringId("选择时间")
                .setMonthText("月")
                .setDayText("日")
                .setHourText("时")
                .setMinuteText("分")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis())
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(currentMillseconds>0?currentMillseconds:System.currentTimeMillis())
                .setThemeColor(Utils.getApp().getResources().getColor(themeColor))
                .setType(Type.MONTH_DAY_HOUR_MIN)
                .setWheelItemTextNormalColor(Utils.getApp().getResources().getColor(normalColor))
                .setWheelItemTextSelectorColor(Utils.getApp().getResources().getColor(selectorColor))
                .setWheelItemTextSize(14)
                .build()
                .show(manager, "timePicker");
    }

    public static void showYMD(FragmentManager manager,long currentMillseconds, @ColorRes int themeColor,
                               @ColorRes int normalColor, @ColorRes int selectorColor,
                                OnDateSetListener listener) {
        new TimePickerDialog.Builder()
                .setCallBack(listener)
                .setCancelStringId("取消")
                .setSureStringId("确认")
                .setTitleStringId("选择时间")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis())
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(currentMillseconds>0?currentMillseconds:System.currentTimeMillis())
                .setThemeColor(Utils.getApp().getResources().getColor(themeColor))
                .setType(Type.YEAR_MONTH_DAY)
                .setWheelItemTextNormalColor(Utils.getApp().getResources().getColor(normalColor))
                .setWheelItemTextSelectorColor(Utils.getApp().getResources().getColor(selectorColor))
                .setWheelItemTextSize(15)
                .build()
                .show(manager, "timePicker");
    }

    public static THDWheelPopup showYMDPop(Context context, long currentMillseconds,long minMillseconds, @ColorRes int themeColor,
                               @ColorRes int normalColor, @ColorRes int selectorColor,
                                  THDWheelPopup.OnDateSetListener listener) {
        return new THDWheelPopup.Builder()
                .setCallBack(listener)
                .setCancelStringId("取消")
                .setSureStringId("确认")
                .setTitleStringId("选择时间")
                .setYearText("")
                .setMonthText("")
                .setDayText("")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis() - minMillseconds)
                .setMaxMillseconds(System.currentTimeMillis())
                .setCurrentMillseconds(currentMillseconds > 0 ? currentMillseconds : System.currentTimeMillis())
                .setThemeColor(Utils.getApp().getResources().getColor(themeColor))
                .setType(Type.YEAR_MONTH_DAY)
                .setWheelItemTextNormalColor(Utils.getApp().getResources().getColor(normalColor))
                .setWheelItemTextSelectorColor(Utils.getApp().getResources().getColor(selectorColor))
                .setWheelItemTextSize(15)
                .build(context);
    }

    public static THDWheelPopup showYMDPop(Context context, long currentMillseconds, @ColorRes int themeColor,
                                           @ColorRes int normalColor, @ColorRes int selectorColor,
                                           THDWheelPopup.OnDateSetListener listener) {
        return new THDWheelPopup.Builder()
                .setCallBack(listener)
                .setCancelStringId("取消")
                .setSureStringId("确认")
                .setTitleStringId("选择时间")
                .setYearText("")
                .setMonthText("")
                .setDayText("")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis())
                .setMaxMillseconds(System.currentTimeMillis()+ninetyYears)
                .setCurrentMillseconds(currentMillseconds > 0 ? currentMillseconds : System.currentTimeMillis())
                .setThemeColor(Utils.getApp().getResources().getColor(themeColor))
                .setType(Type.YEAR_MONTH_DAY)
                .setWheelItemTextNormalColor(Utils.getApp().getResources().getColor(normalColor))
                .setWheelItemTextSelectorColor(Utils.getApp().getResources().getColor(selectorColor))
                .setWheelItemTextSize(15)
                .build(context);
    }

    public static void showYMD(FragmentManager manager,long currentMillseconds,long minMillseconds, @ColorRes int themeColor,
                               @ColorRes int normalColor, @ColorRes int selectorColor,
                               OnDateSetListener listener) {
        new TimePickerDialog.Builder()
                .setCallBack(listener)
                .setCancelStringId("取消")
                .setSureStringId("确认")
                .setTitleStringId("选择时间")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis()- minMillseconds)
                .setMaxMillseconds(System.currentTimeMillis())
                .setCurrentMillseconds(currentMillseconds>0?currentMillseconds:System.currentTimeMillis())
                .setThemeColor(Utils.getApp().getResources().getColor(themeColor))
                .setType(Type.YEAR_MONTH_DAY)
                .setWheelItemTextNormalColor(Utils.getApp().getResources().getColor(normalColor))
                .setWheelItemTextSelectorColor(Utils.getApp().getResources().getColor(selectorColor))
                .setWheelItemTextSize(15)
                .build()
                .show(manager, "timePicker");
    }

    public static void showYM(FragmentManager manager, long currentMillseconds,@ColorRes int themeColor,
                               @ColorRes int normalColor, @ColorRes int selectorColor,
                               OnDateSetListener listener) {
        new TimePickerDialog.Builder()
                .setCallBack(listener)
                .setCancelStringId("取消")
                .setSureStringId("确认")
                .setTitleStringId("选择时间")
                .setYearText("年")
                .setMonthText("月")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis() - tenYears)
                .setMaxMillseconds(System.currentTimeMillis())
                .setCurrentMillseconds(currentMillseconds>0?currentMillseconds:System.currentTimeMillis())
                .setThemeColor(Utils.getApp().getResources().getColor(themeColor))
                .setType(Type.YEAR_MONTH)
                .setWheelItemTextNormalColor(Utils.getApp().getResources().getColor(normalColor))
                .setWheelItemTextSelectorColor(Utils.getApp().getResources().getColor(selectorColor))
                .setWheelItemTextSize(15)
                .build()
                .show(manager, "timePicker");
    }

}
