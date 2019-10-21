package com.taohdao.http.common_controller;

import com.taohdao.http.BasicsRequest;

/**
 * Created by admin on 2019/4/19.
 */

public class HotLineRq extends BasicsRequest {


    public HotLineRq() {
    }

    @Override
    public String getRequestUrl() {
        return "common/hotline";
    }
}
