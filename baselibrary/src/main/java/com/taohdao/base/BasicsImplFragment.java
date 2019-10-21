package com.taohdao.base;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.jess.arms.di.component.AppComponent;
import com.taohdao.di.component.DaggerBasicsComponent;
import com.taohdao.di.module.BasicsModule;
import com.taohdao.http.mvp.interfaces.IBasics;
import com.taohdao.http.mvp.persenter.BasicsPresenterImpl;
import com.taohdao.library.common.widget.topbar.THDTopBar;
import com.taohdao.widget.AbsDialogView;
import com.taohdao.widget.THDLoadingDialog;

import static com.taohdao.http.utils.HttpsUtils.checkNull;

/**
 * Created by admin on 2018/3/16.
 */

public abstract class BasicsImplFragment extends BaseFragment<BasicsPresenterImpl> implements IBasics.View  {

    private AbsDialogView mAbsDialogView;

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerBasicsComponent.builder()
                .appComponent(appComponent)
                .basicsModule(new BasicsModule(this))
                .build()
                .inject(this);
        mAbsDialogView = createDialogView();
    }

    protected AbsDialogView createDialogView(){
        return new THDLoadingDialog();
    }
    @Override
    public void showLoading() {
        if(mAbsDialogView!=null) mAbsDialogView.showDialog();
    }

    @Override
    public void hideLoading() {
        if(mAbsDialogView!=null) mAbsDialogView.dismiss();
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void launchActivity(Intent intent) {

    }

    @Override
    public void onDestroy() {
        mAbsDialogView = null;
        super.onDestroy();
    }

    @Override
    public void killMyself() {

    }

    public void initTopBar(THDTopBar topBar, String title) {
        initTopBar(topBar, title, null);
    }

    public void initTopBar(THDTopBar topBar, String title, String suString) {
        if (topBar != null) {
            topBar.setTitle(checkNull(title));
            if (!TextUtils.isEmpty(suString)) {
                topBar.setSubTitle(checkNull(suString));
            } else {
                topBar.setTitleGravity(Gravity.CENTER);
            }
            topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    backPop();
                    pop();
                }
            });
        }
    }

    protected void backPop(){

    }

}
