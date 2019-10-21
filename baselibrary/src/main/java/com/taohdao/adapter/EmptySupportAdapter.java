package com.taohdao.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taohdao.base.R;

import java.util.List;

/**
 * Created by admin on 2019/4/12.
 */

public abstract class EmptySupportAdapter extends CommonRecQuickAdapter {

    public EmptySupportAdapter(List data) {
        super(data);

    }

    public void useDefaultEmptyView(RecyclerView recyclerView, View.OnClickListener onClickListener){
        View view = LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.layout_empty_view_default, (ViewGroup)recyclerView.getParent(), false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener!=null){
                    onClickListener.onClick(v);
                }
            }
        });
        setEmptyView(view);
    }

    public void useDefaultLoadingView(RecyclerView recyclerView){
        setEmptyView(R.layout.layout_loading_view_default,(ViewGroup)recyclerView.getParent());
    }


}
