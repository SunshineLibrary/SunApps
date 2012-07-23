package com.ssl.curriculum.math.listener;
  
import android.view.GestureDetector.SimpleOnGestureListener;  
import android.view.MotionEvent;  
  
public class ImageViewerGestureListener extends SimpleOnGestureListener {
      
    private OnFlingListener mOnFlingListener;  
  
    public OnFlingListener getOnFlingListener() {  
        return mOnFlingListener;  
    }  
  
    public void setOnFlingListener(OnFlingListener mOnFlingListener) {  
        this.mOnFlingListener = mOnFlingListener;  
    }  
  
    @Override  
    public final boolean onFling(final MotionEvent e1, final MotionEvent e2,  
            final float speedX, final float speedY) {  
        if (mOnFlingListener == null) {  
            return super.onFling(e1, e2, speedX, speedY);  
        }  
  
        float XFrom = e1.getX();  
        float XTo = e2.getX();  
        float YFrom = e1.getY();  
        float YTo = e2.getY();  
        if (Math.abs(XFrom - XTo) > 100.0f && Math.abs(speedX) > 100.0f) {
            if (Math.abs(XFrom - XTo) >= Math.abs(YFrom - YTo)) {
                if (XFrom > XTo) {  
                    mOnFlingListener.flingToNext();
                } else {  
                    mOnFlingListener.flingToPrevious();
                }  
            }  
        } else {  
            return false;  
        }  
        return true;  
    }  
  
    public interface OnFlingListener {  
        void flingToNext();  
  
        void flingToPrevious();  
    }  
}  