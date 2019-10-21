package com.taohdao.http;

import android.os.Message;
import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.jess.arms.integration.AppManager;
import com.jess.arms.utils.ArmsUtils;
import com.taohdao.base.BaseApp;

import org.json.JSONException;
import org.json.JSONObject;

import static com.taohdao.library.GlobalConfig.APP_TIMEOUT;


/**
 * Created by admin on 2018/3/20.
 */

public class JsonResponse extends BasicsResponse {

    /**
     * 解析json,获取响应对象
     *
     * @param json
     * @param isShowToast
     * @return
     */
    public static JsonResponse getResponse(String json, boolean[] isShowToast) {
        JsonResponse mResponse = new JsonResponse();
        mResponse.setFullData(json);
        int code = -1;
        String msg = "";
        String data = "";

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            boolean hasCode = jsonObject.has("code");
            if (hasCode) {
                code = jsonObject.getInt("code");
            }

            boolean hasMessage = jsonObject.has("errors");
            if (hasMessage) {
                msg = jsonObject.getString("errors");
            }

            boolean hasData = jsonObject.has("data");
            if (hasData) {
                data = jsonObject.getString("data");
            }
//
            if (isShowToast != null) {
                int codenumber = Integer.valueOf(code);
                if (codenumber !=  0 && isShowToast[1]) {
                    ToastUtils.showShort(msg);
                } else if ((!TextUtils.isEmpty(msg) && codenumber == 0 && isShowToast[0])) {
                    ToastUtils.showShort(msg);
                }
            }

            if (code == 301) {//TOKEN失效，直接回滚到登录界面
                Message message = new Message();
                message.what = APP_TIMEOUT;
                AppManager.post(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mResponse.setCode(code);
        mResponse.setMsg(msg);
        mResponse.setData(data);
        return mResponse;
    }

    @Override
    public <T> T getBean(Class<T> clazz, boolean isFull) throws IllegalArgumentException, JSONException {
        if (!isFull && TextUtils.isEmpty(getData())) {
            throw new IllegalArgumentException(
                    "In the JsonResponse, data can't be empty");
        } else if (isFull && TextUtils.isEmpty(getFullData())) {
            throw new IllegalArgumentException(
                    "In the JsonResponse, Ful data can't be empty");
        }
        T object = null;
//        T object = CommonUtils.getGson().fromJson(getData(), clazz);
        if (isFull && TextUtils.isEmpty(getData())) {
            try {
                return (T) Class.forName(clazz.getName()).newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            final Gson gson = ArmsUtils.obtainAppComponentFromContext(BaseApp.getInstance()).gson();
            object = gson.fromJson(isFull ? getFullData() : getData(), clazz);
        }

        return object;
    }
}
