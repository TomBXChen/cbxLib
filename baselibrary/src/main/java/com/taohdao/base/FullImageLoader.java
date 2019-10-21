package com.taohdao.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.jess.arms.http.imageloader.glide.GlideArms;
import com.jess.arms.http.imageloader.glide.GlideRequest;
import com.jess.arms.http.imageloader.glide.GlideRequests;
import com.taohdao.library.common.widget.gallery.loader.IZoomMediaLoader;
import com.taohdao.library.common.widget.gallery.loader.MySimpleTarget;


/**
 * Created by admin on 2018/4/26.
 */

public class FullImageLoader implements IZoomMediaLoader {
    RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.ic_placeholder_small)
            .error(R.mipmap.ic_placeholder_small)
            .diskCacheStrategy(DiskCacheStrategy.ALL);
    @Override
    public void displayImage(@NonNull Fragment context, @NonNull String path, String referer, final@NonNull MySimpleTarget<Drawable> simpleTarget) {
        GlideRequests requests;

        requests = GlideArms.with(context);//如果context是activity则自动使用Activity的生命周期


        GlideRequest<Drawable> glideRequest = null;
        if(!TextUtils.isEmpty(referer)){
            GlideUrl headers = new GlideUrl(path, new LazyHeaders.Builder()
                    .addHeader("Referer",referer)
//                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36")
                    .build());
            glideRequest = requests.load(headers);
        }else{
            glideRequest = requests.load(path);
        }
        glideRequest.apply(options);


        glideRequest
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        simpleTarget.onLoadStarted();
                    }


                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        simpleTarget.onLoadFailed(errorDrawable);
                    }

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        simpleTarget.onResourceReady(resource);
                    }
                });
    }

    @Override
    public void onStop(@NonNull Fragment context) {
        Glide.with(context).onStop();

    }

    @Override
    public void clearMemory(@NonNull Context c) {
        Glide.get(c).clearMemory();
    }
}
