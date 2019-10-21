package com.taohdao.base;

import android.app.Activity;
import android.content.Intent;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.taohdao.di.component.DaggerBasicsComponent;
import com.taohdao.di.module.BasicsModule;
import com.taohdao.http.mvp.interfaces.IBasics;
import com.taohdao.http.mvp.persenter.BasicsPresenterImpl;
import com.taohdao.widget.AbsDialogView;
import com.taohdao.widget.THDLoadingDialog;

/**
 * Created by admin on 2018/3/15.
 */

public abstract class BasicsImplActivity extends BaseActivity<BasicsPresenterImpl> implements IBasics.View {


    private AbsDialogView mAbsDialogView;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerBasicsComponent.builder()
                .appComponent(appComponent)
                .basicsModule(new BasicsModule(this))
                .build().inject(this);
        mAbsDialogView =  createDialogView();
    }

    protected AbsDialogView createDialogView(){
        return new THDLoadingDialog();
    }

    @Override
    public void showLoading() {
        if(mAbsDialogView !=null)mAbsDialogView.showDialog();
    }

    @Override
    public void hideLoading() {
        if(mAbsDialogView !=null) mAbsDialogView.dismiss();
    }

    @Override
    public void showMessage(String message) {
        ArmsUtils.makeText(this,message);
    }

    @Override
    public void launchActivity(Intent intent) {

    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAbsDialogView = null;
    }
}
