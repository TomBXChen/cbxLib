package com.taohdao.library.common.widget.popup;

import android.content.Context;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.taohdao.library.common.widget.THDWrapContentListView;

/**
 * 继承自 {@link THDPopup}，在 {@link THDPopup} 的基础上，支持显示一个列表。
 *
 * @author cginechen
 * @date 2016-11-16
 */

public class THDListPopup extends THDPopup {
    private BaseAdapter mAdapter;

    /**
     * 构造方法。
     *
     * @param context   传入一个 Context。
     * @param direction Popup 的方向，为 {@link THDPopup#DIRECTION_NONE}, {@link THDPopup#DIRECTION_TOP} 和 {@link THDPopup#DIRECTION_BOTTOM} 中的其中一个值。
     * @param adapter   列表的 Adapter
     */
    public THDListPopup(Context context, @Direction int direction, BaseAdapter adapter) {
        super(context, direction);
        mAdapter = adapter;
    }

    public void create(int width, int maxHeight, AdapterView.OnItemClickListener onItemClickListener) {
        ListView listView = new THDWrapContentListView(mContext, maxHeight);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, maxHeight);
        listView.setLayoutParams(lp);
        listView.setAdapter(mAdapter);
        listView.setVerticalScrollBarEnabled(false);
        listView.setOnItemClickListener(onItemClickListener);
        listView.setDivider(null);
        setContentView(listView);
    }
}
