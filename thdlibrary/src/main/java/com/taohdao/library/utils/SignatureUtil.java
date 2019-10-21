package com.taohdao.library.utils;

/**
 * Created by admin on 2018/3/16.
 */

public class SignatureUtil {
    public static final int TYPE_T_OPEN_ID = 0;
    public static final int TYPE_W_OPEN_ID = 1;
    public static final int TYPE_W_OPEN_SECRET = 2;
    public static final int TYPE_MI_ID = 3;
    public static final int TYPE_MI_KEY = 4;
    public static final int TYPE_MZ_ID = 5;
    public static final int TYPE_MZ_KEY = 6;
    public static final int TYPE_TINKER_ID = 7;
    public static final int TYPE_SIGN_KEY = 8;

    static {
        System.loadLibrary("signature");
    }

    public static native String getSignatureParam(int type);
}
