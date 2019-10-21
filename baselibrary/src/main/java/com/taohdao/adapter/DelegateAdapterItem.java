package com.taohdao.adapter;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Adapter的所有item必须实现的接口.<br>
 * 
 * 通过{@link #getLayoutResId()}初始化view;<br>
 * 在{@link #bindViews(View)}中就初始化item的内部视图<br>
 * 在{@link #handleData(RecyclerView.ViewHolder, Object, int)}中处理每一行的数据<p>
 * 
 * @author Jack Tony
 * @date 2015/5/15
 */
public interface DelegateAdapterItem<T> {

    /**
     * @return item布局文件的layoutId
     */
    @LayoutRes
    int getLayoutResId();

    /**
     * 初始化views
     */
    void bindViews(final View root);

    /**
     * 设置view的参数
     */
    void setViews();


    /**
     * 根据数据来设置item的内部views
     *
     * @param holder
     * @param t    数据list内部的model
     * @param position 当前adapter调用item的位置
     */
    void handleData(RecyclerView.ViewHolder holder, T t, int position);


    void handleDataWithOffset(T t, int position, int offsetTotal);


}  