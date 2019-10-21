package com.taohdao.library.common.widget;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by taohuadao on 2016/6/27.
 */
public class RebateTextWatcher implements TextWatcher {

    private EditText mEditText;
    private int maxNumber = -1;

    public RebateTextWatcher(EditText e) {
        mEditText = e;
    }

    public RebateTextWatcher(EditText e, int maxNumber) {
        mEditText = e;
        this.maxNumber = maxNumber;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {


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
            return;
        }
        if (content.indexOf(".") != content.lastIndexOf(".")) {
            s.delete(size - 1, size);
            return;
        }

        if (content.indexOf("0") != content.lastIndexOf("0")) {
            s.delete(size - 1, size);
            return;
        }

        try {
            if (Double.valueOf(content) > 9.9) {
                s.delete(size - 1, size);
            } else if (size > 3) {
                s.delete(size - 1, size);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
}
