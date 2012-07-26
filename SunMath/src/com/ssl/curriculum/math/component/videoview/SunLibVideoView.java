package com.ssl.curriculum.math.component.videoview;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.VideoView;
import com.ssl.curriculum.math.R;

public class SunLibVideoView extends VideoView implements MediaPlayerControl {

    private int mForceHeight = 0;
    private int mForceWidth = 0;
    private ViewGroup mFullScreenLayout;
    private SunLibMediaController mMediaController;
    private MediaPlayer mMediaPlayer = null;


    public SunLibVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVideoView();
    }

    public void setDimensions(int w, int h) {
        this.mForceHeight = h;
        this.mForceWidth = w;
    }

    private void initVideoView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFullScreenLayout = (ViewGroup) inflater.inflate(R.layout.video_player_full_screen, null);
    }

    public void setMediaController(SunLibMediaController controller) {
//        if (mMediaController != null) {
//            mMediaController.hide();
//        }
        mMediaController = controller;
        attachMediaController();
        mMediaController.show(0);
    }

    private void attachMediaController() {
        System.out.println("showContextMenu() = " + "show2");
        if (mMediaPlayer != null && mMediaController != null) {
            System.out.println("showContextMenu() = " + "show");
            mMediaController.setMediaPlayer(this);
            View anchorView = this.getParent() instanceof View ?
                    (View)this.getParent() : this;
            mMediaController.setAnchorView(anchorView);
            mMediaController.setEnabled(false);
//            mMediaController.setEnabled(isInPlaybackState());
        }
    }

    private boolean isInPlaybackState() {
        return (mMediaPlayer != null);
    }

    @Override
    public void setVideoURI(Uri uri) {
        openVideo();
        super.setVideoURI(uri);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mForceWidth, mForceHeight);
        this.mMediaController.setPadding(0, 0, 0, 222);
    }

    private View savedContentView;

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
    }

    @Override
    public void setFullscreen(boolean fullscreen) {
        Activity activity = (Activity) getContext();

        if (fullscreen) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            suspend();

            savedContentView = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            ViewGroup container = (ViewGroup) getParent();
            container.removeView(this);
            activity.setContentView(mFullScreenLayout);

            container = (FrameLayout) activity.findViewById(R.id.video_player_full_screen_container);
            container.addView(this);

            resume();
            start();
        } else {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            suspend();

            ViewGroup container = (ViewGroup) getParent();
            container.removeView(this);
            activity.setContentView(savedContentView);
            container = (FrameLayout) activity.findViewById(R.id.video_player_full_screen_container);
            container.addView(this);

            resume();
            start();
        }
    }

    private void openVideo() {

        release(false);
        try {
            mMediaPlayer = new MediaPlayer();
//            mMediaPlayer.setOnPreparedListener(mPreparedListener);
//            mMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
//            mDuration = -1;
//            mMediaPlayer.setOnCompletionListener(mCompletionListener);
//            mMediaPlayer.setOnErrorListener(mErrorListener);
//            mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
//            mCurrentBufferPercentage = 0;
//            mMediaPlayer.setDataSource(getContext(), mUri);
//            mMediaPlayer.setDisplay(mSurfaceHolder);
//            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mMediaPlayer.setScreenOnWhilePlaying(true);
//            mMediaPlayer.prepareAsync();
//            // we don't set the target state here either, but preserve the
//            // target state that was there before.
//            mCurrentState = STATE_PREPARING;
            attachMediaController();
        } catch (Exception ex) {
//            Log.w(TAG, "Unable to open content: " + mUri, ex);
//            mCurrentState = STATE_ERROR;
//            mTargetState = STATE_ERROR;
//            mErrorListener.onError(mMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
//            return;
//        } catch (IllegalArgumentException ex) {
//            Log.w(TAG, "Unable to open content: " + mUri, ex);
//            mCurrentState = STATE_ERROR;
//            mTargetState = STATE_ERROR;
//            mErrorListener.onError(mMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
            return;
        }
    }

    private void release(boolean cleartargetstate) {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
//            mCurrentState = STATE_IDLE;
//            if (cleartargetstate) {
//                mTargetState  = STATE_IDLE;
//            }
        }
    }
}
