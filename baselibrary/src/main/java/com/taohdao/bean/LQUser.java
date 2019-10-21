package com.taohdao.bean;

import android.text.TextUtils;

import com.blankj.utilcode.util.SPUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;

public class LQUser {
    public static final String SAVE_NAME = "THDUser";
    public static final String KEY_VERSION = "thduser_version";
    private static LQUser mInstance;
    private static User userInfo;

    private LQUser() {
        userInfo = readUser();
    }

    public static LQUser getCurrentUser() {
        if (mInstance == null) {
            synchronized (LQUser.class) {
                if (mInstance == null) {
                    mInstance = new LQUser();
                }
            }
        }
        mInstance.refresh();
        return mInstance;
    }

    public User refresh() {
        userInfo = readUser();
        return userInfo;
    }

    public void logout() {
        userInfo = null;
        SPUtils.getInstance(SAVE_NAME).clear();
    }


    public boolean saveUserInfo(User userInfo) {
        if (userInfo == null) {
            return false;
        }
        try {
            User oldInfo = readUser();
            String serialize = serialize(userInfo);
            SPUtils.getInstance(SAVE_NAME).put("user",serialize);
            this.userInfo = userInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public User readUser() {
        String serialize =  SPUtils.getInstance(SAVE_NAME).getString("user", "");
        if (serialize == null || serialize.equals("")) {
            return null;
        }
        User userInfo = null;
        try {
            userInfo = deSerialization(serialize);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return userInfo;
    }

    /**
     * 序列化对象
     *
     * @param person
     * @return
     * @throws IOException
     */
    private String serialize(User person) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        objectOutputStream.writeObject(person);
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
        objectOutputStream.close();
        byteArrayOutputStream.close();
        return serStr;
    }

    /**
     * 反序列化对象
     *
     * @param str
     * @return
     * @throws IOException
     */
    private User deSerialization(String str) throws IOException,
            ClassNotFoundException {
        String redStr = java.net.URLDecoder.decode(str, "UTF-8");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                redStr.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        User person = (User) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return person;
    }

    public User getUserInfo() {
        return userInfo;
    }

    public static User getUser() {
        getCurrentUser().refresh();
        return getCurrentUser().getUserInfo();
    }



    public static void partUpdata(String fieldname, Object value) {
        try {
            if (TextUtils.isEmpty(fieldname) || value == null) {
//                UKLogger.e("partUpdata--fieldname:" + fieldname + " value:" + value);
                return;
            }
            boolean isUpdata = false;
            User userInfo = getCurrentUser().readUser();
            if(userInfo == null)return;
            Class<? extends User> aClass = userInfo.getClass();
            Field[] fields = aClass.getFields();
            for (Field item : fields) {
                if (fieldname.equals(item.getName())) {
                    isUpdata = true;
                    item.setAccessible(true);
                    try {
                        item.set(userInfo, value);
                        item.setAccessible(false);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
//            UKLogger.e(userInfo.toString());
            if (isUpdata) getCurrentUser().saveUserInfo(userInfo);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

    }
}
