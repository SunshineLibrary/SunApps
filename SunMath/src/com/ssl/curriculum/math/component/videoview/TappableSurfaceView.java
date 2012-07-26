package com.ssl.curriculum.math.component.videoview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;

public class TappableSurfaceView extends SurfaceView {
    private ArrayList<TapListener> listeners = new ArrayList<TapListener>();
    private GestureDetector gesture = null;

    public TappableSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            gestureListener.onSingleTapUp(event);
        }

        return (true);
    }

    public void addTapListener(TapListener l) {
        listeners.add(l);
    }

    public void removeTapListener(TapListener l) {
        listeners.remove(l);
    }

    private GestureDetector.SimpleOnGestureListener gestureListener =
            new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    for (TapListener l : listeners) {
                        l.onTap(e);
                    }
                    return (true);
                }
            };

    public interface TapListener {
        void onTap(MotionEvent event);
    }
}
