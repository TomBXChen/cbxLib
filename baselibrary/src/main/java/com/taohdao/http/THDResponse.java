package com.taohdao.http;

import okhttp3.ResponseBody;

public class THDResponse {
    public ResponseBody responseBody;
    public boolean[] isShowToas = new boolean[]{false,true};


    public THDResponse(ResponseBody responseBody) {
        this.responseBody = responseBody;
    }

    public THDResponse(ResponseBody responseBody, boolean[] isShowToas) {
        this.responseBody = responseBody;
        this.isShowToas = isShowToas;
    }
}
