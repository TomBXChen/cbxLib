package com.taohdao.library.common.widget.numberkeyboard;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.taohdao.library.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static com.taohdao.library.common.widget.numberkeyboard.NumberKeyBoardView.TYPE_DECIMALS;
import static com.taohdao.library.common.widget.numberkeyboard.NumberKeyBoardView.TYPE_DEFAULT;
import static com.taohdao.library.common.widget.numberkeyboard.NumberKeyBoardView.TYPE_NUMBER;

/**
 * 自定义键盘
 * Created by xuejinwei on 16/3/5.
 */
public class KeyboardUtil {
    private Activity mActivity;
    private boolean mIfRandom;

    private NumberKeyBoardView mKeyboardView;
    private Keyboard mKeyboardNumber;//数字键盘
    private EditText mEditText;
    private int type = TYPE_DEFAULT;

    public KeyboardUtil(Activity activity) {
        this(activity, -9999);
    }

    public KeyboardUtil(Activity activity, int type) {
        this(activity, false, type, R.xml.keyboardnumber);
    }

    public KeyboardUtil(Activity activity, int type, int xmlLayoutResId) {
        this(activity, false, type, xmlLayoutResId);
    }

    /**
     * @param activity
     * @param ifRandom
     */
    public KeyboardUtil(Activity activity, boolean ifRandom, int type, int xmlLayoutResId) {
        this.mActivity = activity;
        this.mIfRandom = ifRandom;
        this.type = type;
        mKeyboardNumber = new Keyboard(mActivity, xmlLayoutResId);

    }

    public void setKeyboardView(NumberKeyBoardView mKeyboardView) {
        this.mKeyboardView = mKeyboardView;
    }

    /**
     * edittext绑定自定义键盘
     *
     * @param editText 需要绑定自定义键盘的edittext
     */
    public void attachTo(EditText editText) {
        this.mEditText = editText;
        hideSystemSofeKeyboard(mActivity.getApplicationContext(), mEditText);
        showSoftKeyboard();
    }

    public void showSoftKeyboard() {
        if (mKeyboardNumber == null) {
            mKeyboardNumber = new Keyboard(mActivity, R.xml.keyboardnumber);
        }
        if (mKeyboardView == null) {
            throw new NullPointerException("mKeyboardView must be not null");
//            mKeyboardView = (NumberKeyBoardView) mActivity.findViewById(R.id.keyboard_view);
        }
        if (mIfRandom) {
            randomKeyboardNumber();
        } else {
            mKeyboardView.setKeyboard(mKeyboardNumber);
        }
        mKeyboardView.setEnabled(true);
        mKeyboardView.setPreviewEnabled(false);
        mKeyboardView.setVisibility(View.VISIBLE);
        mKeyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener);

    }

    private KeyboardView.OnKeyboardActionListener mOnKeyboardActionListener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = mEditText.getText();
            int start = mEditText.getSelectionStart();
            if (primaryCode == -7) {//清空
                if (editable != null && editable.length() > 0) {
                    editable.delete(0, editable.length());
                }
            } else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
            } else if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 隐藏键盘

                if (mOnCancelClick != null) {
                    if (mOnCancelClick.onCancellClick()) {
                        hideKeyboard();
                    }
                } else {
                    hideKeyboard();
                }
            } else if (primaryCode == Keyboard.KEYCODE_DONE) {// 隐藏键盘

                if (mOnOkClick != null) {
                    if (mOnOkClick.onOkClick()) {
                        hideKeyboard();
                    }
                } else {
                    hideKeyboard();
                }
            } else {
                switch (type) {
                    case TYPE_NUMBER:
                        if (primaryCode != 99 && primaryCode != 46) {
                            editable.insert(start, Character.toString((char) primaryCode));
                        }
                        break;
                    case TYPE_DECIMALS:
                        if (primaryCode != 99) {
                            editable.insert(start, Character.toString((char) primaryCode));
                        }
                        break;
                    default:
                        editable.insert(start, Character.toString((char) primaryCode));
                        break;
                }
            }
        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    };


    /**
     * 隐藏系统键盘
     *
     * @param editText
     */
    public static void hideSystemSofeKeyboard(Context context, EditText editText) {
        if (context == null || editText == null) return;
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= 11) {
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(editText, false);

            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            editText.setInputType(InputType.TYPE_NULL);
        }
        // 如果软键盘已经显示，则隐藏
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }


    public interface OnOkClick {
        boolean onOkClick();
    }

    public interface onCancelClick {
        boolean onCancellClick();
    }

    public OnOkClick mOnOkClick = null;
    public onCancelClick mOnCancelClick;

    public void setOnOkClick(OnOkClick onOkClick) {
        mOnOkClick = onOkClick;
    }

    public void setOnCancelClick(onCancelClick onCancelClick) {
        mOnCancelClick = onCancelClick;
    }


    private boolean isNumber(String str) {
        String wordstr = "0123456789";
        return wordstr.contains(str);
    }

    private void randomKeyboardNumber() {
        List<Keyboard.Key> keyList = mKeyboardNumber.getKeys();
        // 查找出0-9的数字键
        List<Keyboard.Key> newkeyList = new ArrayList<Keyboard.Key>();
        for (int i = 0; i < keyList.size(); i++) {
            if (keyList.get(i).label != null
                    && isNumber(keyList.get(i).label.toString())) {
                newkeyList.add(keyList.get(i));
            }
        }
        // 数组长度
        int count = newkeyList.size();
        // 结果集
        List<KeyModel> resultList = new ArrayList<KeyModel>();
        // 用一个LinkedList作为中介
        LinkedList<KeyModel> temp = new LinkedList<KeyModel>();
        // 初始化temp
        for (int i = 0; i < count; i++) {
            temp.add(new KeyModel(48 + i, i + ""));
        }
        // 取数
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            int num = rand.nextInt(count - i);
            resultList.add(new KeyModel(temp.get(num).getCode(),
                    temp.get(num).getLable()));
            temp.remove(num);
        }
        for (int i = 0; i < newkeyList.size(); i++) {
            newkeyList.get(i).label = resultList.get(i).getLable();
            newkeyList.get(i).codes[0] = resultList.get(i)
                    .getCode();
        }

        mKeyboardView.setKeyboard(mKeyboardNumber);
    }

    public void showKeyboard() {
        int visibility = mKeyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            mKeyboardView.setVisibility(View.VISIBLE);
        }
    }

    public void hideKeyboard() {
        int visibility = mKeyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            mKeyboardView.setVisibility(View.GONE);
        }
    }
}