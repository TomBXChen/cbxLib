package com.taohdao.library.widget.linkage.contract;
/*
 * Copyright (c) 2018-2019. KunMinX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.content.Context;
import android.view.View;

import com.taohdao.library.widget.linkage.adapter.viewholder.LinkagePrimaryViewHolder;


/**
 * Create by KunMinX at 19/5/8
 */
public interface ILinkagePrimaryAdapterConfig {

    /**
     * setContext
     *
     * @param context context
     */
    void setContext(Context context);

    /**
     * get layouts res id
     *
     * @return layouts res id
     */
    int getLayoutId();

    /**
     * get textView id of layouts
     *
     * @return textView id of layouts
     */
    int getGroupTitleViewId();

    /**
     * get rootView id of layouts
     *
     * @return rootView id of layouts
     */
    int getRootViewId();

    /**
     * achieve the onBindViewHolder logic on outside
     * <p>
     * Note: Do not setOnClickListener in onBindViewHolder,
     * instead, you can deal with item click in method 'ILinkagePrimaryAdapterConfig.onItemSelected()'
     * or 'LinkageRecyclerView.OnPrimaryItemClickListener.onItemClick()'
     *
     * @param holder   LinkagePrimaryViewHolder
     * @param title    title of this position
     * @param selected selected of this position
     * @param position holder.getAdapterPosition()
     */
    void onBindViewHolder(LinkagePrimaryViewHolder holder, boolean selected, String title, int position);

    /**
     * on primary item clicked
     *
     * @param view     itemView
     * @param title    title of primary item
     * @param position position
     */
    void onItemClick(View view, String title, int position);
}
