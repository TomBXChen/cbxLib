package com.taohdao.widget.slider_page.transformers;

import android.support.v4.view.ViewPager;
import android.view.View;


/**
 * This is all transformers father.
 * <p>
 * BaseTransformer implement ViewPager.PageTransformer
 * which is just same as {@link ViewPager.PageTransformer}.
 * <p>
 * if you want to make an acceptable transformer, please do not forget to extend from this class.
 */
public abstract class BaseTransformer implements ViewPager.PageTransformer {

    /**
     * Called each {@link #transformPage(View, float)}.
     *
     * @param view
     * @param position
     */
    protected abstract void onTransform(View view, float position);


    /**
     * Apply a property transformation to the given page. For most use cases, this method should not be overridden.
     * Instead use {@link #transformPage(android.view.View, float)} to perform typical transformations.
     *
     * @param page     Apply the transformation to this page
     * @param position Position of page relative to the current front-and-center position of the pager. 0 is front and
     *                 center. 1 is one full page position to the right, and -1 is one page position to the left.
     */
    @Override
    public void transformPage(View page, float position) {
        float clampedPosition = clampPosition(position);

        onPreTransform(page, clampedPosition);
        onTransform(page, clampedPosition);
        onPostTransform(page, clampedPosition);
    }

    /**
     * Clamp the position. This step is required for some Android 4 devices.
     * The position is dependant on the range of the ViewPager and whether it supports infinite scrolling in both directions.
     *
     * @param position Position of page relative to the current front-and-center position of the pager.
     * @return A value between -1 and 1
     */
    private float clampPosition(float position) {
        if (position < -1f) {
            return -1f;
        } else if (position > 1f) {
            return 1f;
        }
        return position;
    }

    /**
     * If the position offset of a fragment is less than negative one or greater than one, returning true will set the
     * fragment alpha to 0f. Otherwise fragment alpha is always defaulted to 1f.
     *
     * @return
     */
    protected boolean hideOffscreenPages() {
        return true;
    }

    /**
     * Indicates if the default animations of the view pager should be used.
     *
     * @return
     */
    protected boolean isPagingEnabled() {
        return false;
    }

    /**
     * Called each {@link #transformPage(android.view.View, float)} before {{@link #onTransform(android.view.View, float)}.
     * <p>
     * The default implementation attempts to reset all view properties. This is useful when toggling transforms that do
     * not modify the same page properties. For instance changing from a transformation that applies rotation to a
     * transformation that fades can inadvertently leave a fragment stuck with a rotation or with some degree of applied
     * alpha.
     *
     * @param page     Apply the transformation to this page
     * @param position Position of page relative to the current front-and-center position of the pager. 0 is front and
     *                 center. 1 is one full page position to the right, and -1 is one page position to the left.
     */
    protected void onPreTransform(View page, float position) {
        final float width = page.getWidth();

        page.setRotationX(0);
        page.setRotationY(0);
        page.setRotation(0);
        page.setScaleX(1);
        page.setScaleY(1);
        page.setPivotX(0);
        page.setPivotY(0);
        page.setTranslationY(0);
        page.setTranslationX(isPagingEnabled() ? 0f : -width * position);

        if (hideOffscreenPages()) {
            page.setAlpha(position <= -1f || position >= 1f ? 0f : 1f);
            page.setEnabled(false);
        } else {
            page.setEnabled(true);
            page.setAlpha(1f);
        }
    }

    /**
     * Called each {@link #transformPage(android.view.View, float)} after {@link #onTransform(android.view.View, float)}.
     *
     * @param page     Apply the transformation to this page
     * @param position Position of page relative to the current front-and-center position of the pager. 0 is front and
     *                 center. 1 is one full page position to the right, and -1 is one page position to the left.
     */
    protected void onPostTransform(View page, float position) {
    }

    /**
     * Same as {@link Math#min(double, double)} without double casting, zero closest to infinity handling, or NaN support.
     *
     * @param val
     * @param min
     * @return
     */
    protected static final float min(float val, float min) {
        return val < min ? min : val;
    }

}