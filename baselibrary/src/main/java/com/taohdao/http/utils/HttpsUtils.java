package com.taohdao.http.utils;


import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SPUtils;
import com.orhanobut.logger.Logger;
import com.taohdao.library.utils.SignatureUtil;

import org.apache.http.conn.util.InetAddressUtils;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

import me.yokeyword.fragmentation.ISupportFragment;

import static com.blankj.utilcode.util.NetworkUtils.NetworkType.NETWORK_NO;
import static com.blankj.utilcode.util.NetworkUtils.NetworkType.NETWORK_UNKNOWN;
import static com.blankj.utilcode.util.NetworkUtils.NetworkType.NETWORK_WIFI;

/**
 * Created by admin on 2018/3/16.
 */

public class HttpsUtils {
    public static Random random = new Random();
    public static final String SAVE_SETTINGS_NAME = "settings";
    public static final String SIGN_IN = SignatureUtil.getSignatureParam(SignatureUtil.TYPE_SIGN_KEY);

    /**
     * 创建一个范围的随机数
     *
     * @param min 最小数
     * @param max 最大数
     * @return
     */
    public static int createRandom(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public static String createSign(String timestamp, String nonce) {
        StringBuilder sb = new StringBuilder();
        sb.append(checkNull(timestamp));
        sb.append(checkNull(nonce));
        sb.append(SIGN_IN);
        return EncryptMD5(EncryptBase64(sb.toString()));
    }

    public static String checkNull(String content) {
        if (content == null || content.length() == 0) {
            return "";
        }
        return content.trim();
    }

    // 转Base64
    public static String EncryptBase64(String str) {
        byte[] b = null;
        String s = null;
        try {
            b = str.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (b != null) {
            s = Base64.encode(b);
        }
        return s;
    }

    /**
     * 对指定字符串进行md5加密
     *
     * @param s
     * @return 加密后的数据
     */
    public static String EncryptMD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> List<T> transform(List<T> list) {
        if (list != null && list.size() > 0) {
            return list;
        }
        return Collections.<T>emptyList();
    }

    /**
     * @param target
     * @param minimum
     * @return
     */
    public static int sub(int target, int minimum) {
        return target > minimum ? target - 1 : minimum;
    }


    public static boolean isFirstInstall(boolean replace) {
        final boolean isInstall = SPUtils.getInstance(SAVE_SETTINGS_NAME).getBoolean("isInstall", true);
        if (isInstall && replace) {
            SPUtils.getInstance(SAVE_SETTINGS_NAME).put("isInstall", false);
        }
        return isInstall;
    }

    public static String getLocalIpAddress(Context mtx) {
        try {
            final NetworkUtils.NetworkType networkType = NetworkUtils.getNetworkType();
            if (networkType == NETWORK_WIFI) {
                WifiManager wifiManager = (WifiManager) mtx.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                int i = wifiInfo.getIpAddress();
                return int2ip(i);
            } else if (networkType != NETWORK_UNKNOWN && networkType != NETWORK_NO) {
                String ipv4;
                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                    NetworkInterface intf = en.nextElement();
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ipv4 = inetAddress.getHostAddress())) {
                            return inetAddress.getHostAddress().toString();
                        }
                    }
                }
            }

        } catch (SocketException ex) {
        }
        return null;
    }

    /**
     * 将ip的整数形式转换成ip形式
     *
     * @param ipInt
     * @return
     */
    public static String int2ip(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }


    public static boolean clazzNameEquals(Class clazz1, Class clazz2) {
        if (clazz1 == null || clazz1 == null)
            return false;
        return TextUtils.equals(clazz1.getSimpleName(), clazz2.getSimpleName());
    }


    private static boolean adjustIntercept(ISupportFragment fragment, Class[] classes) {
        boolean intercept = false;
        for (Class interceptFragment : classes) {
            if (HttpsUtils.clazzNameEquals(fragment.getClass(), interceptFragment)) {
                intercept = true;
                break;
            }
        }
        return intercept;
    }


}
