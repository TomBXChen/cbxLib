package com.taohdao.library.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;

import com.blankj.utilcode.util.PhoneUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.PermissionUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by admin on 2019/4/9.
 */

public class CommonUtils {
    public static int getInt(int target, int defaultValue) {
        return target > 0 ? target : defaultValue;
    }

    public static <T> List<T> transform(List<T> list) {
        if (list != null && list.size() > 0) {
            return list;
        }
        return Collections.<T>emptyList();
    }

    public static int sub(int target, int minimum) {
        return target > minimum ? target - 1 : minimum;
    }

    public static <T> List<T> adjustJsonCreateList(Context context, Class<T> tClass, String json) {
        final Gson gson = ArmsUtils.obtainAppComponentFromContext(context).gson();
        List<T> temp = new ArrayList<>();
        JsonParser parser = new JsonParser();
        //将JSON的String 转成一个JsonArray对象
        JsonArray jsonArray = parser.parse(json).getAsJsonArray();
        //加强for循环遍历JsonArray
        for (JsonElement user : jsonArray) {
            //使用GSON，直接转成Bean对象
            final T t = gson.fromJson(user, tClass);
            temp.add(t);
        }
        return temp;
    }



    /**
     * 主数据源的数值填充到目标数据源，截止到index下标
     *
     * @param mainList
     * @param targetList
     * @param index
     */
    public static void addItemByIndex(List mainList, List targetList, int index) {
        if (mainList == null || targetList == null) return;
        targetList.clear();
        if(mainList.size() == index||mainList.size() < index){
            targetList.addAll(mainList);
            return;
        }
        for (int i = 0; i < index; i++) {
            targetList.add(mainList.get(i));
        }
    }


}
