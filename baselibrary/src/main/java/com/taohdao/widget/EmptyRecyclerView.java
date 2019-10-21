package com.taohdao.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.taohdao.library.common.widget.SharpWrapper;
import com.taohdao.library.common.widget.pulltorefresh.PtrFrameLayout;
import com.taohdao.library.common.widget.pulltorefresh.PtrHandler;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;


/**
 * Created by admin on 2018/3/16.
 */

public class EmptyRecyclerView extends SwipeMenuRecyclerView implements PtrHandler {

    public static final int MODE_AUTO = 1 << 1;//自动加载
    public static final int MODE_PULL_UP = 1 << 2;//手动加载

    private int offsetItemCount = 0;//需要剔除的item数量；默认为0
    private int rowCount = 10;//一页的数量，默认为10
    private int oldItem = 0;

    private SharpWrapper mSharpWrapper;
    private BaseQuickAdapter mQuickAdapter;
    private BaseQuickAdapter.RequestLoadMoreListener mLoadMoreListener;
    private OnLoadingPageListener mOnLoadingPageListener;

    public EmptyRecyclerView(Context context) {
        super(context);
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setAdapter(Adapter adapter, int mode) {
        offsetItemCount = 0;
        if (adapter instanceof BaseQuickAdapter) {
            mQuickAdapter = (BaseQuickAdapter) adapter;
            if (mode == MODE_AUTO) {
                mQuickAdapter.setOnLoadMoreListener(createRequestLoadMoreListener(), this);
                offsetItemCount++;//默认给显示更多view +1
            }
        }

        if (adapter instanceof SharpWrapper) {
            mSharpWrapper = (SharpWrapper) adapter;
            if (mode == MODE_AUTO) {
                mSharpWrapper.setOnLoadMoreListener(createSharpWrapperLoadMoreListener(), this);
                offsetItemCount++;//默认给显示更多view +1
            }
        }
        setAdapter(adapter);
    }

    public void setOnLoadingPageListener(OnLoadingPageListener mOnLoadingPageListener) {
        this.mOnLoadingPageListener = mOnLoadingPageListener;
    }

    public OnLoadingPageListener getOnLoadingPageListener() {
        return mOnLoadingPageListener;
    }

    /**
     * @param rowCount        一页的数量，默认为10
     * @param offsetItemCount 需要剔除的item数量；默认为0
     */
    public void setOffset(int rowCount, int offsetItemCount) {
        this.rowCount = rowCount;
        this.offsetItemCount += offsetItemCount;
    }

    public void setRealOffset(int rowCount, int offsetItemCount) {
        this.rowCount = rowCount;
        this.offsetItemCount = offsetItemCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    /**
     * BaseQuick 专用监听器
     *
     * @return
     */
    private BaseQuickAdapter.RequestLoadMoreListener createRequestLoadMoreListener() {
        if (mLoadMoreListener == null) {
            mLoadMoreListener = new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    if (mQuickAdapter == null)
                        return;
                    final int itemCount = mQuickAdapter.getItemCount();
                    if (itemCount <= 0) {
                        mSharpWrapper.loadMoreEnd(false);
                        return;
                    } else if ((itemCount - offsetItemCount) <= rowCount) {
                        oldItem = 0;
                    }
                    final int newItem = itemCount - offsetItemCount - oldItem;

                    final int page = getPage(itemCount - offsetItemCount, rowCount);
                    if (newItem > 0) {
                        if (newItem < rowCount) {//没有下一页
                            mQuickAdapter.loadMoreEnd(false);
                            Logger.w("没有下一页");
                        } else {
                            if (newItem % rowCount > 0) {
                                mQuickAdapter.loadMoreEnd(false);
                                Logger.w("没有下一页");
                            } else if (mOnLoadingPageListener != null) {
                                mOnLoadingPageListener.onLoadMore(page);
                                Logger.w("有下一页:" + page);
                            }
                        }
                        oldItem = itemCount - offsetItemCount;
                    } else {
                        if (mOnLoadingPageListener != null) {
                            mOnLoadingPageListener.onLoadMore(page);
                        }
                        Logger.w("第一页:" + page);
                    }
                }
            };
        }
        return mLoadMoreListener;
    }

    private SharpWrapper.RequestLoadMoreListener createSharpWrapperLoadMoreListener() {
        SharpWrapper.RequestLoadMoreListener mLoadMoreListener = new SharpWrapper.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (mSharpWrapper == null)
                    return;
                final int itemCount = mSharpWrapper.getItemCount();
                if (itemCount - offsetItemCount <= 0) {
                    mSharpWrapper.loadMoreEnd(false);
                    return;
                } else if ((itemCount - offsetItemCount) <= rowCount) {
                    oldItem = 0;
                }
                final int newItem = itemCount - offsetItemCount - oldItem;
                final int page = getPage(itemCount - offsetItemCount, rowCount);
                Logger.e("itemCount:" + itemCount + "  offsetItemCount:" + offsetItemCount
                        + "  oldItem:" + oldItem + " newItem:" + newItem + " page:" + page
                );
                if (newItem > 0) {
                    if (newItem < rowCount) {//没有下一页
                        mSharpWrapper.loadMoreEnd(false);
                        Logger.w("没有下一页");
                    } else {
                        if (newItem % rowCount > 0) {
                            mSharpWrapper.loadMoreEnd(false);
                            Logger.w("没有下一页");
                        } else if (mOnLoadingPageListener != null) {
                            mOnLoadingPageListener.onLoadMore(page);
                            Logger.w("有下一页:" + page);
                        }
                    }
                    oldItem = itemCount - offsetItemCount;
                } else {
                    if (mOnLoadingPageListener != null) {
                        mOnLoadingPageListener.onLoadMore(page);
                    }
                    Logger.w("第一页:" + page);
                }
            }
        };
        return mLoadMoreListener;
    }

    private int getPage(int itemCount, int rowcount) {
        int page = 0;
        double temp = (double) itemCount / rowcount;
        if ((temp % 1) > 0) {
            page = (int) (temp + 2);
        } else {
            page = (int) (temp + 1);
        }
        return page;
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        oldItem = 0;
        if (mQuickAdapter != null) {
            mQuickAdapter.setEnableLoadMore(true);
            mQuickAdapter.loadMoreComplete();
        }
        if (mSharpWrapper != null && mSharpWrapper.isLoadMoreEnable()) {
            mSharpWrapper.setEnableLoadMore(true);
            mSharpWrapper.loadMoreComplete();
        }

        if (mOnLoadingPageListener != null) {
            mOnLoadingPageListener.onRefresh(1);
        }
    }

    public void resetLoadMore() {
        oldItem = 0;
        if (mQuickAdapter != null) {
            mQuickAdapter.setEnableLoadMore(true);
            mQuickAdapter.loadMoreComplete();
        }
        if (mSharpWrapper != null && mSharpWrapper.isLoadMoreEnable()) {
            mSharpWrapper.setEnableLoadMore(true);
            mSharpWrapper.loadMoreComplete();
        }
    }

    public interface OnLoadingPageListener {
        void onRefresh(int page);

        void onLoadMore(int page);
    }
}
