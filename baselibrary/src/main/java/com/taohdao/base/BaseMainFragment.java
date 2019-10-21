package com.taohdao.base;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by admin on 2018/9/10.
 */

public abstract class BaseMainFragment extends BasicsImplFragment  {

    protected OnCloseMenuListener _mOnCloseMenuListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCloseMenuListener) {
            _mOnCloseMenuListener = (OnCloseMenuListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCloseMenuListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        _mOnCloseMenuListener = null;
    }

    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;
    /**
     * 处理回退事件
     *
     * @return
     */
    @Override
    public boolean onBackPressedSupport() {
        if (!_mOnCloseMenuListener.onCloseMenu()) {
            if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
                _mActivity.finish();
            } else {
                TOUCH_TIME = System.currentTimeMillis();
                Toast.makeText(_mActivity, "再按一次退出", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    public interface OnCloseMenuListener {
        boolean onCloseMenu();
    }
}
