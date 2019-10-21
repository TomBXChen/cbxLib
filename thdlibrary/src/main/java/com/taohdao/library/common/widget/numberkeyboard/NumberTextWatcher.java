package com.taohdao.library.common.widget.numberkeyboard;

import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by admin on 2017/4/17.
 */

public class NumberTextWatcher implements TextWatcher {

    private EditText mEditText;
    private int maxNumber = -1;
    private String beforeText;
    private boolean exceededZero = false;
    private OnStateErrorListener  mOnStateErrorListener;

    public NumberTextWatcher(EditText e) {
        mEditText = e;
    }

    public NumberTextWatcher(EditText e, int maxNumber) {
       this(e,maxNumber,null);
    }
    public NumberTextWatcher(EditText e, int maxNumber,OnStateErrorListener mOnStateErrorListener) {
        this(e,maxNumber,false,mOnStateErrorListener);
    }

    /**
     * @param e
     * @param maxNumber
     * @param exceededZero 是否大于零
     * @param mOnStateErrorListener
     */
    public NumberTextWatcher(EditText e, int maxNumber,boolean exceededZero,OnStateErrorListener mOnStateErrorListener) {
        mEditText = e;
        this.maxNumber = maxNumber;
        this.exceededZero = exceededZero;
        this.mOnStateErrorListener = mOnStateErrorListener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        this.beforeText = s.toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().startsWith("0")
                && s.toString().trim().length() > 1) {
            final String substring = s.toString().substring(1, 2);
            if (!substring.equals(".")&&!exceededZero) {
                mEditText.setText(s.subSequence(0, 1));
                mEditText.setSelection(1);
                return;
            } else {
                final int size = s.length();
                if (size > 4) {
                    mEditText.setText(s.subSequence(0, 4));
                    mEditText.setSelection(4);
                    return;
                }
            }
        } else if (!s.toString().startsWith("0") && s.toString().contains(".")) {
            final int pIndex = s.toString().indexOf(".") + 1;
            final int size = s.length();
            if (size > pIndex + 2) {
                mEditText.setText(s.subSequence(0, pIndex + 2));
                mEditText.setSelection(pIndex + 2);
                return;
            }
        }

//        try{
//            if (maxNumber!=-1&&s.length() > 0&&!(s.toString().contains("."))) {
//                if (Float.valueOf(s.toString()) > maxNumber) {
//                    mEditText.setText(maxNumber+"");
//                    afterCursor(mEditText);
//                }
//            }
//        }catch (Exception e){
//            mEditText.setText(maxNumber+"");
//            afterCursor(mEditText);
//        }

    }

    @Override
    public void afterTextChanged(Editable s) {
        String content = s == null ? null : s.toString();
        if (s == null || s.length() == 0) {
            return;
        }
        int size = content.length();
        if (content.startsWith(".")) {
            s.delete(0, size);
        }

        if(!exceededZero){
            final int pIndex = s.toString().indexOf(".");
            final int lastIndex = s.toString().lastIndexOf(".");
            if (pIndex != lastIndex) {
                s.delete(lastIndex, size);
            }
        }else{
            if (content.startsWith("0")&&content.length()>1) {
                final String lastNumber = content.substring(content.length() - 1, content.length());
                s.clear();
                s.append(lastNumber);
            }
        }

        try {
            if (maxNumber != -1 && !TextUtils.isEmpty(beforeText)) {
                final int beforeNumber = Integer.parseInt(beforeText);
//                final int nextNumber = Integer.parseInt(content.substring(content.length() - 1, content.length()));
                final int nextNumber = Integer.parseInt(content);
                if (nextNumber > maxNumber) {
                    s.delete(content.length() - 1, content.length());
                    if(mOnStateErrorListener!=null){
                        mOnStateErrorListener.onRangeError();
                    }
//
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }

    public void afterCursor(TextView view) {
        CharSequence charSequence = view.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
    }

    public interface OnStateErrorListener {
        void onRangeError();
    }
}
