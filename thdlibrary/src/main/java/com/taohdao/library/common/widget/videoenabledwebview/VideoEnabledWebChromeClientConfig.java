package com.taohdao.library.common.widget.videoenabledwebview;

import android.media.MediaPlayer;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * Created by admin on 2018/4/23.
 */

public class VideoEnabledWebChromeClientConfig {
    private ToggledFullscreenCallback mToggledFullscreenCallback;
    private MediaPlayer.OnPreparedListener mOnPreparedListener;
    private MediaPlayer.OnCompletionListener mOnCompletionListener;
    private MediaPlayer.OnErrorListener mOnErrorListener;

    /**
     * View that will be hidden when video goes fullscreen
     */
    private View mNonVideoLayout;
    /**
     * View where the video will be shown when video goes fullscreen
     */
    private ViewGroup mVideoLayout;
    private View mLoadingView;
    private WebView mWebView;

    private VideoEnabledWebChromeClientConfig(Builder builder) {
        mLoadingView = builder.mLoadingView;
        mToggledFullscreenCallback = builder.mToggledFullscreenCallback;
        mOnPreparedListener = builder.mOnPreparedListener;
        mOnCompletionListener = builder.mOnCompletionListener;
        mOnErrorListener = builder.mOnErrorListener;
        mNonVideoLayout = builder.mNonVideoLayout;
        mVideoLayout = builder.mVideoLayout;
        mWebView = builder.mWebView;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * {@code VideoEnabledWebChromeClientConfig} builder static inner class.
     */
    public static final class Builder {
        private MediaPlayer.OnCompletionListener mOnCompletionListener;
        private ToggledFullscreenCallback mToggledFullscreenCallback;
        private MediaPlayer.OnPreparedListener mOnPreparedListener;
        private MediaPlayer.OnErrorListener mOnErrorListener;
        private View mNonVideoLayout;
        private ViewGroup mVideoLayout;
        private WebView mWebView;
        private View mLoadingView;

        private Builder() {
        }

        /**
         * Sets the {@code mOnCompletionListener} and returns a reference to this Builder so that the
         * methods can be chained together.
         *
         * @param mOnCompletionListener
         *     the {@code mOnCompletionListener} to set
         * @return a reference to this Builder
         */
        public Builder mOnCompletionListener(MediaPlayer.OnCompletionListener mOnCompletionListener) {
            this.mOnCompletionListener = mOnCompletionListener;
            return this;
        }

        /**
         * Sets the {@code mToggledFullscreenCallback} and returns a reference to this Builder so that
         * the methods can be chained together.
         *
         * @param mToggledFullscreenCallback
         *     the {@code mToggledFullscreenCallback} to set
         * @return a reference to this Builder
         */
        public Builder mToggledFullscreenCallback(
                ToggledFullscreenCallback mToggledFullscreenCallback) {
            this.mToggledFullscreenCallback = mToggledFullscreenCallback;
            return this;
        }

        /**
         * Sets the {@code mOnPreparedListener} and returns a reference to this Builder so that the
         * methods can be chained together.
         *
         * @param mOnPreparedListener
         *     the {@code mOnPreparedListener} to set
         * @return a reference to this Builder
         */
        public Builder mOnPreparedListener(MediaPlayer.OnPreparedListener mOnPreparedListener) {
            this.mOnPreparedListener = mOnPreparedListener;
            return this;
        }

        /**
         * Sets the {@code mOnErrorListener} and returns a reference to this Builder so that the
         * methods
         * can be chained together.
         *
         * @param mOnErrorListener
         *     the {@code mOnErrorListener} to set
         * @return a reference to this Builder
         */
        public Builder mOnErrorListener(MediaPlayer.OnErrorListener mOnErrorListener) {
            this.mOnErrorListener = mOnErrorListener;
            return this;
        }

        /**
         * Sets the {@code mNonVideoLayout} and returns a reference to this Builder so that the methods
         * can be chained together.
         *
         * @param mNonVideoLayout
         *     the {@code mNonVideoLayout} to set
         * @return a reference to this Builder
         */
        public Builder mNonVideoLayout(View mNonVideoLayout) {
            this.mNonVideoLayout = mNonVideoLayout;
            return this;
        }

        /**
         * Sets the {@code mVideoLayout} and returns a reference to this Builder so that the methods can
         * be chained together.
         *
         * @param mVideoLayout
         *     the {@code mVideoLayout} to set
         * @return a reference to this Builder
         */
        public Builder mVideoLayout(ViewGroup mVideoLayout) {
            this.mVideoLayout = mVideoLayout;
            return this;
        }

        /**
         * Sets the {@code mWebView} and returns a reference to this Builder so that the methods can be
         * chained together.
         *
         * @param mWebView
         *     the {@code mWebView} to set
         * @return a reference to this Builder
         */
        public Builder mWebView(WebView mWebView) {
            this.mWebView = mWebView;
            return this;
        }

        /**
         * Returns a {@code VideoEnabledWebChromeClientConfig} built from the parameters previously
         * set.
         *
         * @return a {@code VideoEnabledWebChromeClientConfig} built with parameters of this {@code
         * VideoEnabledWebChromeClientConfig.Builder}
         */
        public VideoEnabledWebChromeClientConfig build() {
            return new VideoEnabledWebChromeClientConfig(this);
        }

        /**
         * Sets the {@code mLoadingView} and returns a reference to this Builder so that the methods can
         * be chained together.
         *
         * @param mLoadingView
         *     the {@code mLoadingView} to set
         * @return a reference to this Builder
         */
        public Builder mLoadingView(View mLoadingView) {
            this.mLoadingView = mLoadingView;
            return this;
        }
    }

    public View getLoadingView() {
        return mLoadingView;
    }

    public View getNonVideoLayout() {
        return mNonVideoLayout;
    }

    public MediaPlayer.OnCompletionListener getOnCompletionListener() {
        return mOnCompletionListener;
    }

    public MediaPlayer.OnErrorListener getOnErrorListener() {
        return mOnErrorListener;
    }

    public MediaPlayer.OnPreparedListener getOnPreparedListener() {
        return mOnPreparedListener;
    }

    public ToggledFullscreenCallback getToggledFullscreenCallback() {
        return mToggledFullscreenCallback;
    }

    public ViewGroup getVideoLayout() {
        return mVideoLayout;
    }

    public WebView getWebView() {
        return mWebView;
    }
}
