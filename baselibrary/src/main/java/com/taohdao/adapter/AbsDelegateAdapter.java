package com.taohdao.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by admin on 2019/4/9.
 */

public abstract class AbsDelegateAdapter<T> implements DelegateAdapterItem<T> {

    protected T mBean;
    protected int currentPosition;

    @Override
    public void bindViews(View root) {
        ButterKnife.bind(this, root);
    }

    @Override
    public void handleDataWithOffset(T t, int position, int offsetTotal) {

    }

    @Override
    public void handleData(RecyclerView.ViewHolder holder, T t, int position) {
        this.mBean = t;
        this.currentPosition = position;
    }


}
