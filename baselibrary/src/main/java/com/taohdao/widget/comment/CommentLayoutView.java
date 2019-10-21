package com.taohdao.widget.comment;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.taohdao.base.R;
import com.taohdao.library.common.widget.textview.THDSpanTouchFixTextView;

public class CommentLayoutView extends LinearLayout implements View.OnClickListener {

    private THDSpanTouchFixTextView mTxtSourceContent;
    private int mCurrentPosition;
    private VerticalCommentWidget.OnCommentClickListener mViewClickListener;

    public CommentLayoutView(Context context) {
        super(context);
        init(context);
    }

    public CommentLayoutView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CommentLayoutView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_comment_view, this, false);
        mTxtSourceContent = itemView.findViewById(R.id.content);
//        mTxtSourceContent.setOnLongClickListener(this);
        mTxtSourceContent.setMovementMethodDefault();
        mTxtSourceContent.setOnClickListener(this);
        addView(itemView);
    }


    public CommentLayoutView setViewClickListener(VerticalCommentWidget.OnCommentClickListener mViewClickListener) {
        this.mViewClickListener = mViewClickListener;
        return this;
    }

    public CommentLayoutView setSourceContent(SpannableStringBuilder builder) {
        if (mTxtSourceContent != null) {
            mTxtSourceContent.setText(builder);
        }
        return this;
    }

    public CommentLayoutView setCurrentPosition(int mCurrentPosition) {
        this.mCurrentPosition = mCurrentPosition;
        return this;
    }


    @Override
    public void onClick(View v) {
        if (mViewClickListener != null) {
            mViewClickListener.onCommentClick(mCurrentPosition);
        }
    }
}
