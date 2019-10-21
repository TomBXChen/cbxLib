package uk.co.senab.photoview;

import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Provided default implementation of UKGestureDetector.OnDoubleTapListener, to be overridden with custom behavior, if needed
 * <p>&nbsp;</p>
 * To be used via {@link UKPhotoViewAttacher#setOnDoubleTapListener(GestureDetector.OnDoubleTapListener)}
 */
public class UKDefaultOnDoubleTapListener implements GestureDetector.OnDoubleTapListener {

    private UKPhotoViewAttacher photoViewAttacher;

    /**
     * Default constructor
     *
     * @param photoViewAttacher UKPhotoViewAttacher to bind to
     */
    public UKDefaultOnDoubleTapListener(UKPhotoViewAttacher photoViewAttacher) {
        setPhotoViewAttacher(photoViewAttacher);
    }

    /**
     * Allows to change UKPhotoViewAttacher within range of single instance
     *
     * @param newPhotoViewAttacher UKPhotoViewAttacher to bind to
     */
    public void setPhotoViewAttacher(UKPhotoViewAttacher newPhotoViewAttacher) {
        this.photoViewAttacher = newPhotoViewAttacher;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        if (this.photoViewAttacher == null)
            return false;

        ImageView imageView = photoViewAttacher.getImageView();

        if (null != photoViewAttacher.getOnPhotoTapListener()) {
            final RectF displayRect = photoViewAttacher.getDisplayRect();

            if (null != displayRect) {
                final float x = e.getX(), y = e.getY();

                // Check to see if the user tapped on the photo
                if (displayRect.contains(x, y)) {

                    float xResult = (x - displayRect.left)
                            / displayRect.width();
                    float yResult = (y - displayRect.top)
                            / displayRect.height();

                    photoViewAttacher.getOnPhotoTapListener().onPhotoTap(imageView, xResult, yResult);
                    return true;
                }else{
                    photoViewAttacher.getOnPhotoTapListener().onOutsidePhotoTap();
                }
            }
        }
        if (null != photoViewAttacher.getOnViewTapListener()) {
            photoViewAttacher.getOnViewTapListener().onViewTap(imageView, e.getX(), e.getY());
        }

        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent ev) {
        if (photoViewAttacher == null)
            return false;

        try {
            float scale = photoViewAttacher.getScale();
            float x = ev.getX();
            float y = ev.getY();

            if (scale < photoViewAttacher.getMediumScale()) {
                photoViewAttacher.setScale(photoViewAttacher.getMediumScale(), x, y, true);
            } else if (scale >= photoViewAttacher.getMediumScale() && scale < photoViewAttacher.getMaximumScale()) {
                photoViewAttacher.setScale(photoViewAttacher.getMaximumScale(), x, y, true);
            } else {
                photoViewAttacher.setScale(photoViewAttacher.getMinimumScale(), x, y, true);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            // Can sometimes happen when getX() and getY() is called
        }

        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        // Wait for the confirmed onDoubleTap() instead
        return false;
    }

}
