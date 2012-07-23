package com.ssl.curriculum.math.component;


import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ViewFlipper;
import com.ssl.curriculum.math.listener.ImageViewerGestureListener;
import com.ssl.curriculum.math.listener.OnViewFlipperListener;

public class ImageViewerFlipper extends ViewFlipper implements ImageViewerGestureListener.OnFlingListener {
  
    private GestureDetector mGestureDetector = null;  
  
    private OnViewFlipperListener mOnViewFlipperListener = null;
  
    public ImageViewerFlipper(Context context) {
        super(context);  
    }  
  
    public ImageViewerFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);  
    }  
  
    public void setOnViewFlipperListener(OnViewFlipperListener mOnViewFlipperListener) {
        this.mOnViewFlipperListener = mOnViewFlipperListener;  
        ImageViewerGestureListener imageViewerGestureListener = new ImageViewerGestureListener();
        imageViewerGestureListener.setOnFlingListener(this);
        mGestureDetector = new GestureDetector(imageViewerGestureListener);
    }  
  
    @Override  
    public boolean onInterceptTouchEvent(MotionEvent ev) {  
        if (null != mGestureDetector) {  
            return mGestureDetector.onTouchEvent(ev);  
        } else {  
            return super.onInterceptTouchEvent(ev);  
        }  
    }  
  
    public void flingToNext() {  
        if (null != mOnViewFlipperListener) {  
            int childCnt = getChildCount();  
            if (childCnt == 2) {  
                removeViewAt(1);  
            }  
            addView(mOnViewFlipperListener.getNextView(), 0);  
            if (0 != childCnt) {  
                setInAnimation(getContext(), android.R.anim.fade_in);  
                setOutAnimation(getContext(), android.R.anim.fade_out);  
                setDisplayedChild(0);  
            }  
        }  
    }  
  
    public void flingToPrevious() {  
        if (null != mOnViewFlipperListener) {  
            int childCnt = getChildCount();  
            if (childCnt == 2) {  
                removeViewAt(1);  
            }  
            addView(mOnViewFlipperListener.getPreviousView(), 0);  
            if (0 != childCnt) {  
                setInAnimation(getContext(), android.R.anim.fade_in);  
                setOutAnimation(getContext(), android.R.anim.fade_out);  
                setDisplayedChild(0);  
            }  
        }  
    }

}
