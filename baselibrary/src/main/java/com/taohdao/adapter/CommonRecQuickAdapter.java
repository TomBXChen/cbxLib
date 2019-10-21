package com.taohdao.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.HashMap;
import java.util.List;

import kale.adapter.item.AdapterItem;
import kale.adapter.util.IAdapter;
import kale.adapter.util.ItemTypeUtil;

/**
 * Created by admin on 2018/3/14.
 */

public abstract class CommonRecQuickAdapter<T> extends BaseQuickAdapter<T,CommonRecQuickAdapter.CommonRecQuickBaseView> implements IAdapter<T> {
    private Object mType;

    private ItemTypeUtil mUtil;

    private int currentPos;

    public CommonRecQuickAdapter(List<T> data) {
        super(data);
        mUtil = new ItemTypeUtil();
    }
    /**
     * 配合RecyclerView的pool来设置TypePool
     * @param typePool
     */
    public void setTypePool(HashMap<Object, Integer> typePool) {
        mUtil.setTypePool(typePool);
    }

    @Override
    protected CommonRecQuickBaseView onCreateDefViewHolder(ViewGroup parent, int viewType) {
        final AdapterItem item = createItem(mType);
        return new CommonRecQuickBaseView(LayoutInflater.from(parent.getContext()).inflate(item.getLayoutResId(), parent, false)).init(item);
    }

    @Override
    public void onBindViewHolder(CommonRecQuickBaseView holder, int position) {
        super.onBindViewHolder(holder, position);
        final int itemViewType = holder.getItemViewType();
        switch (itemViewType){
            case 0:
                convert(holder,getItem(position - getHeaderLayoutCount()),holder.getLayoutPosition()-getHeaderLayoutCount());
                break;
            case LOADING_VIEW:
                break;
            case HEADER_VIEW:
                break;
            case EMPTY_VIEW:
                break;
            case FOOTER_VIEW:
                break;
            default:
                convert(holder,getItem(position - getHeaderLayoutCount()),holder.getLayoutPosition()-getHeaderLayoutCount());
        }
    }

    protected void convert(CommonRecQuickBaseView helper, T item) {
//        helper.item.handleData(getConvertedData(item, mType), -1);
    }
    protected void convert(CommonRecQuickBaseView helper, T item,int position) {
        helper.item.handleData(getConvertedData(item, mType), position);
    }

    @Override
    protected int getDefItemViewType(int position) {
        this.currentPos = position;
        mType = getItemType(getData().get(position));
        return mUtil.getIntType(mType);
    }


    @Override
    public Object getItemType(T t) {
        return -1; // default
    }


    @Override
    public void setData(@NonNull List<T> data) {

    }

    @NonNull
    @Override
    public Object getConvertedData(T data, Object type) {
        return data;
    }

    @Override
    public int getCurrentPosition() {
        return currentPos;
    }

    public static class CommonRecQuickBaseView extends BaseViewHolder {

        protected AdapterItem item;

        boolean isNew = true; // debug中才用到

        public CommonRecQuickBaseView(View view) {
            super(view);
//            setEnabledClick(false);

        }
        public CommonRecQuickBaseView init(AdapterItem item){
            this.item = item;
            this.item.bindViews(itemView);
//            AutoUtils.autoSize(itemView);
            this.item.setViews();
            return this;
        }
    }
}