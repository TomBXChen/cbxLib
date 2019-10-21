package com.taohdao.http.mvp.model.service;


import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by admin on 2018/3/15.
 */

public interface BasicsService {
    @POST()
    @FormUrlEncoded
    Observable<ResponseBody> post(@Url String url, @FieldMap Map<String, String> map);

    @POST()
//    @Headers({"Content-Type: application/json;charset=utf-8","Accept: application/json"})
    Observable<ResponseBody> postForJson(@Url String url,@Body RequestBody info);

    @GET()
    Observable<ResponseBody> get(@Url String url, @QueryMap Map<String, String> map);

    @Headers({"Domain-Name: upload"})
    @Multipart
    @POST("upload/uploadFile")
    Observable<ResponseBody> upSingleFile(@Part("imgType") RequestBody imageType, @Part() MultipartBody.Part part);

    @Headers({"Domain-Name: upload"})
    @Multipart
    @POST("upload/uploadFiles")
    Observable<ResponseBody> uploadFiles(@Part("imgType") RequestBody imageType,@Part() List<MultipartBody.Part> parts);

}
