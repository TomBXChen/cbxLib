package com.taohdao.base;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.mvp.IPresenter;
import com.jess.arms.mvp.IView;
import com.taohdao.widget.AbsDialogView;
import com.taohdao.widget.THDLoadingDialog;

public abstract class LQBaseActivity<P extends IPresenter> extends  BaseActivity<P> implements IView {

    private AbsDialogView mAbsDialogView ;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        mAbsDialogView = new THDLoadingDialog();
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
    public void showMessage(@NonNull String message) {

    }

    @Override
    public void launchActivity(@NonNull Intent intent) {

    }

    @Override
    public void killMyself() {
        finish();
    }
}
