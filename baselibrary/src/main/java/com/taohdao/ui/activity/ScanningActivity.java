package com.taohdao.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.journeyapps.barcodescanner.BarcodeSucceedCallback;
import com.journeyapps.barcodescanner.CaptureCyanManager;
import com.journeyapps.barcodescanner.CyanBarcodeView;
import com.orhanobut.logger.Logger;
import com.taohdao.base.BaseEvent;
import com.taohdao.base.BasicsImplActivity;
import com.taohdao.base.R;
import com.taohdao.http.BasicsResponse;
import com.taohdao.library.common.widget.topbar.THDTopBar;
import com.taohdao.library.utils.EventBusUtils;

/**
 * Created by admin on 2019/5/15.
 */

public class ScanningActivity extends BasicsImplActivity implements BarcodeSucceedCallback {

    public static final int SCAN_TYPE_STOREIDS = 1 << 1;
    public static final int SCAN_TYPE_CALL_BACK = 1 << 2;
    private static final String SCAN_TYPE = "scanType";
    private static final String EVEN_CALL_NAME = "evenCallName";

    public static void start(Context mCtx, int scanType, String evenCallName) {
        Intent intent = new Intent();
        intent.setClass(mCtx, ScanningActivity.class);
        intent.putExtra(SCAN_TYPE, scanType);
        intent.putExtra(EVEN_CALL_NAME, evenCallName);
        mCtx.startActivity(intent);
    }

    THDTopBar topbar;
    CyanBarcodeView barcodeScannerView;
    public int scanType = SCAN_TYPE_STOREIDS;
    public String taskno;//出售设备任务会带任务编号过来

    private CaptureCyanManager capture;

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_base_scanning;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        topbar = findViewById(R.id.topbar);
        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner);
        initTopBar(topbar, "二维码/条形码");
        scanType = getIntent().getIntExtra(SCAN_TYPE,SCAN_TYPE_CALL_BACK);
        capture = new CaptureCyanManager(this, barcodeScannerView, this);
        capture.decodeContinuous();

    }


    @Override
    public void barcodeSucceed(String result) {
        Logger.e(result);
        try {
            if (!TextUtils.isEmpty(result)) {
                switch (scanType) {
                    case SCAN_TYPE_CALL_BACK:
                        BaseEvent.ActionEvent callBack = BaseEvent.ActionEvent.CALL_BACK;
                        callBack.setObject(result);
                        EventBusUtils.post(callBack,getIntent().getStringExtra(EVEN_CALL_NAME));
                        break;
                }
//                String callId = CCUtil.getNavigateCallId(this);
//                if (callId != null) {
//                    final CCResult ccResult = CCResult.success("result", result);
//                    CC.sendCCResult(callId, ccResult);
//                    finish();
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            capture.barcodeResume();
        }

    }

    @Override
    public void callback(BasicsResponse response, int tag, Object object) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }
}
