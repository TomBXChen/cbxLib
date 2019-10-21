package com.cbx.lib.http.home;

import com.taohdao.http.BasicsRequest;
import com.taohdao.http.PageRequest;

public class AdPageListRq extends PageRequest {
    @Override
    public String getRequestUrl() {
        return "marketing/ad/pageList";
    }
}
