package com.ssl.curriculum.math.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import com.ssl.curriculum.math.listener.GalleySlideListener;

public class TouchableImageView extends ImageView implements View.OnTouchListener {
    private static final long DOUBLE_PRESS_INTERVAL = 300;

    private static final float ORIGINAL_SCALE = 1.0f;
    private static final float MAX_SCALE = 4.0f;

    private static float IMAGE_VIEW_WIDTH;
    private static float IMAGE_VIEW_HEIGHT;


    private enum State {DRAG, SCALE, NONE}

    private State state = State.NONE;

    private float BITMAP_WIDTH;
    private float BITMAP_HEIGHT;

    private float BITMAP_ORIGIN_ADJUSTED_WIDTH;
    private float BITMAP_ORIGIN_ADJUSTED_HEIGHT;

    private long lastPressTime = 0;
    private float lastTwoPointersDistance;

    private PointF startPointF = new PointF();
    private PointF lastPointF = new PointF();
    private PointF currPointF = new PointF();

    private Matrix matrix = new Matrix();
    private float[] matrixValues = new float[9];

    private float scaleToOrigin = ORIGINAL_SCALE;
    private float lastScaleToOrigin = scaleToOrigin;

    private float transBeginningX;
    private float transBeginningY;

    private float scaledBitmapXDiffToView;
    private float scaledBitmapYDiffToView;

    private boolean isTwoFingersDown;

    private GalleySlideListener galleySlideListener;

    public TouchableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initState();
        initListener();
    }

    private void initState() {
        setClickable(true);
        setScaleType(ScaleType.MATRIX);
    }

    private void initListener() {
        setOnTouchListener(this);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        BITMAP_WIDTH = bm.getWidth();
        BITMAP_HEIGHT = bm.getHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        scaleToFitViewWhenImageLargerThanView(widthMeasureSpec, heightMeasureSpec);
    }

    private void scaleToFitViewWhenImageLargerThanView(int widthMeasureSpec, int heightMeasureSpec) {
        IMAGE_VIEW_WIDTH = MeasureSpec.getSize(widthMeasureSpec);
        IMAGE_VIEW_HEIGHT = MeasureSpec.getSize(heightMeasureSpec);

        float scaleX = IMAGE_VIEW_WIDTH / BITMAP_WIDTH;
        float scaleY = IMAGE_VIEW_HEIGHT / BITMAP_HEIGHT;
        float scale = Math.min(scaleX, scaleY);

        float yHalfSpaceWhenOneSideFitsToView = (IMAGE_VIEW_HEIGHT - (scale * BITMAP_HEIGHT)) / 2.0f;
        float xHalfSpaceWhenOneSideFitsToView = (IMAGE_VIEW_WIDTH - (scale * BITMAP_WIDTH)) / 2.0f;

        BITMAP_ORIGIN_ADJUSTED_WIDTH = IMAGE_VIEW_WIDTH - 2 * xHalfSpaceWhenOneSideFitsToView;
        BITMAP_ORIGIN_ADJUSTED_HEIGHT = IMAGE_VIEW_HEIGHT - 2 * yHalfSpaceWhenOneSideFitsToView;

        matrix.setScale(scale, scale);
        matrix.postTranslate(xHalfSpaceWhenOneSideFitsToView, yHalfSpaceWhenOneSideFitsToView);
        setImageMatrix(matrix);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int action = event.getAction();
        currPointF.set(event.getX(), event.getY());
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                handleSingleActionDown(event);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                handleDoubleActionDown(event);
                break;
            case MotionEvent.ACTION_UP:
                handleActionUp();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                resetState();
                break;
            case MotionEvent.ACTION_MOVE:
                handleActionMove(event);
                break;
        }
        return false;
    }

    private void handleActionMove(MotionEvent event) {
        if (state == State.DRAG && scaleToOrigin != ORIGINAL_SCALE) {
            handleDragAction();
            return;
        }

        if (state == State.DRAG && scaleToOrigin == ORIGINAL_SCALE) {
            handleFlingAction();
            return;
        }

        if (state == State.SCALE) {
            handleScaleAction(event);
        }
    }

    private void handleFlingAction() {
        if (galleySlideListener != null) {
            galleySlideListener.onSlide(lastPointF.x - currPointF.x);
        }
    }

    private void resetState() {
        state = State.NONE;
    }

    private void handleScaleAction(MotionEvent event) {
        float newDist = getDistanceBetweenTwoPointers(event);
        if (event.getPointerCount() < 2) return;
        float scale = newDist / lastTwoPointersDistance;
        lastTwoPointersDistance = newDist;
        scale = calculateScale(scale);
        zoomImage(scale);
    }

    private void zoomImage(float calculatedScale) {
        final float bitMapCurrentScaledWidth = scaleToOrigin * BITMAP_ORIGIN_ADJUSTED_WIDTH;
        final float bitMapCurrentScaledHeight = scaleToOrigin * BITMAP_ORIGIN_ADJUSTED_HEIGHT;

        calculateDiff();

        if (isBitmapScaledBoundSmallerThanViewBound(bitMapCurrentScaledWidth, bitMapCurrentScaledHeight)) {
            scaleInsideViewBounds(calculatedScale);
            return;
        }
        scaleOutsideViewBounds(calculatedScale);
    }

    private void handleDoubleActionDown(MotionEvent event) {
        isTwoFingersDown = true;
        lastTwoPointersDistance = getDistanceBetweenTwoPointers(event);
        if (lastTwoPointersDistance > 10f) {
            state = State.SCALE;
        }
    }

    private void handleDragAction() {
        fetchMatrixValues();
        float deltaX = currPointF.x - lastPointF.x;
        float deltaY = currPointF.y - lastPointF.y;
        lastPointF.set(currPointF);

        final float bitMapCurrentScaledWidth = Math.round(scaleToOrigin * BITMAP_ORIGIN_ADJUSTED_WIDTH);
        final float bitMapCurrentScaledHeight = Math.round(scaleToOrigin * BITMAP_ORIGIN_ADJUSTED_HEIGHT);

        if (bitMapCurrentScaledWidth > IMAGE_VIEW_HEIGHT && bitMapCurrentScaledHeight <= IMAGE_VIEW_HEIGHT) {
            deltaY = 0;
            deltaX = adjustXDelta(deltaX);
        } else if (bitMapCurrentScaledWidth <= IMAGE_VIEW_WIDTH && bitMapCurrentScaledHeight > IMAGE_VIEW_HEIGHT) {
            deltaX = 0;
            deltaY = adjustYDelta(deltaY);
        } else {
            deltaX = adjustXDelta(deltaX);
            deltaY = adjustYDelta(deltaY);
        }
        matrix.postTranslate(deltaX, deltaY);
        redrawImage();
    }

    private float adjustYDelta(float deltaY) {
        if (isBitmapBottomEdgeVisible(deltaY)) {
            deltaY = -(transBeginningY + scaledBitmapYDiffToView);
        }
        if (isBitmapTopEdgeVisible(deltaY)) {
            deltaY = -transBeginningY;
        }
        return deltaY;
    }

    private float adjustXDelta(float deltaX) {
        if (isBitmapRightEdgeVisible(deltaX)) {
            deltaX = -(transBeginningX + scaledBitmapXDiffToView);
        }
        if (isBitmapLeftEdgeVisible(deltaX)) {
            deltaX = -transBeginningX;
        }
        return deltaX;
    }

    private boolean isBitmapLeftEdgeVisible(float deltaX) {
        return transBeginningX + deltaX > 0;
    }

    private boolean isBitmapRightEdgeVisible(float deltaX) {
        return transBeginningX + deltaX < -scaledBitmapXDiffToView;
    }

    private boolean isBitmapTopEdgeVisible(float deltaY) {
        return transBeginningY + deltaY > 0;
    }

    private boolean isBitmapBottomEdgeVisible(float deltaY) {
        return transBeginningY + deltaY < -scaledBitmapYDiffToView;
    }


    private void handleActionUp() {
        state = State.NONE;
        long actionUpTime = System.currentTimeMillis();
        if ((actionUpTime - lastPressTime) <= DOUBLE_PRESS_INTERVAL && !isTwoFingersDown) {
            onDoubleClick();
            return;
        }
        lastPressTime = actionUpTime;
    }

    private void onDoubleClick() {
        if (scaleToOrigin == ORIGINAL_SCALE) {
            scaleToMax();
            return;
        }
        scaleToOrigin();
    }

    private void scaleToOrigin() {
        float calculatedScale = ORIGINAL_SCALE / scaleToOrigin;
        scaleToOrigin = ORIGINAL_SCALE;
        lastScaleToOrigin = scaleToOrigin;
        zoomImage(calculatedScale);
    }

    private void scaleToMax() {
        scaleToOrigin = MAX_SCALE;
        lastScaleToOrigin = scaleToOrigin;
        zoomImage(scaleToOrigin);
    }

    private void scaleOutsideViewBounds(float scale) {
        matrix.postScale(scale, scale, startPointF.x, startPointF.y);
        if (isZoomOut(scale)) {
            fillViewByBitmapWhenBitmapBiggerThanView();
        }
        redrawImage();
    }

    private void fillViewByBitmapWhenBitmapBiggerThanView() {
        fetchMatrixValues();
        if (transBeginningX < -scaledBitmapXDiffToView) {
            matrix.postTranslate(-(transBeginningX + scaledBitmapXDiffToView), 0);
        } else if (transBeginningX > 0) {
            matrix.postTranslate(-transBeginningX, 0);
        }

        if (transBeginningY < -scaledBitmapYDiffToView) {
            matrix.postTranslate(0, -(transBeginningY + scaledBitmapYDiffToView));
        } else if (transBeginningY > 0) {
            matrix.postTranslate(0, -transBeginningY);
        }
    }

    private void scaleInsideViewBounds(float scale) {
        matrix.postScale(scale, scale, IMAGE_VIEW_WIDTH / 2.0f, IMAGE_VIEW_HEIGHT / 2.0f);
        if (isZoomOut(scale)) {
            translateToViewBoundsInside();
        }
        redrawImage();
    }

    private void translateToViewBoundsInside() {
        fetchMatrixValues();
        if (Math.abs(transBeginningX + scaledBitmapXDiffToView / 2) > 0.5f) {
            matrix.postTranslate(-(transBeginningX + scaledBitmapXDiffToView / 2), 0);
        }
        if (Math.abs(transBeginningY + scaledBitmapYDiffToView / 2) > 0.5f) {
            matrix.postTranslate(0, -(transBeginningY + scaledBitmapYDiffToView / 2));
        }
    }

    private boolean isZoomOut(float scale) {
        return scale < 1;
    }

    private boolean isBitmapScaledBoundSmallerThanViewBound(float bitMapCurrentScaledWidth, float bitMapCurrentScaledHeight) {
        return bitMapCurrentScaledHeight <= IMAGE_VIEW_HEIGHT || bitMapCurrentScaledWidth <= IMAGE_VIEW_WIDTH;
    }

    private float calculateScale(float currentScale) {
        scaleToOrigin *= currentScale;
        if (scaleToOrigin >= MAX_SCALE) {
            scaleToOrigin = MAX_SCALE;
        }
        if (scaleToOrigin < ORIGINAL_SCALE) {
            scaleToOrigin = ORIGINAL_SCALE;
        }
        float scale = scaleToOrigin / lastScaleToOrigin;
        lastScaleToOrigin = scaleToOrigin;
        return scale;
    }

    private void calculateDiff() {
        scaledBitmapXDiffToView = BITMAP_ORIGIN_ADJUSTED_WIDTH * scaleToOrigin - IMAGE_VIEW_WIDTH;
        scaledBitmapYDiffToView = BITMAP_ORIGIN_ADJUSTED_HEIGHT * scaleToOrigin - IMAGE_VIEW_HEIGHT;
    }

    private void redrawImage() {
        setImageMatrix(matrix);
        invalidate();
    }

    private void fetchMatrixValues() {
        matrix.getValues(matrixValues);
        transBeginningX = matrixValues[Matrix.MTRANS_X];
        transBeginningY = matrixValues[Matrix.MTRANS_Y];
    }

    private void handleSingleActionDown(MotionEvent event) {
        state = State.DRAG;
        isTwoFingersDown = false;
        startPointF.set(event.getX(), event.getY());
        lastPointF.set(startPointF);
    }

    private float getDistanceBetweenTwoPointers(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    public void setGallerySlideListener(GalleySlideListener galleySlideListener) {
        this.galleySlideListener = galleySlideListener;
    }

}
