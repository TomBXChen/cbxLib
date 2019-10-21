package com.taohdao.utils;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.taohdao.base.BaseApp;
import com.taohdao.base.R;

import static com.luck.picture.lib.config.PictureConfig.LUBAN_COMPRESS_MODE;

/**
 * Created by admin on 2018/3/21.
 */

public class ImageUtils {

    /**
     * 加载图片
     * @param img
     * @param url
     */
    public static void loadImg(ImageView img,String url){
        final ImageLoader imageLoader = ArmsUtils.obtainAppComponentFromContext(img.getContext()).imageLoader();
        imageLoader.loadImage(BaseApp.getInstance(),
                ImageConfigImpl
                        .builder()
                        .url(MyStringUtils.getRealUrl(url))
                        .errorPic(R.mipmap.ic_placeholder_small)
                        .placeholder(R.mipmap.ic_placeholder_small)
                        .imageView(img)
                        .build());
    }

    /**
     * 高斯模糊
     * @param img
     * @param url
     */
    public static void loadBlurImg(ImageView img,String url){
        final ImageLoader imageLoader = ArmsUtils.obtainAppComponentFromContext(img.getContext()).imageLoader();
        imageLoader.loadImage(BaseApp.getInstance(),
                ImageConfigImpl
                        .builder()
                        .url(MyStringUtils.getRealUrl(url))
                        .blurValue(15)
                        .imageView(img)
                        .build());
    }

    public static void loadImg(ImageView img,String url,boolean local){
        final ImageLoader imageLoader = ArmsUtils.obtainAppComponentFromContext(img.getContext()).imageLoader();
        imageLoader.loadImage(BaseApp.getInstance(),
                ImageConfigImpl
                        .builder()
                        .url(local?url:MyStringUtils.getRealUrl(url))
                        .errorPic(R.mipmap.ic_placeholder_small)
                        .placeholder(R.mipmap.ic_placeholder_small)
                        .imageView(img)
                        .build());
    }
    /**
     * 加载图片 可传占位图
     * @param img
     * @param url
     * @param placeholder
     */
    public static void loadImg(ImageView img,String url,int placeholder){
        final ImageLoader imageLoader = ArmsUtils.obtainAppComponentFromContext(img.getContext()).imageLoader();
        imageLoader.loadImage(BaseApp.getInstance(),
                ImageConfigImpl
                        .builder()
                        .url(MyStringUtils.getRealUrl(url))
                        .errorPic(placeholder)
                        .placeholder(placeholder)
                        .imageView(img)
                        .build());
    }


    public static void startGalleryForResult(Activity activity, int maxSelectNum, int resultCode){
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                .theme(R.style.picture_Thd_style)// 主题样式设置 具体参考 values/styles
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
                .previewVideo(false)// 是否可预览视频
                .enablePreviewAudio(false)// 是否预览音频
                .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.setOutputCameraPath("/Chinayie/App")// 自定义拍照保存路径
                .compress(true)// 是否压缩
                .compressMode(LUBAN_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .isGif(false)// 是否显示gif图片
                .openClickSound(false)// 是否开启点击声音
                .selectionMedia(null)// 是否传入已选图片
                //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.compressMaxKB()//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效
                //.compressWH() // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效
                //.videoQuality()// 视频录制质量 0 or 1
                //.videoSecond()//显示多少秒以内的视频
                //.recordVideoSecond()//录制视频秒数 默认60秒
                .forResult(resultCode);//结果回调onActivityResult code PictureConfig.CHOOSE_REQUEST
    }
    public static void startGalleryForResult(Fragment fragment, int maxSelectNum, int resultCode){
        PictureSelector.create(fragment)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                .theme(R.style.picture_Thd_style)// 主题样式设置 具体参考 values/styles
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
                .previewVideo(false)// 是否可预览视频
                .enablePreviewAudio(false)// 是否预览音频
                .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.setOutputCameraPath("/Chinayie/App")// 自定义拍照保存路径
                .compress(true)// 是否压缩
                .compressMode(LUBAN_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .isGif(false)// 是否显示gif图片
                .openClickSound(false)// 是否开启点击声音
                .selectionMedia(null)// 是否传入已选图片
                //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.compressMaxKB()//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效
                //.compressWH() // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效
                //.videoQuality()// 视频录制质量 0 or 1
                //.videoSecond()//显示多少秒以内的视频
                //.recordVideoSecond()//录制视频秒数 默认60秒
                .forResult(resultCode);//结果回调onActivityResult code PictureConfig.CHOOSE_REQUEST
    }

}
