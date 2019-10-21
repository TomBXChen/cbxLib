package com.taohdao.library.common.widget.fragmentnavigator;

import android.support.annotation.AnimRes;
import android.support.annotation.AnimatorRes;

/**
 * Created by admin on 2018/8/9.
 */

public class FragmentAnim {
    public @AnimatorRes@AnimRes int mEnter;
    public @AnimatorRes@AnimRes int mExit;

    public FragmentAnim(int mEnter, int mExit) {
        this.mEnter = mEnter;
        this.mExit = mExit;
    }
}
