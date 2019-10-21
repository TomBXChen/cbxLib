package com.taohdao.library.common.widget;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView;

/**
 * Created by Administrator on 2018-03-18.
 */

public class SharpWrapper extends RecyclerView.Adapter<ViewHolder> {


    private Context mContext;
    private RecyclerView.Adapter mInnerAdapter;
    protected LayoutInflater mLayoutInflater;

    //load more
    private boolean mNextLoadEnable = false;
    private boolean mLoadMoreEnable = false;
    private boolean mLoading = false;
    private LoadMoreView mLoadMoreView = new SimpleLoadMoreView();
    private SharpWrapper.RequestLoadMoreListener mRequestLoadMoreListener;
    private boolean mEnableLoadMoreEndClick = false;
    private SpanSizeLookup mSpanSizeLookup;

    public static final int LOADING_VIEW = 0x00000222;

    private int mPreLoadNumber = 1;

    private RecyclerView mRecyclerView;

    protected RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    private void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    private void checkNotNull() {
        if (getRecyclerView() == null) {
            throw new RuntimeException("please bind recyclerView first!");
        }
    }

    public SharpWrapper(Context context, RecyclerView.Adapter adapter) {
        this.mContext = context;
        this.mInnerAdapter = adapter;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setOnLoadMoreListener(SharpWrapper.RequestLoadMoreListener requestLoadMoreListener, RecyclerView recyclerView) {
        openLoadMore(requestLoadMoreListener);
        if (getRecyclerView() == null) {
            setRecyclerView(recyclerView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case LOADING_VIEW:
                return getLoadingView(parent);
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        autoLoadMore(position);
        int viewType = holder.getItemViewType();
        switch (viewType) {
            case LOADING_VIEW:
                mLoadMoreView.convert((BaseViewHolder)holder);
                break;
            default:
                mInnerAdapter.onBindViewHolder(holder, position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mInnerAdapter.getItemCount() + getLoadMoreViewCount();
    }

    @Override
    public int getItemViewType(int position) {
        int adapterCount = mInnerAdapter.getItemCount();
        if (position < adapterCount) {
            return mInnerAdapter.getItemViewType(position);
        } else {
            return LOADING_VIEW;
        }
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);

                    if (mSpanSizeLookup == null) {
                        return isFixedViewType(type) ? gridManager.getSpanCount() : 1;
                    } else {
                        return (isFixedViewType(type)) ? gridManager.getSpanCount() : mSpanSizeLookup.getSpanSize(gridManager,position);
                    }
                }


            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);
        int type = holder.getItemViewType();
        if (type == LOADING_VIEW) {
            setFullSpan(holder);
        }
    }

    protected void setFullSpan(RecyclerView.ViewHolder holder) {
        if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder
                    .itemView.getLayoutParams();
            params.setFullSpan(true);
        }
    }

    protected boolean isFixedViewType(int type) {
        return type ==LOADING_VIEW;
    }

    public void setPreLoadNumber(int preLoadNumber) {
        if (preLoadNumber > 1) {
            mPreLoadNumber = preLoadNumber;
        }
    }

    private void autoLoadMore(int position) {
//        Log.e("SharpWrapper","position:"+position+" getItemCount():"+getItemCount());
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        if (position < getItemCount() - mPreLoadNumber) {
            return;
        }
        if (mLoadMoreView.getLoadMoreStatus() != LoadMoreView.STATUS_DEFAULT) {
            return;
        }
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_LOADING);
        if (!mLoading) {
            mLoading = true;
            if (getRecyclerView() != null) {
                getRecyclerView().post(new Runnable() {
                    @Override
                    public void run() {
                        mRequestLoadMoreListener.onLoadMoreRequested();
                    }
                });
            } else {
                mRequestLoadMoreListener.onLoadMoreRequested();
            }
        }
    }

    private ViewHolder getLoadingView(ViewGroup parent) {
        View view = getItemView(mLoadMoreView.getLayoutId(), parent);
        ViewHolder holder = new BaseViewHolder(view) {
        };
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoadMoreView.getLoadMoreStatus() == LoadMoreView.STATUS_FAIL) {
                    notifyLoadMoreToLoading();
                }
                if (mEnableLoadMoreEndClick && mLoadMoreView.getLoadMoreStatus() == LoadMoreView.STATUS_END) {
                    notifyLoadMoreToLoading();
                }
            }
        });
        return holder;
    }

    protected View getItemView(@LayoutRes int layoutResId, ViewGroup parent) {
        return mLayoutInflater.inflate(layoutResId, parent, false);
    }

    public int getLoadMoreStatus(){
        return mLoadMoreView.getLoadMoreStatus();
    }

    public void notifyLoadMoreToLoading() {
        if (mLoadMoreView.getLoadMoreStatus() == LoadMoreView.STATUS_LOADING) {
            return;
        }
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
        notifyItemChanged(getItemCount()-getLoadMoreViewCount());
    }

    /**
     * Refresh end, no more data
     */
    public void loadMoreEnd() {
        loadMoreEnd(false);
    }

    /**
     * Refresh end, no more data
     *
     * @param gone if true gone the load more view
     */
    public void loadMoreEnd(boolean gone) {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        mLoading = false;
        mNextLoadEnable = false;
        mLoadMoreView.setLoadMoreEndGone(gone);
        if (gone) {
            notifyItemRemoved(getItemCount()-getLoadMoreViewCount());
        } else {
            mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_END);
            notifyItemChanged(getItemCount()-getLoadMoreViewCount());
        }
    }

    /**
     * Refresh complete
     */
    public void loadMoreComplete() {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        mLoading = false;
        mNextLoadEnable = true;
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
        int itemCount = getItemCount();
        notifyItemChanged(itemCount-getLoadMoreViewCount());
    }

    /**
     * Refresh failed
     */
    public void loadMoreFail() {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        mLoading = false;
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_FAIL);
        notifyItemChanged(getItemCount()-getLoadMoreViewCount());
    }

    /**
     * Set the enabled state of load more.
     *
     * @param enable True if load more is enabled, false otherwise.
     */
    public void setEnableLoadMore(boolean enable) {
        int oldLoadMoreCount = getLoadMoreViewCount();
        mLoadMoreEnable = enable;
        int newLoadMoreCount = getLoadMoreViewCount();

        if (oldLoadMoreCount == 1) {
            if (newLoadMoreCount == 0) {
                notifyItemRemoved(getItemCount()-getLoadMoreViewCount());
            }
        } else {
            if (newLoadMoreCount == 1) {
                mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
                notifyItemInserted(getItemCount()-getLoadMoreViewCount());
            }
        }
    }

    public boolean isLoadMoreEnable(){
        return mLoadMoreEnable;
    }

    public int getLoadMoreViewCount() {
        if (mRequestLoadMoreListener == null || !mLoadMoreEnable) {
            return 0;
        }
        if (!mNextLoadEnable && mLoadMoreView.isLoadEndMoreGone()) {
            return 0;
        }
        return 1;
    }

    public void disableLoadMoreIfNotFullPage() {
        checkNotNull();
        disableLoadMoreIfNotFullPage(getRecyclerView());
    }

    /**
     * <p>
     * 不是配置项！！
     * <p>
     * 这个方法是用来检查是否满一屏的，所以只推荐在 {setNewData(List)} 之后使用
     * 原理很简单，先关闭 load more，检查完了再决定是否开启
     * <p>
     * 不是配置项！！
     *
     * @param recyclerView your recyclerView
     *                     setNewData(List)
     */
    public void disableLoadMoreIfNotFullPage(RecyclerView recyclerView) {
        setEnableLoadMore(false);
        if (recyclerView == null) return;
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager == null) return;
        if (manager instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) manager;
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if ((linearLayoutManager.findLastCompletelyVisibleItemPosition() + 1) != getItemCount()) {
                        setEnableLoadMore(true);
                    }
                }
            }, 50);
        } else if (manager instanceof StaggeredGridLayoutManager) {
            final StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) manager;
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    final int[] positions = new int[staggeredGridLayoutManager.getSpanCount()];
                    staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(positions);
                    int pos = getTheBiggestNumber(positions) + 1;
                    if (pos != getItemCount()) {
                        setEnableLoadMore(true);
                    }
                }
            }, 50);
        }
    }

    private int getTheBiggestNumber(int[] numbers) {
        int tmp = -1;
        if (numbers == null || numbers.length == 0) {
            return tmp;
        }
        for (int num : numbers) {
            if (num > tmp) {
                tmp = num;
            }
        }
        return tmp;
    }

    private void openLoadMore(SharpWrapper.RequestLoadMoreListener requestLoadMoreListener) {
        this.mRequestLoadMoreListener = requestLoadMoreListener;
        mNextLoadEnable = true;
        mLoadMoreEnable = true;
        mLoading = false;
    }


    public interface SpanSizeLookup {
        int getSpanSize(GridLayoutManager gridLayoutManager, int position);
    }

    public interface RequestLoadMoreListener {

        void onLoadMoreRequested();

    }
}
