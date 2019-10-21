package com.taohdao.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.LayoutViewFactory;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.taohdao.adapter.DelegateAdapterItem;
import com.taohdao.adapter.DelegateRecAdapter;

import java.util.List;

/**
 * Created by admin on 2018/3/28.
 */

public class RecyclerViewHelper {

    /**
     * 初始化使用Vlayout的RecyclerView
     * @param mContext
     * @param mRecyclerView
     * @param useBackground
     * @return 返回Vlayout自带的adapter
     */
    public static DelegateAdapter initVirtualLayoutManager(Context mContext, RecyclerView mRecyclerView, boolean useBackground){
        final VirtualLayoutManager layoutManager = new VirtualLayoutManager(mContext);

        if(useBackground){
            layoutManager.setLayoutViewFactory(new LayoutViewFactory() {
                @Override
                public View generateLayoutView(@NonNull Context context) {
                    return new AppCompatImageView(context);
                }
            });
        }

        mRecyclerView.setLayoutManager(layoutManager);
        return new DelegateAdapter(layoutManager, true);
    }




    public static final int ITEM_TYPE_LIST = 2;
    public static final int ITEM_TYPE_OTHER = 555;

    public static <T> DelegateRecAdapter createListAdapter(List<T> mList, OnItemCreate mOnItemCreate) {
        if (mOnItemCreate == null) {
            throw new NullPointerException("OnItemCreate must be not null!");
        }
        final LayoutHelper layoutHelper = mOnItemCreate.createLayoutHelper();

        return new DelegateRecAdapter<T>(mList, layoutHelper == null ? new LinearLayoutHelper() : layoutHelper) {
            @NonNull
            @Override
            public DelegateAdapterItem createItem(Object type) {
                return mOnItemCreate.createItem();
            }

            @Override
            public int getItemType(T o) {
                return mOnItemCreate.getItemType() != -1 ? mOnItemCreate.getItemType() : ITEM_TYPE_LIST;
            }
        };
    }

    public static DelegateRecAdapter createAdapter(Object obj, OnItemCreate mOnItemCreate) {
        if (mOnItemCreate == null) {
            throw new NullPointerException("OnItemCreate must be not null!");
        }
        final LayoutHelper layoutHelper = mOnItemCreate.createLayoutHelper();
        return new DelegateRecAdapter(Pair.create(1, obj), layoutHelper == null ? new LinearLayoutHelper() : layoutHelper) {
            @NonNull
            @Override
            public DelegateAdapterItem createItem(Object type) {
                return mOnItemCreate.createItem();
            }

            @Override
            public int getItemType(Object o) {
                return ITEM_TYPE_OTHER;
            }
        };
    }

    public static <T> DelegateRecAdapter createListAdapter(List<T> mList, LayoutHelper mLayoutHelper, OnItemCreate mOnItemCreate) {
        if (mOnItemCreate == null) {
            throw new NullPointerException("OnItemCreate must be not null!");
        }
        return new DelegateRecAdapter<T>(mList, mLayoutHelper) {
            @NonNull
            @Override
            public DelegateAdapterItem createItem(Object type) {
                return mOnItemCreate.createItem();
            }

            @Override
            public int getItemType(T o) {
                return ITEM_TYPE_LIST;
            }

        };
    }


    public static DelegateRecAdapter createItem(Object obj, OnItemCreate mOnItemCreate) {
        if (mOnItemCreate == null) {
            throw new NullPointerException("OnItemCreate must be not null!");
        }
        final LayoutHelper layoutHelper = mOnItemCreate.createLayoutHelper();

        return new DelegateRecAdapter(Pair.create(1, obj), layoutHelper) {
            @NonNull
            @Override
            public DelegateAdapterItem createItem(Object type) {
                return mOnItemCreate.createItem();
            }

            @Override
            public int getItemType(Object o) {
                return mOnItemCreate.getItemType();
            }
        };
    }

    public interface OnItemCreate {
        DelegateAdapterItem createItem();

        LayoutHelper createLayoutHelper();

        int getItemType();
    }
}
