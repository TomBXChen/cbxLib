package com.taohdao.library.common.widget.popup;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.blankj.utilcode.util.SizeUtils;
import com.taohdao.library.R;
import com.taohdao.library.common.widget.CashTextWatcher;
import com.taohdao.library.common.widget.numberkeyboard.KeyboardUtil;
import com.taohdao.library.common.widget.numberkeyboard.NumberKeyBoardView;
import com.taohdao.library.common.widget.roundwidget.THDRoundButton;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


/**
 * Created by admin on 2018/11/5.
 */

public class THDInputPopup extends THDPopup implements View.OnClickListener {

    private OnConfirmClickListener onConfirmClickListener;
    private EditText _editTargetNum;
    private String hintStr;

    public THDInputPopup(Context context) {
        super(context);
        initView();
    }

    public THDInputPopup(Context context, OnConfirmClickListener onConfirmClickListener) {
        this(context);
        this.onConfirmClickListener = onConfirmClickListener;
    }

    public THDInputPopup(Context context, String hintStr,OnConfirmClickListener onConfirmClickListener) {
        this(context);
        this.hintStr = hintStr;
        this.onConfirmClickListener = onConfirmClickListener;
        init();
    }

    private void init() {
        if(!TextUtils.isEmpty(hintStr)){
            _editTargetNum.setHint(hintStr);
        }
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.thd_popup_input, null);
        final ViewGroup.LayoutParams layoutParams = generateLayoutParam(SizeUtils.dp2px(280), WRAP_CONTENT);
        view.setLayoutParams(layoutParams);

        _editTargetNum = view.findViewById(R.id.edit_target_num);
        final NumberKeyBoardView targetKeyView = view.findViewById(R.id.targetKeyView);
        targetKeyView.setType(NumberKeyBoardView.TYPE_NUMBER);
        final KeyboardUtil keyboardUtil = new KeyboardUtil((Activity) mContext, NumberKeyBoardView.TYPE_NUMBER, R.xml.popup_input_number);
        _editTargetNum.addTextChangedListener(new CashTextWatcher(_editTargetNum));
        keyboardUtil.setKeyboardView(targetKeyView);
        keyboardUtil.attachTo(_editTargetNum);

        final THDRoundButton confirm = view.findViewById(R.id.confirm);
        confirm.setOnClickListener(this);

        setContentView(view);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.confirm) {
            if (onConfirmClickListener != null) {
                onConfirmClickListener.onClick(_editTargetNum.getText().toString());
            }
            dismiss();
        }
    }


    public interface OnConfirmClickListener {
        void onClick(String ids);
    }
}
