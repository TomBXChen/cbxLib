package com.taohdao.widget.comment;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.taohdao.library.widget.bottomnavigation.utils.Utils;
import com.taohdao.utils.SpanUtils;
import com.taohdao.widget.SimpleWeakObjectPool;

import java.util.List;

/**
 * @author KCrason
 * @date 2018/4/27
 */
public class VerticalCommentWidget extends LinearLayout implements ViewGroup.OnHierarchyChangeListener {

    private List<? extends AbsCommentImpl> mCommentBeans;

    private LinearLayout.LayoutParams mLayoutParams;
    private SimpleWeakObjectPool<View> COMMENT_TEXT_POOL;
    private int mCommentVerticalSpace;
    private OnCommentClickListener onWbListener;
    private OnCommentClickListener mClickListener = new OnCommentClickListener() {

        @Override
        public void onCommentClick(int index) {
                if(onWbListener!=null){
                    onWbListener.onCommentClick(index);
                }
        }
    };

    public void setOnCommentListener(OnCommentClickListener onWbListener) {
        this.onWbListener = onWbListener;
    }

    public VerticalCommentWidget(Context context) {
        super(context);
        init();
    }

    public VerticalCommentWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VerticalCommentWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mCommentVerticalSpace = Utils.dp2px(getContext(), 3f);
        COMMENT_TEXT_POOL = new SimpleWeakObjectPool<>();
        setOnHierarchyChangeListener(this);
    }


    public void addComments(List<? extends AbsCommentImpl> commentBeans) {
        this.mCommentBeans = commentBeans;
        if (commentBeans != null) {
            int oldCount = getChildCount();
            int newCount = commentBeans.size();
            if (oldCount > newCount) {
                removeViewsInLayout(newCount, oldCount - newCount);
            }
            for (int i = 0; i < newCount; i++) {
                boolean hasChild = i < oldCount;
                View childView = hasChild ? getChildAt(i) : null;
                AbsCommentImpl commentBean = commentBeans.get(i);
                final String parentUser = commentBean.parentUser();
                final String childUser = commentBean.childUser();
                final String parentUserId = commentBean.parentUserId();
                final String childUserId = commentBean.childUserId();
                SpannableStringBuilder commentSpan = null;
                if (!TextUtils.isEmpty(childUser)) {
                    commentSpan = SpanUtils.makeReplyCommentSpan(getContext(), parentUser, childUser,parentUserId,childUserId, commentBean.content());
                } else {
                    commentSpan = SpanUtils.makeSingleCommentSpan(getContext(), parentUser,parentUserId, commentBean.content());
                }
                if (childView == null) {
                    childView = COMMENT_TEXT_POOL.get();
                    if (childView == null) {
                        addViewInLayout(makeCommentItemView(commentSpan, i), i, generateMarginLayoutParams(i), true);
                    } else {
                        CommentLayoutView translationLayoutView = (CommentLayoutView) childView;
                        translationLayoutView.setCurrentPosition(i).setSourceContent(commentSpan);
                        addViewInLayout(translationLayoutView, i, generateMarginLayoutParams(i), true);
//                        addViewInLayout(childView, i, generateMarginLayoutParams(i));
                    }
                } else {
                    updateCommentData(childView, commentSpan, i);
                }
            }
            requestLayout();
        }
    }


    /**
     * 更新指定的position的comment
     */
    public void updateTargetComment(int position, List<? extends AbsCommentImpl> commentBeans) {
        int oldCount = getChildCount();
        for (int i = 0; i < oldCount; i++) {
            if (i == position) {
                View childView = getChildAt(i);
                if (childView != null) {
                    AbsCommentImpl commentBean = commentBeans.get(i);
                    final String parentUser = commentBean.parentUser();
                    final String childUser = commentBean.childUser();
                    final String parentUserId = commentBean.parentUserId();
                    final String childUserId = commentBean.childUserId();
                    SpannableStringBuilder commentSpan = null;
                    if (!TextUtils.isEmpty(childUser)) {
                        commentSpan = SpanUtils.makeReplyCommentSpan(getContext(), parentUser, childUser,parentUserId,childUserId, commentBean.content());
                    } else {
                        commentSpan = SpanUtils.makeSingleCommentSpan(getContext(), parentUser, parentUserId,commentBean.content());
                    }
                    updateCommentData(childView, commentSpan, i);
                }
                break;
            }
        }
        requestLayout();
    }


    /**
     * 創建Comment item view
     */
    private View makeCommentItemView(SpannableStringBuilder content, int index) {
        return new CommentLayoutView(getContext())
                .setCurrentPosition(index)
                .setSourceContent(content)
                .setViewClickListener(mClickListener);


    }


    /**
     * 更新comment list content
     */
    private void updateCommentData(View view, SpannableStringBuilder builder, int index) {
        if (view instanceof CommentLayoutView) {
            CommentLayoutView layoutView = (CommentLayoutView) view;
            layoutView.setCurrentPosition(index)
                    .setSourceContent(builder);
//            addViewInLayout(makeCommentItemView(builder, index), index, generateMarginLayoutParams(index), true);
//            removeViewInLayout(view);
        }
    }


    private LayoutParams generateMarginLayoutParams(int index) {
        if (mLayoutParams == null) {
            mLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        if (mCommentBeans != null && index > 0) {
            mLayoutParams.bottomMargin = index == mCommentBeans.size() - 1 ? 0 : mCommentVerticalSpace;
        }
        return mLayoutParams;
    }

    @Override
    public void onChildViewAdded(View parent, View child) {

    }

    @Override
    public void onChildViewRemoved(View parent, View child) {
        COMMENT_TEXT_POOL.put(child);
    }

    public interface OnCommentClickListener {
        void onCommentClick(int index);
    }



}
