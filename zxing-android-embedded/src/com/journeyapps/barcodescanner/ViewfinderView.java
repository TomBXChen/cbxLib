/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.journeyapps.barcodescanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder rectangle and partial
 * transparency outside it, as well as the laser scanner animation and result points.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public class ViewfinderView extends View {
    protected static final String TAG = ViewfinderView.class.getSimpleName();

    protected static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
    protected static final long ANIMATION_DELAY = 80L;
    protected static final int CURRENT_POINT_OPACITY = 0xA0;
    protected static final int MAX_RESULT_POINTS = 20;
    protected static final int POINT_SIZE = 6;

    protected final Paint paint;
    protected Bitmap resultBitmap;
    protected final int maskColor;
    protected final int resultColor;
    protected final int laserColor;
    protected final int resultPointColor;
    protected int scannerAlpha;
    protected List<ResultPoint> possibleResultPoints;
    protected List<ResultPoint> lastPossibleResultPoints;
    protected CameraPreview cameraPreview;

    // Cache the framingRect and previewFramingRect, so that we can still draw it after the preview
    // stopped.
    protected Rect framingRect;
    protected Rect previewFramingRect;

    //cyan custom
    private String mTipText = "将二维码/条形码放入框内，即可自动扫描";
    private StaticLayout mTipTextSl;
    private TextPaint mTipPaint;
    private int mTipTextSize;
    private int mTipTextColor;
    private int mTipBackgroundRadius;
    private int mTipTextMargin;

    private int mAnimTime = 1000;
    private int mAnimDelayTime = -1;
    private int mMoveStepDistance;
    private float mScanLineTop;
    private float mScanLineTopTemp;
    private int mScanLineSize;
    private int mCornerSize;
    private int mCornerLength;
    private float mHalfCornerSize;
    private int mScanLineMargin = 0;
    private int mTipBackgroundColor;


    // This constructor is used when the class is built from an XML resource.
    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCornerSize = dp2px(context, 3);
        mCornerLength = dp2px(context, 20);
        mTipTextMargin = dp2px(context, 20);
        mScanLineSize = dp2px(context, 1);
        mMoveStepDistance = dp2px(context, 2);
        mTipBackgroundColor = Color.parseColor("#22000000");
        mTipTextSize = sp2px(context, 14);
        mTipTextColor = Color.WHITE;
        mTipBackgroundRadius = dp2px(context, 4);
        // Initialize these once for performance rather than calling them every time in onDraw().
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        Resources resources = getResources();

        // Get setted attributes on view
        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.zxing_finder);

        this.maskColor = attributes.getColor(R.styleable.zxing_finder_zxing_viewfinder_mask,
                resources.getColor(R.color.zxing_viewfinder_mask));
        this.resultColor = attributes.getColor(R.styleable.zxing_finder_zxing_result_view,
                resources.getColor(R.color.zxing_result_view));
        this.laserColor = attributes.getColor(R.styleable.zxing_finder_zxing_viewfinder_laser,
                resources.getColor(R.color.zxing_viewfinder_laser));
        this.resultPointColor = attributes.getColor(R.styleable.zxing_finder_zxing_possible_result_points,
                resources.getColor(R.color.zxing_possible_result_points));
        mTipText =  attributes.getString(R.styleable.zxing_finder_zxing_viewfinder_tip);
        if(TextUtils.isEmpty(mTipText)){
            mTipText  = "将二维码/条形码放入框内，即可自动扫描";
        }
        attributes.recycle();

        scannerAlpha = 0;
        possibleResultPoints = new ArrayList<>(5);
        lastPossibleResultPoints = null;

        mHalfCornerSize = 1.0f * mCornerSize / 2;

        mTipPaint = new TextPaint();
        mTipPaint.setAntiAlias(true);
        mTipPaint.setTextSize(mTipTextSize);
        mTipPaint.setColor(mTipTextColor);

    }

    public void setCameraPreview(CameraPreview view) {
        this.cameraPreview = view;
        view.addStateListener(new CameraPreview.StateListener() {
            @Override
            public void previewSized() {
                refreshSizes();
                invalidate();
            }

            @Override
            public void previewStarted() {

            }

            @Override
            public void previewStopped() {

            }

            @Override
            public void cameraError(Exception error) {

            }
        });
    }

    protected void refreshSizes() {
        if (cameraPreview == null) {
            return;
        }
        Rect framingRect = cameraPreview.getFramingRect();
        Rect previewFramingRect = cameraPreview.getPreviewFramingRect();
        if (framingRect != null && previewFramingRect != null) {
            this.framingRect = framingRect;
            this.previewFramingRect = previewFramingRect;
            mScanLineTop = mScanLineTopTemp + framingRect.top;
            if (mAnimDelayTime == -1) {
                mAnimDelayTime = (int) ((1.0f * mAnimTime * mMoveStepDistance) / framingRect.height());
            }
            if (mTipTextSl == null) {
                mTipTextSl = new StaticLayout(mTipText, mTipPaint, framingRect.width() - 2 * mTipBackgroundRadius, Layout.Alignment.ALIGN_CENTER, 1.0f, 0, true);
            }
        }
    }

//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//        calFramingRect();
//    }
//
//    private void calFramingRect() {
//        if (framingRect != null) {
//            mScanLineTop = framingRect.top;
//        }
//    }

    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {
        refreshSizes();
        if (framingRect == null || previewFramingRect == null) {
            return;
        }

        Rect frame = framingRect;
        Rect previewFrame = previewFramingRect;

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // Draw the exterior (i.e. outside the framing rect) darkened
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(resultBitmap != null ? resultColor : maskColor);
        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);
        drawCornerLine(canvas);
        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            paint.setAlpha(CURRENT_POINT_OPACITY);
            canvas.drawBitmap(resultBitmap, null, frame, paint);
        } else {

            // Draw a red "laser scanner" line through the middle to show decoding is active
            drawScanLine(canvas);
            /*
            paint.setColor(laserColor);
            paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
            scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
            int middle = frame.height() / 2 + frame.top;
            canvas.drawRect(frame.left + 2, middle - 1, frame.right - 1, middle + 2, paint);
            */
            float scaleX = frame.width() / (float) previewFrame.width();
            float scaleY = frame.height() / (float) previewFrame.height();

            List<ResultPoint> currentPossible = possibleResultPoints;
            List<ResultPoint> currentLast = lastPossibleResultPoints;
            int frameLeft = frame.left;
            int frameTop = frame.top;
            if (currentPossible.isEmpty()) {
                lastPossibleResultPoints = null;
            } else {
                possibleResultPoints = new ArrayList<>(5);
                lastPossibleResultPoints = currentPossible;
                paint.setAlpha(CURRENT_POINT_OPACITY);
                paint.setColor(resultPointColor);
                for (ResultPoint point : currentPossible) {
                    canvas.drawCircle(frameLeft + (int) (point.getX() * scaleX),
                            frameTop + (int) (point.getY() * scaleY),
                            POINT_SIZE, paint);
                }
            }
            if (currentLast != null) {
                paint.setAlpha(CURRENT_POINT_OPACITY / 2);
                paint.setColor(resultPointColor);
                float radius = POINT_SIZE / 2.0f;
                for (ResultPoint point : currentLast) {
                    canvas.drawCircle(frameLeft + (int) (point.getX() * scaleX),
                            frameTop + (int) (point.getY() * scaleY),
                            radius, paint);
                }
            }
            // 画提示文本
            drawTipText(canvas);
            moveScanLine();
            // Request another update at the animation interval, but only repaint the laser line,
            // not the entire viewfinder mask.
//            postInvalidateDelayed(ANIMATION_DELAY,
//                    frame.left - POINT_SIZE,
//                    frame.top - POINT_SIZE,
//                    frame.right + POINT_SIZE,
//                    frame.bottom + POINT_SIZE);
        }
    }

    public void drawViewfinder() {
        Bitmap resultBitmap = this.resultBitmap;
        this.resultBitmap = null;
        if (resultBitmap != null) {
            resultBitmap.recycle();
        }
        invalidate();
    }

    /**
     * 画四个直角的线
     *
     * @param canvas
     */
    private void drawCornerLine(Canvas canvas) {
        if (mHalfCornerSize > 0) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(laserColor);
            paint.setStrokeWidth(mCornerSize);
            canvas.drawLine(framingRect.left - mHalfCornerSize, framingRect.top, framingRect.left - mHalfCornerSize + mCornerLength, framingRect.top, paint);
            canvas.drawLine(framingRect.left, framingRect.top - mHalfCornerSize, framingRect.left, framingRect.top - mHalfCornerSize + mCornerLength, paint);
            canvas.drawLine(framingRect.right + mHalfCornerSize, framingRect.top, framingRect.right + mHalfCornerSize - mCornerLength, framingRect.top, paint);
            canvas.drawLine(framingRect.right, framingRect.top - mHalfCornerSize, framingRect.right, framingRect.top - mHalfCornerSize + mCornerLength, paint);

            canvas.drawLine(framingRect.left - mHalfCornerSize, framingRect.bottom, framingRect.left - mHalfCornerSize + mCornerLength, framingRect.bottom, paint);
            canvas.drawLine(framingRect.left, framingRect.bottom + mHalfCornerSize, framingRect.left, framingRect.bottom + mHalfCornerSize - mCornerLength, paint);
            canvas.drawLine(framingRect.right + mHalfCornerSize, framingRect.bottom, framingRect.right + mHalfCornerSize - mCornerLength, framingRect.bottom, paint);
            canvas.drawLine(framingRect.right, framingRect.bottom + mHalfCornerSize, framingRect.right, framingRect.bottom + mHalfCornerSize - mCornerLength, paint);
        }
    }

    /**
     * Draw a bitmap with the result points highlighted instead of the live scanning display.
     *
     * @param result An image of the result.
     */
    public void drawResultBitmap(Bitmap result) {
        resultBitmap = result;
        invalidate();
    }

    /**
     * Only call from the UI thread.
     *
     * @param point a point to draw, relative to the preview frame
     */
    public void addPossibleResultPoint(ResultPoint point) {
        List<ResultPoint> points = possibleResultPoints;
        points.add(point);
        int size = points.size();
        if (size > MAX_RESULT_POINTS) {
            // trim it
            points.subList(0, size - MAX_RESULT_POINTS / 2).clear();
        }
    }

    /**
     * 画扫描线
     */
    private void drawScanLine(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(laserColor);
        canvas.drawRect(framingRect.left + mHalfCornerSize + mScanLineMargin, mScanLineTop, framingRect.right - mHalfCornerSize - mScanLineMargin, mScanLineTop + mScanLineSize, paint);
    }

    /**
     * 移动扫描线的位置
     */
    private void moveScanLine() {
        mScanLineTopTemp += mMoveStepDistance;
        int scanLineSize = mScanLineSize;
//        Log.e("mScanLineTop",mScanLineTopTemp+"");
        // || mScanLineTop < framingRect.top + mHalfCornerSize
        if (mScanLineTop + scanLineSize > framingRect.bottom - mHalfCornerSize) {
            mScanLineTopTemp = 0;
            mScanLineTop = framingRect.top;
        }

        postInvalidateDelayed(mAnimDelayTime, framingRect.left, framingRect.top, framingRect.right, framingRect.bottom);
    }

    /**
     * 画提示文本
     *
     * @param canvas
     */
    private void drawTipText(Canvas canvas) {
        if (TextUtils.isEmpty(mTipText) || mTipTextSl == null) {
            return;
        }
        paint.setColor(mTipBackgroundColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(new RectF(framingRect.left, framingRect.top - mTipTextMargin - mTipTextSl.getHeight() - mTipBackgroundRadius, framingRect.right, framingRect.top - mTipTextMargin + mTipBackgroundRadius), mTipBackgroundRadius, mTipBackgroundRadius, paint);
        canvas.save();
        canvas.translate(framingRect.left + mTipBackgroundRadius, framingRect.top - mTipTextMargin - mTipTextSl.getHeight());
        mTipTextSl.draw(canvas);
        canvas.restore();
    }

    public static int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }
}
