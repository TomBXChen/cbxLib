package com.taohdao.library.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/8/24.
 */

public class GSONUtils {
    /**
     * 解析没有数据头的纯数组
     */
    public static <T> List<T> createList(Gson gson, String json, Class<T> clazz) {
        JsonParser parser = new JsonParser();
        //将JSON的String 转成一个JsonArray对象
        JsonArray jsonArray = parser.parse(json).getAsJsonArray();
        ArrayList<T> userBeanList = new ArrayList<>();

        //加强for循环遍历JsonArray
        for (JsonElement user : jsonArray) {
            //使用GSON，直接转成Bean对象
            final T t = gson.fromJson(user, clazz);
            userBeanList.add(t);
        }

        return userBeanList;
    }

}
