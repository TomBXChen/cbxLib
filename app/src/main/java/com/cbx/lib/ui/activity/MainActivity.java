package com.cbx.lib.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cbx.lib.R;
import com.cbx.lib.http.home.AdPageListRq;
import com.taohdao.base.BasicsImplActivity;
import com.taohdao.http.BasicsResponse;
import com.taohdao.library.GlobalConfig;

import butterknife.BindView;

public class MainActivity extends BasicsImplActivity {


    @BindView(R.id.request)
    Button request;
    @BindView(R.id.body)
    TextView body;

    @Override
    protected void initImmersionBar() {
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        addRxClick(request);
    }

    @Override
    public void onRxClick(View view) {
        switch (view.getId()){
            case R.id.request:
                mPresenter.requestJson(new AdPageListRq(),false, GlobalConfig.LOAD);
                break;
        }
    }

    @Override
    public void callback(BasicsResponse response, int tag, Object object) {
        try{
            switch (tag){
                case GlobalConfig.LOAD:
                    body.setText(response.getFullData());
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
