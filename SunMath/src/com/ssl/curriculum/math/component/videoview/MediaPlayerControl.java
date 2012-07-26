package com.ssl.curriculum.math.component.videoview;

public interface MediaPlayerControl extends android.widget.MediaController.MediaPlayerControl {
    void setVolume(float leftVolume, float rightVolume);

    void setFullscreen(boolean fullscreen);
}
