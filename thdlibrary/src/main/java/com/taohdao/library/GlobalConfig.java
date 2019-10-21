package com.taohdao.library;

import com.taohdao.library.utils.SignatureUtil;

/**
 * Created by admin on 2018/3/13.
 */

public class GlobalConfig {

    public static final int APP_LOGIN = 5004;
    public static final int APP_LOGOUT = 5005;
    public static final int APP_TIMEOUT = 5006;
    public static final int APP_BIND_PUSH = 5007;
    public static final int APP_UNBIND_PUSH = 5008;
    public static final int APP_SHOW_TOAST = 5009;

    public static final int NEW_ACTIVITY_TIEZI_USER_LIST = 6001;

    public static final String API = "http://218.104.147.230:22346/";
    public static final String API_WECHAT = "https://api.weixin.qq.com/";//微信登录使用到的URL

    public static final String API_UPLOAD = "http://api.luqu411.com/";
    public static final String IMG_URL = "http://images.luqu411.com/";


    public static final String SIGN_KEY = SignatureUtil.getSignatureParam(SignatureUtil.TYPE_SIGN_KEY);
    public static final String W_OPEN_ID = SignatureUtil.getSignatureParam(SignatureUtil.TYPE_W_OPEN_ID);
    public static final String W_OPEN_SECRET = SignatureUtil.getSignatureParam(SignatureUtil.TYPE_W_OPEN_SECRET);//微信开放SECRET
    public static final String W_MI_ID = SignatureUtil.getSignatureParam(SignatureUtil.TYPE_MI_ID);
    public static final String W_MI_KEY = SignatureUtil.getSignatureParam(SignatureUtil.TYPE_MI_KEY);

    public static final int LOAD = 1 << 1;
    public static final int LOAD_MORE = 1 << 2;
    public static final int SUBMIT = 1 << 3;
    public static final int DELETE = 1 << 4;
    public static final int UPLOAD = 1 << 5;
    public static final int PREVIEW = 1 << 6;
    public static final int UPDATE = 1 << 7;
    public static final int ADD = 1 << 8;
    public static final int PRODUCT = 1 << 9;


    public static final int RECEIPT = 1 << 10;
    public static final int BINDING = 1 << 11;
    public static final int SIMPLE_REQUEST = 1 << 12;
    public static final int COLLECT = 1 << 13;
    public static final int ADD_CART = 1 << 14;
    public static final int INFO = 1 << 15;
    public static final int NEXT = 1 << 16;
    public static final int CANCEL = 1 << 17;
    public static final int SUCCESS = 1 << 18;
    public static final int PAY = 1 << 19;
    public static final int MOBILE = 1 << 20;


    public static final String SCAN_TYPE_DRUGSTORE = "drugstore";
    public static final String SCAN_TYPE_MEDICINE = "medicineRep";
    public static final String THD_LOCAL_NAME = "mvp.ui.activity.";


}
