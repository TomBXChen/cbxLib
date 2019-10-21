package com.taohdao.library.common.widget.fragmentnavigator;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import static android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE;
import static android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN;

/**
 * Created by aspsine on 16/3/30.
 */
public class FragmentNavigator {

    private static final String EXTRA_CURRENT_POSITION = "extra_current_position";

    private FragmentManager mFragmentManager;

    private FragmentNavigatorAdapter mAdapter;

    @IdRes
    private int mContainerViewId;

    private int mCurrentPosition = -1;

    private int mDefaultPosition;

    public FragmentNavigator(FragmentManager fragmentManager, FragmentNavigatorAdapter adapter, @IdRes int containerViewId) {
        this.mFragmentManager = fragmentManager;
        this.mAdapter = adapter;
        this.mContainerViewId = containerViewId;
    }

    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(EXTRA_CURRENT_POSITION, mDefaultPosition);
        }
    }

    public FragmentNavigatorAdapter getAdapter(){
        return mAdapter;
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(EXTRA_CURRENT_POSITION, mCurrentPosition);
    }

    public void showFragment(int position, boolean notify) {
        this.mCurrentPosition = position;
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (notify) {
            removeAll(transaction);
            show(position, transaction);
            transaction.commitAllowingStateLoss();
        } else {
            int count = mAdapter.getCount();
            for (int i = 0; i < count; i++) {
                if (position == i) {
                    show(i, transaction);
                } else {
                    hide(i, transaction);
                }
            }
            transaction.commitAllowingStateLoss();
            mFragmentManager.executePendingTransactions();
        }
    }

    public void showFragment(int position) {
        showFragment(position, false);
    }

    public void showFragmentWithAnim(int position, boolean isAnim) {
        this.mCurrentPosition = position;
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        int count = mAdapter.getCount();
        String tag = mAdapter.getTag(position);

        for (int i = 0; i < count; i++) {
            if (position == i) {
                show(i, transaction, isAnim);
            } else {
                hide(i, transaction, isAnim);
            }
        }
        transaction.commit();
        mFragmentManager.executePendingTransactions();
    }

    public Fragment getShowFragment(int position,FragmentTransaction transaction){
        String tag = mAdapter.getTag(position);
        Fragment fragment = null;
        fragment = mFragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = mAdapter.onCreateFragment(position);
            transaction.addToBackStack(null);
        }
        return fragment;
    }

    public void removeAllFragment(boolean allowingStateLoss) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        removeAll(transaction);
        if (allowingStateLoss) {
            transaction.commitAllowingStateLoss();
        } else {
            transaction.commit();
        }
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public Fragment getCurrentFragment() {
        String tag = mAdapter.getTag(mCurrentPosition);
        return mFragmentManager.findFragmentByTag(tag);
    }

    public Fragment getFragment(String tag) {
        return mFragmentManager.findFragmentByTag(tag);
    }

    private void show(int position, FragmentTransaction transaction) {
        show(position, transaction, false);
    }

    private void show(int position, FragmentTransaction transaction, boolean isAnim) {
        String tag = mAdapter.getTag(position);
        final FragmentAnim anim = mAdapter.getAnim(position);
        if (anim != null && isAnim) {
            transaction.setTransition(TRANSIT_FRAGMENT_OPEN);
//            transaction.setCustomAnimations(anim.mEnter, anim.mExit);
        }
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            add(position, transaction);
        } else {
            transaction.show(fragment);
        }
    }

    private void hide(int position, FragmentTransaction transaction) {
      hide(position,transaction,false);
    }
    private void hide(int position, FragmentTransaction transaction,boolean isAnim) {
        String tag = mAdapter.getTag(position);
        final FragmentAnim anim = mAdapter.getAnim(position);
        if (anim != null&&isAnim) {
            transaction.setTransition(TRANSIT_FRAGMENT_CLOSE);
//            transaction.setCustomAnimations(anim.mEnter, anim.mExit);
        }
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            transaction.hide(fragment);
        }
    }

    private void add(int position, FragmentTransaction transaction) {
        Fragment fragment = mAdapter.onCreateFragment(position);
        String tag = mAdapter.getTag(position);
        transaction.add(mContainerViewId, fragment, tag);
    }

    @Deprecated
    public void changeHomeFragment(int type) {
        String tag = mAdapter.getTag(0);
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        switch (type) {
//            case HOME_NORMAL:
//
//                break;
            default:
                if (fragment != null) {
                    transaction.remove(fragment);
                }
                break;
        }
        transaction.commitAllowingStateLoss();
        mFragmentManager.executePendingTransactions();
    }

    public void restFragment() {
        String homeTag = mAdapter.getTag(0);
        String centerTag = mAdapter.getTag(2);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        Fragment homeFragment = mFragmentManager.findFragmentByTag(homeTag);
        Fragment centerFragment = mFragmentManager.findFragmentByTag(centerTag);
        removeFragment(transaction, homeFragment);
        removeFragment(transaction, centerFragment);
        transaction.commitAllowingStateLoss();
        mFragmentManager.executePendingTransactions();
    }

    private void removeFragment(FragmentTransaction transaction, Fragment fragment) {
        if (fragment != null) {
            transaction.remove(fragment);
        }
    }


    private void removeAll(FragmentTransaction transaction) {
        int count = mAdapter.getCount();
        for (int i = 0; i < count; i++) {
            remove(i, transaction);
        }
    }

    private void remove(int position, FragmentTransaction transaction) {
        String tag = mAdapter.getTag(position);
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            transaction.remove(fragment);
        }
    }

    public void setDefaultPosition(int defaultPosition) {
        this.mDefaultPosition = defaultPosition;
        if (mCurrentPosition == -1) {
            this.mCurrentPosition = defaultPosition;
        }
    }
}
