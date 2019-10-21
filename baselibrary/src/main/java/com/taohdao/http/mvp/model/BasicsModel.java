package com.taohdao.http.mvp.model;


import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.taohdao.http.BasicsRequest;
import com.taohdao.http.MediaTypes;
import com.taohdao.http.THDResponse;
import com.taohdao.http.mvp.interfaces.IBasics;
import com.taohdao.http.mvp.model.service.BasicsService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import me.jessyan.progressmanager.ProgressListener;
import me.jessyan.progressmanager.ProgressManager;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by admin on 2018/3/14.
 */
@ActivityScope
public class BasicsModel extends BaseModel implements IBasics.Model {
    private BasicsService basicsService;
    @Inject
    public BasicsModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
        basicsService  = mRepositoryManager.obtainRetrofitService(BasicsService.class);
    }
    @Deprecated
    @Override
    public Observable<ResponseBody> post(String actionHeader, Map<String, String> map) {
        return basicsService.post(actionHeader,map);
    }


    @Deprecated
    @Override
    public Observable<ResponseBody> get(String actionHeader, Map<String, String> map) {
        return basicsService.get(actionHeader,map);
    }

    @Override
    public Observable<ResponseBody> upSingleFile(String imageType, File file, ProgressListener progressListener) {
        if(file == null||(!file.exists())||file.length()<=0){
            return null;
        }
//        if(progressListener!=null)ProgressManager.getInstance().addRequestListener(GlobalConfig.API_UPLOAD,progressListener);
        RequestBody requestBody = RequestBody.create(MediaTypes.IMAGE_TYPE, file);
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), imageType);
        MultipartBody.Part part = MultipartBody.Part.createFormData("upload", file.getName(), requestBody);
        return basicsService.upSingleFile(description,part);
    }

    @Override
    public Observable<ResponseBody> upLoadFiles(String imageType, List<File> files, ProgressListener progressListener) {
        if(files == null||files.size()==0){
            return null;
        }
        List<MultipartBody.Part> multipartBodyParts = new ArrayList<>();
//        if(progressListener!=null){
            for (File file : files) {
                ProgressManager.getInstance().addRequestListener(file.getAbsolutePath(),progressListener);
                RequestBody requestBody = RequestBody.create(MediaTypes.IMAGE_TYPE, file);
                MultipartBody.Part part = MultipartBody.Part.createFormData("uploads", file.getName(), requestBody);
//                RequestBody requestBody = RequestBody.create(MediaTypes.IMAGE_TYPE, file);
//                MultipartBody.Part part = MultipartBody.Part.createFormData(active, file.getName(), requestBody);
                multipartBodyParts.add(part);
            }
//        }
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), imageType);
        return basicsService.uploadFiles(description,multipartBodyParts);
    }

    @Override
    public <T extends BasicsRequest> Observable<THDResponse> post(T t) {
        return basicsService.post(t.getRequestUrl(),t.getMapParams())
                .flatMap((Function<ResponseBody, Observable<THDResponse>>) responseBody -> {
                    THDResponse mTHDResponse = new THDResponse(responseBody,t.isShowMsg());
                    return Observable.just(mTHDResponse);
                });
    }

    @Override
    public <T extends BasicsRequest> Observable<THDResponse> postJson(T t) {
        return basicsService.postForJson(t.getRequestUrl(),RequestBody.create(MediaType.parse("application/json; charset=utf-8"), t.toJson()))
                .flatMap((Function<ResponseBody, Observable<THDResponse>>) responseBody -> {
                    THDResponse mTHDResponse = new THDResponse(responseBody,t.isShowMsg());
                    return Observable.just(mTHDResponse);
                });
    }

    @Override
    public <T extends BasicsRequest> Observable<THDResponse> get(T t) {
        return basicsService.get(t.getRequestUrl(),t.getMapParams())
                .flatMap((Function<ResponseBody, Observable<THDResponse>>) responseBody -> {
                    THDResponse mTHDResponse = new THDResponse(responseBody,t.isShowMsg());
                    return Observable.just(mTHDResponse);
                });
    }


}
