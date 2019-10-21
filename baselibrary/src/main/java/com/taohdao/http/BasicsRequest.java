package com.taohdao.http;

import com.google.gson.Gson;
import com.jess.arms.utils.ArmsUtils;
import com.taohdao.base.BaseApp;
import com.taohdao.http.utils.HttpsUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2019/4/2.
 */

public abstract class BasicsRequest implements Serializable, Comparable<BasicsRequest> {

    private boolean[] showMsg = new boolean[]{false, true};//第一个参数：请求成功，展示json中的msg；第二个参数：请求失败，展示json中的msg
    //应用场景：请求成功就展示，失败就不需要展示，

    public boolean[] isShowMsg() {
        return showMsg;
    }

    public void setShowMsg(boolean[] showMsg) {
        this.showMsg = showMsg;
    }

    public String toJson(){
        Gson gson = ArmsUtils.obtainAppComponentFromContext(BaseApp.getInstance()).gson();
        return gson.toJson(this);
    }

    public Map<String, String> getMapParams() {
        Class<? extends BasicsRequest> clazz = this.getClass();
        Class<? extends Object> superclass = clazz.getSuperclass();

        Field[] fields = clazz.getDeclaredFields();
        Field[] superFields = superclass.getDeclaredFields();

//        if (fields == null || fields.length == 0) {
//            return Collections.emptyMap();
//        }

        Map<String, String> params = new HashMap<String, String>();
        try {
            if (fields != null && fields.length > 0) {
                for (Field field : fields) {
                    field.setAccessible(true);
                    if (field.getName().equals("showMsg")) {
                        continue;
                    }
                    if (field.getType() == HashMap.class) {
                        HashMap<String, String> list = (HashMap<String, String>) field.get(this);
                        for (Map.Entry<String, String> item : list.entrySet()) {
                            params.put(item.getKey(), item.getValue());
                        }
                    } else {
                        params.put(field.getName(), field.get(this) != null ? String.valueOf(field.get(this)) : "");
                    }

                }
            }

            if (superFields != null && superFields.length > 0) {
                for (Field superField : superFields) {
                    superField.setAccessible(true);
                    if (superField.getName().equals("showMsg")) {
                        continue;
                    }
                    if (superField.getType() == HashMap.class) {
                        HashMap<String, String> list = (HashMap<String, String>) superField.get(this);
                        for (Map.Entry<String, String> item : list.entrySet()) {
                            params.put(item.getKey(), item.getValue());
                        }
                    } else {
                        params.put(superField.getName(), superField.get(this) != null ? String.valueOf(superField.get(this)) : "");
                    }
                }
            }

            params.put("apikey", "c4c59xdwc93wfa0");
            params.put("devices", "android");
            params.put("timestamp", String.valueOf(System.currentTimeMillis()));
            params.put("nonce", String.valueOf(HttpsUtils.createRandom(100000, 999999)));
            params.put("sign", HttpsUtils.createSign(params.get("timestamp"), params.get("nonce")));

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return params;
    }

    @Override
    public int compareTo(BasicsRequest another) {
        return 0;
    }

    abstract public String getRequestUrl();

    public Object getTag() {
        return null;
    }
}
