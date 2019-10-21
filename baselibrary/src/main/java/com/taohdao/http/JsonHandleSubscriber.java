package com.taohdao.http;

import java.io.IOException;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import okhttp3.ResponseBody;

/**
 * Created by admin on 2018/3/20.
 */

public abstract class JsonHandleSubscriber extends ErrorHandleSubscriber<THDResponse> {


    public JsonHandleSubscriber(RxErrorHandler rxErrorHandler) {
        super(rxErrorHandler);
    }

    @Override
    public void onNext(THDResponse thdResponse) {
        if(thdResponse!=null){
            try {
                final ResponseBody responseBody = thdResponse.responseBody;
                final String json = responseBody.string();
                final JsonResponse response = JsonResponse.getResponse(json, thdResponse.isShowToas);
                if(response.getCode() == 0){
                    onSucceed(response);
                }else{
                    onDefinedError(response.getCode());
                }
            } catch (IOException e) {
                e.printStackTrace();
                onError(null);
            }

        }
    }


    public abstract void onSucceed(JsonResponse response);
    public  void onDefinedError(int code){

    }
}
