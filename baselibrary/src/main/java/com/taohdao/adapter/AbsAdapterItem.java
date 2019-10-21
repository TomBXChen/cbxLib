package com.taohdao.adapter;

import android.view.View;

import butterknife.ButterKnife;
import kale.adapter.item.AdapterItem;

/**
 * Created by admin on 2019/4/9.
 */

public abstract class AbsAdapterItem<T> implements AdapterItem<T> {

    protected T mBean;
    protected int position;

    @Override
    public void bindViews(View root) {
        ButterKnife.bind(this, root);
    }

    @Override
    public void handleData(T t, int i) {
        this.mBean = t;
        this.position = i;
    }
}
