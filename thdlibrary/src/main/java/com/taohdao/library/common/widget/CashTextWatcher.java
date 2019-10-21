package com.taohdao.library.common.widget;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.taohdao.library.common.widget.dialog.THDViewHelper;

/**
 * Created by taohuadao on 2016/6/27.
 * 不能第一个输入小数点和0
 */
public class CashTextWatcher implements TextWatcher {

    private EditText mEditText;
    private int maxNumber = -1;

    public CashTextWatcher(EditText e) {
        mEditText = e;
    }
    public CashTextWatcher(EditText e, int maxNumber) {
        mEditText = e;
        this.maxNumber = maxNumber;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().contains(".")) {
            if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                s = s.toString().subSequence(0,
                        s.toString().indexOf(".") + 3);
                mEditText.setText(s);
                mEditText.setSelection(s.length());
                return;
            }
        }

        if (s.toString().startsWith("0")
                && s.toString().trim().length() > 1) {
            if (!s.toString().substring(1, 2).equals(".")) {
                mEditText.setText(s.subSequence(0, 1));
                mEditText.setSelection(1);
                return;
            }
        }
        try{
            if (maxNumber!=-1&&s.length() > 0&&!(s.toString().contains("."))) {
                if (Float.valueOf(s.toString()) > maxNumber) {
                    mEditText.setText(maxNumber+"");
                    THDViewHelper.afterCursor(mEditText);
                }
            }
        }catch (Exception e){
            mEditText.setText(maxNumber+"");
            THDViewHelper.afterCursor(mEditText);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {
        String content = s == null ? null : s.toString();
        if (s == null || s.length() == 0) {
            return;
        }
        int size = content.length();
        if (content.startsWith(".") /*|| content.startsWith("0")*/) {
            s.delete(0, size);
        }
        if(content.indexOf(".")!=content.lastIndexOf(".")){
            s.delete(size-1, size);
        }
    }
}
