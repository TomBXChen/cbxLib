package com.taohdao.base;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.taohdao.adapter.DelegateAdapterItem;
import com.taohdao.adapter.DelegateRecAdapter;
import com.taohdao.http.BasicsResponse;
import com.taohdao.http.OnExecuteEvent;
import com.taohdao.http.PageRequest;
import com.taohdao.http.utils.HttpsUtils;
import com.taohdao.library.GlobalConfig;
import com.taohdao.library.common.widget.SharpWrapper;
import com.taohdao.library.common.widget.THDEmptyView;
import com.taohdao.library.common.widget.pulltorefresh.PtrFrameLayout;
import com.taohdao.widget.EmptyRecyclerView;
import com.taohdao.widget.RecyclerViewHelper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import static com.taohdao.library.GlobalConfig.LOAD_MORE;
import static com.taohdao.widget.RecyclerViewHelper.ITEM_TYPE_LIST;

/**
 * 分页基类，需要分页的版面继承此类实现方法即可
 * Created by admin on 2018/4/2.
 */
public abstract class PageActivity<T,R extends PageRequest> extends BasicsImplActivity {

    private List<T> mDataList = new ArrayList<>();
    protected DelegateRecAdapter listAdapter;
    protected DelegateAdapter mDelegateAdapter;

    public static final int METHOD_GET = 1;
    public static final int METHOD_POST = 2;


    @IntDef({METHOD_GET, METHOD_POST})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }


    @Override
    public void initData(Bundle savedInstanceState) {
        initOthersData(savedInstanceState);
        final EmptyRecyclerView mRecyclerView = getRecyclerView();
        mDelegateAdapter = RecyclerViewHelper.initVirtualLayoutManager(getActivity(), mRecyclerView, useBackground());
        initBeforeAdapter(mDelegateAdapter);
        mDelegateAdapter.addAdapter(listAdapter = RecyclerViewHelper.createListAdapter(getDataList(), new RecyclerViewHelper.OnItemCreate() {
            @Override
            public DelegateAdapterItem createItem() {
                return createAdapter();
            }

            @Override
            public LayoutHelper createLayoutHelper() {
                return createAdapterLayoutHelper();
            }

            @Override
            public int getItemType() {
                return ITEM_TYPE_LIST;
            }
        }));
        SharpWrapper mLoadMoreWrapper = new SharpWrapper(getActivity(), mDelegateAdapter);
        mRecyclerView.setAdapter(mLoadMoreWrapper, EmptyRecyclerView.MODE_AUTO);
        initAfterAdapter();
        mLoadMoreWrapper.disableLoadMoreIfNotFullPage();
        mRecyclerView.setOnLoadingPageListener(new EmptyRecyclerView.OnLoadingPageListener() {
            @Override
            public void onRefresh(int page) {
                requestForPage(page);
            }

            @Override
            public void onLoadMore(int page) {
                requestForPage(page);
            }
        });
        getPtrFrameLayout().setPtrHandler(mRecyclerView);
        if (!lazyRefresh()) {
            refresh();
        }
    }

    protected boolean lazyRefresh() {
        return false;
    }

    protected boolean enabledPullRefresh() {
        return true;
    }

    protected void refresh() {
        if (getEmptyView() != null) {
            getPtrFrameLayout().setPullToRefresh(false);
            final THDEmptyView emptyView = getEmptyView();
            emptyView.show(true);
            mPresenter.execute(200, new OnExecuteEvent() {
                @Override
                public boolean onExecute() {
                    return false;
                }

                @Override
                public void accept() {
                    requestForPage(1);
                }
            });
        } else {
            getPtrFrameLayout().autoRefreshDelay(200);
        }
    }

    protected boolean refreshDialog = false;

    public void refresh(boolean showDialog){
        if(showDialog){
            refreshDialog = true;
            showLoading();
            mPresenter.execute(200, new OnExecuteEvent() {
                @Override
                public boolean onExecute() {
                    return false;
                }

                @Override
                public void accept() {
                    requestForPage(1);
                }
            });
        }else{
            refresh();
        }
    }

    protected boolean useBackground() {
        return false;
    }

    protected void initBeforeAdapter(DelegateAdapter delegateAdapter) {

    }

    protected void initAfterAdapter() {

    }

    protected abstract void initOthersData(Bundle savedInstanceState);


    @Override
    public void callback(BasicsResponse response, int tag, Object object) {
        try {
            R rq = (R) object;

            switch (tag) {
                case GlobalConfig.LOAD:
                    adjustResponse(response, tag,rq);
                    break;
                case LOAD_MORE:
                    adjustResponse(response, tag,rq);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (tag == GlobalConfig.LOAD && (getEmptyView() != null || useCustomEmpty())) {
                if (response == null) {
                    //这里显示网络错误的占位View，先用空余的View替代
                    if (getEmptyView() != null) {
                        getEmptyView().showEmptyIcon(getEmptyTitle(), getEmptyIcon());
                        getPtrFrameLayout().setPullToRefresh(false);
                    }
                } else if (getDataList().size() > 0) {
                    if (onPageHasDataCallback()) {
                        if (getEmptyView() != null) {
                            //隐藏占位View
                            getEmptyView().hide();
                            getPtrFrameLayout().setPullToRefresh(enabledPullRefresh());
                        }
                    }
                } else {
                    if (onPageNotDataCallback()) {
                        if (getEmptyView() != null) {
                            getEmptyView().showEmptyIcon(getEmptyTitle(), getEmptyIcon());
                            getPtrFrameLayout().setPullToRefresh(false);
                        }
                    }
                    getSharpWrapper().setEnableLoadMore(false);
                }
            }
            try {
                final SharpWrapper originAdapter = getSharpWrapper();
                if (originAdapter != null) {
                    if (originAdapter.isLoadMoreEnable() && (originAdapter.getLoadMoreStatus() == LoadMoreView.STATUS_DEFAULT || originAdapter.getLoadMoreStatus() == LoadMoreView.STATUS_LOADING)) {
                        originAdapter.loadMoreComplete();
                    }
                    if (tag == LOAD_MORE) {
                        if (response == null) originAdapter.loadMoreFail();
                    }
                }
                getRecyclerView().getAdapter().notifyDataSetChanged();
                listAdapter.notifyDataSetChanged();
                if (getPtrFrameLayout().isRefreshing()) getPtrFrameLayout().refreshComplete();

                if(refreshDialog){
                    refreshDialog =  false;
                    resetLoadMore();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    protected void resetLoadMore(){
        getRecyclerView().resetLoadMore();
    }

    protected boolean useCustomEmpty() {
        return false;
    }

    protected boolean onPageHasDataCallback() {
        return true;
    }

    /**
     * 默认返回true，如返回false自行处理没有数据的版面
     *
     * @return
     */
    protected boolean onPageNotDataCallback() {
        return true;
    }

    private void adjustResponse(BasicsResponse response, int tag,R rq) {
        final List<T> newList = HttpsUtils.transform(adjustList(response,rq.getPage()));
        switch (tag) {
            case GlobalConfig.LOAD:
                getDataList().clear();
                getDataList().addAll(newList);
                if (newList.size() > 0) {
                    getSharpWrapper().setEnableLoadMore(true);
                }

                break;
            case LOAD_MORE:
                if (newList.size() > 0) {
                    getDataList().addAll(newList);
                } else {
                    getSharpWrapper().loadMoreEnd();
                }
                break;
        }
    }

    protected @PageActivity.State
    int getMethod() {
        return METHOD_GET;
    }

    protected SharpWrapper getSharpWrapper() {
        return (SharpWrapper) getRecyclerView().getOriginAdapter();
    }


    protected THDEmptyView getEmptyView() {
        return null;
    }

    protected String getEmptyTitle() {
        return "暂无数据";
    }

    protected @DrawableRes
    int getEmptyIcon() {
        return com.taohdao.base.R.mipmap.ic_empty_n;
    }

    protected abstract EmptyRecyclerView getRecyclerView();

    protected abstract PtrFrameLayout getPtrFrameLayout();

    protected abstract DelegateAdapterItem createAdapter();

    protected abstract List<T> adjustList(BasicsResponse response, int pagesize);

    protected LayoutHelper createAdapterLayoutHelper() {
        return null;
    }

    protected List<T> getDataList() {
        return mDataList;
    }

    protected void requestForPage(int page) {
        try{

        final R request = createRequest(page);
        switch (getMethod()) {
            case METHOD_GET:
                mPresenter.get(request, false, page == 1 ? GlobalConfig.LOAD : LOAD_MORE, request);
                break;
            case METHOD_POST:
                mPresenter.request(request, false, page == 1 ? GlobalConfig.LOAD : LOAD_MORE, request);
                break;
        }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected abstract R createRequest(int page);

    public DelegateAdapter getDelegateAdapter() {
        return mDelegateAdapter;
    }

}
