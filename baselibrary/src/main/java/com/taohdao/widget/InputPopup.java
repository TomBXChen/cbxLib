package com.taohdao.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.ConfirmPopupView;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.util.XPopupUtils;
import com.taohdao.base.R;

/**
 * Created by admin on 2019/4/22.
 */

public class InputPopup extends ConfirmPopupView implements View.OnClickListener {

    public InputPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout._xpopup_center_impl_confirm;
    }

    AppCompatEditText tv_input;
    private String content;

    @Override
    protected void initPopupContent() {
        tv_input = findViewById(R.id.tv_input);
        tv_input.setVisibility(VISIBLE);
        tv_input.setText(TextUtils.isEmpty(content)?"":content);
        tv_input.setSelection(tv_input.getText().length());
        super.initPopupContent();
    }

    public void setFillContent(String content){
        this.content = content;
    }

    protected void applyPrimaryColor() {
        super.applyPrimaryColor();
        XPopupUtils.setCursorDrawableColor(tv_input, XPopup.getPrimaryColor());
        tv_input.post(new Runnable() {
            @Override
            public void run() {
                BitmapDrawable defaultDrawable = XPopupUtils.createBitmapDrawable(getResources(), tv_input.getMeasuredWidth(), Color.parseColor("#888888"));
                BitmapDrawable focusDrawable = XPopupUtils.createBitmapDrawable(getResources(), tv_input.getMeasuredWidth(), XPopup.getPrimaryColor());
                tv_input.setBackgroundDrawable(XPopupUtils.createSelector(defaultDrawable, focusDrawable));
            }
        });

    }

    OnCancelListener cancelListener;
    OnInputConfirmListener inputConfirmListener;

    public void setListener(OnInputConfirmListener inputConfirmListener, OnCancelListener cancelListener) {
        this.cancelListener = cancelListener;
        this.inputConfirmListener = inputConfirmListener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == com.lxj.xpopup.R.id.tv_cancel) {
            if (cancelListener != null) cancelListener.onCancel();
            dismiss();
        } else if (v.getId() == com.lxj.xpopup.R.id.tv_confirm) {
            if (inputConfirmListener != null)
                inputConfirmListener.onConfirm(tv_input.getText().toString().trim());
            if (popupInfo.autoDismiss) dismiss();
        }
    }
}
