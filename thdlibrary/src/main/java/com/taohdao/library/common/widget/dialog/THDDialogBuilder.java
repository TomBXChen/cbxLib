package com.taohdao.library.common.widget.dialog;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.taohdao.library.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/3/20.
 */

public abstract class THDDialogBuilder <T extends THDDialogBuilder> {
    protected Context mContext;
    protected THDDialog mDialog;
    protected LayoutInflater mInflater;
    protected String mTitle;

    protected LinearLayout mRootView;
    protected LinearLayout mDialogWrapper;
    protected View mAnchorTopView;
    protected View mAnchorBottomView;
    protected List<THDDialogAction> mActions = new ArrayList<>();
    protected THDDialogAction mLeftAction;

    protected TextView mTitleView;
    protected LinearLayout mActionContainer;

    public THDDialogBuilder(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    /**
     * 设置对话框顶部的标题文字
     */
    public T setTitle(String title) {
        if (title != null && title.length() > 0) {
            this.mTitle = title + mContext.getString(R.string.thd_tool_fixellipsize);
        }
        return (T) this;
    }

    /**
     * 设置对话框顶部的标题文字
     */
    public T setTitle(int resId) {
        return setTitle(mContext.getResources().getString(resId));
    }

    //region 添加action

    /**
     * 添加对话框底部的操作按钮
     */
    public T addAction(@Nullable THDDialogAction action) {
        if (action != null) {
            mActions.add(action);
        }

        return (T) this;
    }

    /**
     * 添加无图标正常类型的操作按钮
     *
     * @param strResId 文案
     * @param listener 点击回调事件
     */
    public T addAction(int strResId, THDDialogAction.ActionListener listener) {
        return addAction(0, strResId, listener);
    }

    /**
     * 添加无图标正常类型的操作按钮
     *
     * @param str      文案
     * @param listener 点击回调事件
     */
    public T addAction(String str, THDDialogAction.ActionListener listener) {
        return addAction(0, str, listener);
    }

    /**
     * 添加普通类型的操作按钮
     *
     * @param iconResId 图标
     * @param strResId  文案
     * @param listener  点击回调事件
     */
    public T addAction(int iconResId, int strResId, THDDialogAction.ActionListener listener) {
        return addAction(iconResId, strResId, THDDialogAction.ACTION_PROP_NEUTRAL, listener);
    }

    /**
     * 添加普通类型的操作按钮
     *
     * @param iconResId 图标
     * @param str       文案
     * @param listener  点击回调事件
     */
    public T addAction(int iconResId, String str, THDDialogAction.ActionListener listener) {
        return addAction(iconResId, str, THDDialogAction.ACTION_PROP_NEUTRAL, listener);
    }

    /**
     * 添加普通类型的操作按钮
     *
     * @param iconRes  图标
     * @param strRes   文案
     * @param prop     属性
     * @param listener 点击回调事件
     */
    public T addAction(int iconRes, int strRes, @THDDialogAction.Prop int prop, THDDialogAction.ActionListener listener) {
        return addAction(iconRes, mContext.getResources().getString(strRes), prop, listener);
    }

    /**
     * 添加普通类型的操作按钮
     *
     * @param iconRes  图标
     * @param str      文案
     * @param prop     属性
     * @param listener 点击回调事件
     */
    public T addAction(int iconRes, String str, @THDDialogAction.Prop int prop, THDDialogAction.ActionListener listener) {
        return addAction(iconRes, str, prop, THDDialogAction.ACTION_TYPE_NORMAL, listener);
    }

    /**
     * 添加普通类型的操作按钮
     *
     * @param iconRes  图标
     * @param strRes   文案
     * @param type     类型
     * @param prop     属性
     * @param listener 点击回调事件
     */
    protected T addAction(int iconRes, int strRes, @THDDialogAction.Prop int prop, @THDDialogAction.Type int type, THDDialogAction.ActionListener listener) {
        return addAction(iconRes, mContext.getResources().getString(strRes), prop, type, listener);
    }

    /**
     * 添加普通类型的操作按钮
     *
     * @param iconRes  图标
     * @param str      文案
     * @param type     类型
     * @param prop     属性
     * @param listener 点击回调事件
     */
    protected T addAction(int iconRes, String str, @THDDialogAction.Prop int prop, @THDDialogAction.Type int type, THDDialogAction.ActionListener listener) {
        THDDialogAction action = new THDDialogAction(mContext, iconRes, str, type, prop, listener);
        mActions.add(action);
        return (T) this;
    }

    public THDDialogAction setLeftAction(String str, THDDialogAction.ActionListener listener) {
        return setLeftAction(0, str, listener);
    }

    public THDDialogAction setLeftAction(int iconRes, String str, THDDialogAction.ActionListener listener) {
        return setLeftAction(iconRes, str, THDDialogAction.ACTION_PROP_NEUTRAL, listener);
    }


    public THDDialogAction setLeftAction(int iconRes, String str, @THDDialogAction.Prop int prop, THDDialogAction.ActionListener listener) {
        mLeftAction = new THDDialogAction(mContext, iconRes, str, THDDialogAction.ACTION_TYPE_NORMAL, prop, listener);
        return mLeftAction;
    }

    //endregion

    /**
     * 判断对话框是否需要显示title
     *
     * @return 是否有title
     */
    protected boolean hasTitle() {
        return mTitle != null && mTitle.length() != 0;
    }

    /**
     * 产生一个 Dialog 并显示出来
     */
    public THDDialog show() {
        final THDDialog dialog = create();
        dialog.show();
        return dialog;
    }

    /**
     * 只产生一个 Dialog, 不显示出来
     *
     * @see #create(int)
     */
    public THDDialog create() {
        return create(R.style.THD_Dialog);
    }

    /**
     * 产生一个Dialog，但不显示出来。
     *
     * @param style Dialog 的样式
     * @see #create()
     */
    public THDDialog create(@StyleRes int style) {
        mDialog = new THDDialog(mContext, style);

        mRootView = (LinearLayout) mInflater.inflate(
                R.layout.thd_dialog_layout, null);
        mDialogWrapper = (LinearLayout) mRootView.findViewById(R.id.dialog);
        mAnchorTopView = mRootView.findViewById(R.id.anchor_top);
        mAnchorBottomView = mRootView.findViewById(R.id.anchor_bottom);

        // title
        onCreateTitle(mDialog, mDialogWrapper);

        //content
        onCreateContent(mDialog, mDialogWrapper);

        // 操作
        onCreateHandlerBar(mDialog, mDialogWrapper);


        mDialog.addContentView(mRootView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        onAfter(mDialog, mRootView);
        return mDialog;
    }

    /**
     * 创建顶部的标题区域
     */
    protected void onCreateTitle(THDDialog dialog, ViewGroup parent) {
        if (hasTitle()) {
            mTitleView = new TextView(mContext);
            mTitleView.setSingleLine(true);
            mTitleView.setEllipsize(TextUtils.TruncateAt.END);
            mTitleView.setText(mTitle);
            mTitleView.setTextColor(THDResHelper.getAttrColor(mContext, R.attr.thd_dialog_title_text_color));
            mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, THDResHelper.getAttrDimen(mContext, R.attr.thd_dialog_title_text_size));
            mTitleView.setPadding(
                    THDResHelper.getAttrDimen(mContext, R.attr.thd_dialog_padding_horizontal),
                    THDResHelper.getAttrDimen(mContext, R.attr.thd_dialog_title_margin_top),
                    THDResHelper.getAttrDimen(mContext, R.attr.thd_dialog_padding_horizontal),
                    0
            );
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mTitleView.setLayoutParams(lp);
            parent.addView(mTitleView);
        }
    }

    public TextView getTitleView() {
        return mTitleView;
    }

    /**
     * 创建中间的区域
     */
    protected abstract void onCreateContent(THDDialog dialog, ViewGroup parent);

    /**
     * 创建底部的操作栏区域
     */
    protected void onCreateHandlerBar(final THDDialog dialog, ViewGroup parent) {
        int size = mActions.size();
        if (size > 0 || mLeftAction != null) {
            mActionContainer = new LinearLayout(mContext);
            mActionContainer.setOrientation(LinearLayout.HORIZONTAL);
            mActionContainer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mActionContainer.setPadding(
                    THDResHelper.getAttrDimen(mContext, R.attr.thd_dialog_action_container_margin_horizontal),
                    0,
                    THDResHelper.getAttrDimen(mContext, R.attr.thd_dialog_action_container_margin_horizontal),
                    THDResHelper.getAttrDimen(mContext, R.attr.thd_dialog_action_container_margin_bottom));
            if (mLeftAction != null) {
                mActionContainer.addView(mLeftAction.generateActionView(mContext, mDialog, 0, false));
            }
            View space = new View(mContext);
            LinearLayout.LayoutParams spaceLp = new LinearLayout.LayoutParams(0, 0);
            spaceLp.weight = 1;
            space.setLayoutParams(spaceLp);
            mActionContainer.addView(space);

            for (int i = 0; i < size; i++) {
                THDDialogAction action = mActions.get(i);
                mActionContainer.addView(action.generateActionView(mContext, mDialog, i, true));
            }

            mActionContainer.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    int width = right - left;
                    int childCount = mActionContainer.getChildCount();
                    if (childCount > 0) {
                        View lastChild = mActionContainer.getChildAt(childCount - 1);
                        // 如果ActionButton的宽度过宽，则减小padding
                        if (lastChild.getRight() > width) {
                            int childPaddingHor = Math.max(0, lastChild.getPaddingLeft() - SizeUtils.dp2px(3));
                            for (int i = 0; i < childCount; i++) {
                                mActionContainer.getChildAt(i).setPadding(childPaddingHor, 0, childPaddingHor, 0);
                            }
                        }
                    }

                }
            });
            parent.addView(mActionContainer);

        }
    }

    protected void onAfter(THDDialog dialog, LinearLayout parent) {
        //默认情况下，点击anchorView使得dialog消失
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        };
        mAnchorBottomView.setOnClickListener(listener);
        mAnchorTopView.setOnClickListener(listener);
    }

    public View getAnchorTopView() {
        return mAnchorTopView;
    }

    public View getAnchorBottomView() {
        return mAnchorBottomView;
    }

    public List<THDDialogAction> getPositiveAction() {
        List<THDDialogAction> output = new ArrayList<>();
        for (THDDialogAction action : mActions) {
            if (action.getActionProp() == THDDialogAction.ACTION_PROP_POSITIVE) {
                output.add(action);
            }
        }
        return output;
    }
}
