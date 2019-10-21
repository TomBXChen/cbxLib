package com.taohdao.library.common.widget.videoenabledwebview;

import android.media.MediaPlayer;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;

/**
 * Created by admin on 2018/4/23.
 */

public class VideoEnabledWebChromeClient extends WebChromeClient
        implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener {

    private final VideoEnabledWebChromeClientConfig mVideoEnabledWebChromeClientConfig;

    public VideoEnabledWebChromeClient(
            VideoEnabledWebChromeClientConfig pVideoEnabledWebChromeClientConfig) {
        mVideoEnabledWebChromeClientConfig = pVideoEnabledWebChromeClientConfig;

        prepareView();
    }

    private ToggledFullscreenCallback toggledFullscreenCallback;
    private MediaPlayer.OnPreparedListener mOnPreparedListener;
    private MediaPlayer.OnCompletionListener mOnCompletionListener;
    private MediaPlayer.OnErrorListener mOnErrorListener;
    /**
     * A View in the activity's layouts that contains every other view that should be hidden when the
     * video goes full-screen.
     */
    private View activityNonVideoView;
    /**
     * A ViewGroup in the activity's layouts that will display the video. Typically you would like
     * this
     * to fill the whole layouts.
     */
    private ViewGroup activityVideoView;
    /**
     * A View to be shown while the video is loading (typically only used in API level <11). Must be
     * already inflated and not attached to a parent view.
     */
    private View loadingView;
    /**
     * The owner VideoEnabledWebView. Passing it will enable the VideoEnabledWebChromeClient to
     * detect
     * the HTML5 video ended event and exit full-screen.
     */
    private WebView webView;
    /**
     * Indicates if the video is being displayed using a custom view (typically full-screen)
     */
    private boolean isVideoFullscreen;

    private FrameLayout videoViewContainer;
    private CustomViewCallback videoViewCallback;

    private void prepareView() {
        toggledFullscreenCallback = mVideoEnabledWebChromeClientConfig.getToggledFullscreenCallback();
        mOnPreparedListener = mVideoEnabledWebChromeClientConfig.getOnPreparedListener();
        mOnCompletionListener = mVideoEnabledWebChromeClientConfig.getOnCompletionListener();
        mOnErrorListener = mVideoEnabledWebChromeClientConfig.getOnErrorListener();
        activityNonVideoView = mVideoEnabledWebChromeClientConfig.getNonVideoLayout();
        activityVideoView = mVideoEnabledWebChromeClientConfig.getVideoLayout();
        loadingView = mVideoEnabledWebChromeClientConfig.getLoadingView();
        webView = mVideoEnabledWebChromeClientConfig.getWebView();
    }

    /**
     * Indicates if the video is being displayed using a custom view (typically full-screen)
     *
     * @return true it the video is being displayed using a custom view (typically full-screen)
     */
    public boolean isVideoFullscreen() {
        return isVideoFullscreen;
    }

    @Override public void onShowCustomView(View view, CustomViewCallback callback) {
        if (view instanceof FrameLayout) {
            // A video wants to be shown
            FrameLayout frameLayout = (FrameLayout) view;
            View focusedChild = frameLayout.getFocusedChild();

            // Save video related variables
            this.isVideoFullscreen = true;
            this.videoViewContainer = frameLayout;
            this.videoViewCallback = callback;

            // Hide the non-video view, add the video view, and show it
            activityNonVideoView.setVisibility(View.INVISIBLE);
            activityVideoView.addView(videoViewContainer,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
            activityVideoView.setVisibility(View.VISIBLE);

            if (focusedChild instanceof android.widget.VideoView) {
                // android.widget.VideoView (typically API level <11)
                android.widget.VideoView videoView = (android.widget.VideoView) focusedChild;

                // Handle all the required events
                videoView.setOnPreparedListener(this);
                videoView.setOnCompletionListener(this);
                videoView.setOnErrorListener(this);
            } else {
                // Other classes, including:
                // - android.webkit.HTML5VideoFullScreen$VideoSurfaceView, which inherits from android.view.SurfaceView (typically API level 11-18)
                // - android.webkit.HTML5VideoFullScreen$VideoTextureView, which inherits from android.view.TextureView (typically API level 11-18)
                // - com.android.org.chromium.content.browser.ContentVideoView$VideoSurfaceView, which inherits from android.view.SurfaceView (typically API level 19+)

                // Handle HTML5 video ended event only if the class is a SurfaceView
                // Test case: TextureView of Sony Xperia T API level 16 doesn't work fullscreen when loading the javascript below
                if (webView != null
                        && webView.getSettings().getJavaScriptEnabled()
                        && focusedChild instanceof SurfaceView) {
                    // Run javascript code that detects the video end and notifies the Javascript interface
                    String js = "javascript:";
                    js += "var _ytrp_html5_video_last;";
                    js += "var _ytrp_html5_video = document.getElementsByTagName('video')[0];";
                    js +=
                            "if (_ytrp_html5_video != undefined && _ytrp_html5_video != _ytrp_html5_video_last) {";
                    {
                        js += "_ytrp_html5_video_last = _ytrp_html5_video;";
                        js += "function _ytrp_html5_video_ended() {";
                        {
                            js +=
                                    "_VideoEnabledWebView.notifyVideoEnd();"; // Must match Javascript interface name and method of VideoEnableWebView
                        }
                        js += "}";
                        js += "_ytrp_html5_video.addEventListener('ended', _ytrp_html5_video_ended);";
                    }
                    js += "}";
                    webView.loadUrl(js);
                }
            }

            // Notify full-screen change
            if (null != toggledFullscreenCallback ) {
                toggledFullscreenCallback.toggledFullscreen(true);
            }
        }
    }

    @SuppressWarnings("deprecation") @Override
    public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
        onShowCustomView(view, callback);
    }

    @Override public void onHideCustomView() {
        // This method should be manually called on video end in all cases because it's not always called automatically.
        // This method must be manually called on back key press (from this class' onBackPressed() method).

        if (isVideoFullscreen) {
            // Hide the video view, remove it, and show the non-video view
            activityVideoView.setVisibility(View.INVISIBLE);
            activityVideoView.removeView(videoViewContainer);
            activityNonVideoView.setVisibility(View.VISIBLE);

            // Call back (only in API level <19, because in API level 19+ with chromium webview it crashes)
            if (videoViewCallback != null && !videoViewCallback.getClass()
                    .getName()
                    .contains(".chromium.")) {
                videoViewCallback.onCustomViewHidden();
            }

            // Reset video related variables
            isVideoFullscreen = false;
            videoViewContainer = null;
            videoViewCallback = null;

            // Notify full-screen change
            if (null != toggledFullscreenCallback ) {
                toggledFullscreenCallback.toggledFullscreen(false);
            }
        }
    }

    @Override public View getVideoLoadingProgressView() // Video will start loading
    {
        if (loadingView != null) {
            loadingView.setVisibility(View.VISIBLE);
            return loadingView;
        } else {
            return super.getVideoLoadingProgressView();
        }
    }

    @Override public void onPrepared(
            MediaPlayer mp) // Video will start playing, only called in the case of android.widget.VideoView (typically API level <11)
    {
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
        if (null != mOnPreparedListener) {
            mOnPreparedListener.onPrepared(mp);
        }
    }

    @Override public void onCompletion(
            MediaPlayer mp) // Video finished playing, only called in the case of android.widget.VideoView (typically API level <11)
    {
        onHideCustomView();
        if (null != mOnCompletionListener) {
            mOnCompletionListener.onCompletion(mp);
        }
    }

    @Override public boolean onError(MediaPlayer mp, int what,
                                     int extra) // Error while playing video, only called in the case of android.widget.VideoView (typically API level <11)
    {
        if (null != mOnErrorListener) {
            mOnErrorListener.onError(mp, what, extra);
        }
        return false; // By returning false, onCompletion() will be called
    }

    /**
     * Notifies the class that the back key has been pressed by the user.
     * This must be called from the Activity's onBackPressed(), and if it returns false, the activity
     * itself should handle it. Otherwise don't do anything.
     *
     * @return Returns true if the event was handled, and false if was not (video view is not visible)
     */
    @SuppressWarnings("unused") public boolean onBackPressed() {
        if (isVideoFullscreen) {
            onHideCustomView();
            return true;
        } else {
            return false;
        }
    }
}
