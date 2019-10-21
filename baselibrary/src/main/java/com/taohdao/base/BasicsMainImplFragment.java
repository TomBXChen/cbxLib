package com.taohdao.base;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.barlibrary.ImmersionOwner;
import com.gyf.barlibrary.ImmersionProxy;
import com.taohdao.http.BasicsResponse;

public abstract class BasicsMainImplFragment extends BasicsImplFragment implements ImmersionOwner {

    private ImmersionProxy mImmersionProxy = new ImmersionProxy(this);
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mImmersionProxy.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImmersionProxy.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mImmersionProxy.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mImmersionProxy.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mImmersionProxy.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImmersionProxy.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mImmersionProxy.onHiddenChanged(hidden);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mImmersionProxy.onConfigurationChanged(newConfig);
    }

    /**
     * 懒加载，在view初始化完成之前执行
     * On lazy after view.
     */
    @Override
    public void onLazyBeforeView() {
    }

    /**
     * 懒加载，在view初始化完成之后执行
     * On lazy before view.
     */
    @Override
    public void onLazyAfterView() {
    }

    /**
     * Fragment用户可见时候调用
     * On visible.
     */
    @Override
    public void onVisible() {
    }

    /**
     * Fragment用户不可见时候调用
     * On invisible.
     */
    @Override
    public void onInvisible() {
    }

    @Override
    public void initImmersionBar() {
        ImmersionBar.with(this)
                .keyboardEnable(true).navigationBarWithKitkatEnable(false)
                .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true, 0.2f).init();
    }

    /**
     * 是否可以实现沉浸式，当为true的时候才可以执行initImmersionBar方法
     * Immersion bar enabled boolean.
     *
     * @return the boolean
     */
    @Override
    public boolean immersionBarEnabled() {
        return true;
    }

}
