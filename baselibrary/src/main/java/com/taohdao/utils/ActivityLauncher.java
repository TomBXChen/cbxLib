package com.taohdao.utils;

import android.content.Context;
import android.content.Intent;

import com.taohdao.ui.activity.ScanningActivity;

public class ActivityLauncher {

    public static void start(Context mCtx, int scanType, String evenCallName){
        ScanningActivity.start(mCtx,scanType,evenCallName);
    }
}
