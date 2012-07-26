package com.ssl.curriculum.math.task;

import com.ssl.curriculum.math.component.GalleryFloatingPanel;

import java.util.Timer;
import java.util.TimerTask;

public class GalleryFloatingTask {
    private static final int PANEL_FLOATING_MILLI_SECOND = 5000;
    private GalleryFloatingPanel galleryFloatingPanel;
    private Timer timer;
    

    public GalleryFloatingTask(GalleryFloatingPanel galleryFloatingPanel) {
        this.galleryFloatingPanel = galleryFloatingPanel;
        timer = new Timer();
    }

    public void scheduleFlipAway() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
              galleryFloatingPanel.flipAway();
            }
        }, PANEL_FLOATING_MILLI_SECOND);
    }

    public void reset() {
        recycle();
        timer = new Timer();
        scheduleFlipAway();
    }

    private void recycle() {
        timer.cancel();
        timer.purge();
    }
}
