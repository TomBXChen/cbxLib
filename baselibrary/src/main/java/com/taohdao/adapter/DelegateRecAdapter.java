package com.taohdao.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.jess.arms.utils.ArmsUtils;
import com.taohdao.base.BaseApp;

import java.util.List;

import me.jessyan.autosize.AutoSize;


/**
 * Created by admin on 2017/3/20.
 */

public abstract class DelegateRecAdapter<T> extends DelegateAdapter.Adapter implements IDelegateAdapter<T> {

    private List<T> mDataList;

    private int mType;

    private LayoutHelper mLayoutHelper;

    private Integer mCount;

    private Object mData;

    private DelegateRecAdapter(Pair<Pair<Integer,Object>,List<T>> data, LayoutHelper mLayoutHelper, boolean noting) {
        if (data == null) {
          throw new NullPointerException("DelegateRecAdapter : data must be not null");
        }
        Pair<Integer,Object> mFirstData =  data.first;
        if(mFirstData!=null){
            mCount = mFirstData.first;
            mData = mFirstData.second;
        }
        mDataList = data.second;
        if (mLayoutHelper == null) {
            this.mLayoutHelper = new LinearLayoutHelper();
        } else {
            this.mLayoutHelper = mLayoutHelper;
        }
    }

    public DelegateRecAdapter(@Nullable List<T>  data) {
        this(Pair.create(Pair.create((Integer)null,null), data),null,false);
    }
    public DelegateRecAdapter(@Nullable List<T>  data, LayoutHelper mLayoutHelper) {
        this(Pair.create(Pair.create((Integer)null,null), data),mLayoutHelper,false);
    }
    public DelegateRecAdapter(@Nullable Pair<Integer,Object> data, LayoutHelper mLayoutHelper) {
        this(Pair.create(data,(List<T>)null),mLayoutHelper,false);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLayoutHelper;
    }

    public LayoutHelper getLayoutHelper() {
        return mLayoutHelper;
    }

    public void setLayoutHelper(LayoutHelper mLayoutHelper) {
        this.mLayoutHelper = mLayoutHelper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RcvAdapterItem(parent.getContext(), parent, createItem(mType));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AutoSize.autoConvertDensityOfGlobal(ArmsUtils.obtainAppComponentFromContext(BaseApp.getInstance()).appManager().getCurrentActivity());
        ((RcvAdapterItem) holder).item.handleData(holder,mCount==null?getConvertedData(mDataList.get(position), mType):mData, position);
    }

    @Override
    protected void onBindViewHolderWithOffset(RecyclerView.ViewHolder holder, int position, int offsetTotal) {
        ((RcvAdapterItem) holder).item.handleDataWithOffset(mCount==null?getConvertedData(mDataList.get(position), mType):mData, position,offsetTotal);
    }

    @Override
    public int getItemCount() {
        return mCount!=null?mCount.intValue():mDataList.size();
    }

    public void setData(T mData) {
        this.mData = mData;
    }
    public Object getDefinedData(){
        return this.mData;
    }
    @Override
    public void setData(@NonNull List<T> data) {
        mDataList = data;
    }

    @Override
    public List<T> getData() {
        return mDataList;
    }

    @Deprecated
    @Override
    public int getItemViewType(int position) {
        mType = getItemType(mDataList!=null?mDataList.get(position):null);
        return mType;
    }

    @Override
    public int getItemType(T t) {
        return -1; // default
    }


    @NonNull
    @Override
    public Object getConvertedData(T data, Object type) {
        return data;
    }

    private static class RcvAdapterItem extends RecyclerView.ViewHolder {

        protected DelegateAdapterItem item;

        boolean isNew = true; // debug中才用到

        protected RcvAdapterItem(Context context, ViewGroup parent, DelegateAdapterItem item) {
            super(LayoutInflater.from(context).inflate(item.getLayoutResId(), parent, false));
            this.item = item;
            this.item.bindViews(itemView);
//            AutoUtils.autoSize(itemView);
            this.item.setViews();
        }
    }
}
