/*
 *
 *  MIT License
 *
 *  Copyright (c) 2017 Alibaba Group
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 *
 */

package com.taohdao.library.common.widget.ultraviewpager.transformer;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by mikeafc on 15/12/3.
 */
public class UltraDepthScaleTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.5f;
    private static final float MAX_ROTATION = 30;

    @Override
    public void transformPage(View view, float position) {
        final float scale = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
        final float rotation = MAX_ROTATION * Math.abs(position);

        if (position <= 0f) {
            view.setTranslationX(view.getWidth() * -position * 0.19f);
            view.setPivotY(0.5f * view.getHeight());
            view.setPivotX(0.5f * view.getWidth());
            view.setScaleX(scale);
            view.setScaleY(scale);
            view.setRotationY(rotation);
        } else if (position <= 1f) {
            view.setTranslationX(view.getWidth() * -position * 0.19f);
            view.setPivotY(0.5f * view.getHeight());
            view.setPivotX(0.5f * view.getWidth());
            view.setScaleX(scale);
            view.setScaleY(scale);
            view.setRotationY(-rotation);
        }
    }
}
