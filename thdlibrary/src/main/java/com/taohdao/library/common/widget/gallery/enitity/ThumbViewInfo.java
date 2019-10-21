package com.taohdao.library.common.widget.gallery.enitity;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yangc on 2017/4/26.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated: 图片预览实体类
 *
 * {@link IThumbViewInfo}.
 **/
@Deprecated
public class ThumbViewInfo implements IThumbViewInfo {
    //图片地址
    private String url;
    // 记录坐标
    private Rect mBounds;

    public ThumbViewInfo(String url) {
        this.url = url;
    }

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public Rect getBounds() {
        return mBounds;
    }

    @Override
    public String getReferer() {
        return null;
    }

    public void setBounds(Rect bounds) {
        mBounds = bounds;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeParcelable(this.mBounds, 0);
    }

    protected ThumbViewInfo(Parcel in) {
        this.url = in.readString();
        this.mBounds = in.readParcelable(Rect.class.getClassLoader());
    }

    public static final Parcelable.Creator<ThumbViewInfo> CREATOR = new Parcelable.Creator<ThumbViewInfo>() {
        public ThumbViewInfo createFromParcel(Parcel source) {
            return new ThumbViewInfo(source);
        }

        public ThumbViewInfo[] newArray(int size) {
            return new ThumbViewInfo[size];
        }
    };
}
