package com.taohdao.http.mvp.interfaces;

import android.app.Activity;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.taohdao.http.BasicsRequest;
import com.taohdao.http.BasicsResponse;
import com.taohdao.http.THDResponse;

import java.io.File;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import me.jessyan.progressmanager.ProgressListener;
import okhttp3.ResponseBody;

/**
 * Created by admin on 2018/3/15.
 */

public interface IBasics {
    interface View extends IView {
        void callback(BasicsResponse response, int tag, Object object);
        Activity getActivity();
    }

    interface Model extends IModel {
        Observable<ResponseBody> post(String actionHeader, Map<String, String> map);



        Observable<ResponseBody> get(String actionHeader, Map<String, String> map);

        Observable<ResponseBody> upSingleFile(String imageType, File file, ProgressListener progressListener);

        Observable<ResponseBody> upLoadFiles(String imageType, List<File> file, ProgressListener progressListener);

        <T extends BasicsRequest> Observable<THDResponse> post(T t);

        <T extends BasicsRequest> Observable<THDResponse>  postJson(T t);

        <T extends BasicsRequest> Observable<THDResponse> get(T t);

    }
}
