package com.taohdao.http;

import org.json.JSONException;

/**
 * Created by admin on 2018/3/15.
 */

public abstract class BasicsResponse {
    private String fullData;//完整json
    private String msg;
    private String data;
    private int code;
    private Object tag;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public String getFullData() {
        return fullData;
    }

    public void setFullData(String fullData) {
        this.fullData = fullData;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public boolean isSucceed() {
        return getCode() == 0;
    }

    /**
     * 解析单条数据
     *
     * @param clazz
     * @param isFull 是否解析完整json数据
     * @return
     * @throws IllegalArgumentException 参数异常(Response中data为空)
     * @throws JSONException
     */
    public abstract <T> T getBean(Class<T> clazz, boolean isFull)
            throws IllegalArgumentException, JSONException;
}
