package com.ssl.curriculum.math.component.videoview;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.ssl.curriculum.math.R;

public class SunLibMediaController extends MediaController {

    private com.ssl.curriculum.math.component.videoview.MediaPlayerControl mPlayer;
    private Context mContext;
    private View mAnchor;
    private View mRoot;
    private WindowManager mWindowManager;
    //        private Window mWindow;
    private View mDecor;
    private ProgressBar mProgress;
    private boolean mShowing;
    private boolean mDragging;
    private static final int sDefaultTimeout = 3000;
    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    private ImageButton mPauseButton;

    private VerticalProgressBar mVolumeLevel;
    private ImageButton mMuteButton;
    private float mMuteSavedVolume = 0;
    private VerticalProgressBar mBrightnessLevel;
    private ViewFlipper mRightButtons;
    private ImageButton mBrightnessButton;
    private ImageButton mVolumeButton;
    private GestureDetector mGestureDetector;

    private static final int FLIPPER_CHILD_VOLUME = 0;
    private static final int FLIPPER_CHILD_BRIGHTNESS = 1;
    private PopupWindow mWindow;
//
//    public SunLibMediaController(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        mRoot = this;
//        mContext = context;
//    }
//
//    @Override
//    public void onFinishInflate() {
//        if (mRoot != null)
//            initControllerView(mRoot);
//    }
//
//    public SunLibMediaController(Context context, boolean useFastForward) {
//        super(context);
//        mContext = context;
//        initFloatingWindow();
//    }

    public SunLibMediaController(Context context) {
        super(context);
        mContext = context;
        initFloatingWindow();
    }

    private void initFloatingWindow() {
        mGestureDetector = new GestureDetector(mContext, mGestureListener);
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
//            mWindow = PolicyManager.makeNewWindow(mContext);
        mWindow = new PopupWindow(mContext);
//            mWindow.setWindowManager(mWindowManager, null, null);
//            mWindow.requestFeature(Window.FEATURE_NO_TITLE);
//            mDecor = mWindow.getDecorView();
//            mDecor.setOnTouchListener(mTouchListener);
//            mWindow.setContentView(this);
//            mWindow.setBackgroundDrawableResource(android.R.color.transparent);
//
//            // While the media controller is up, the volume control keys should
//            // affect the media stream type
//            mWindow.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        setFocusable(true);
        setFocusableInTouchMode(true);
        setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        requestFocus();
    }

//    private OnTouchListener mTouchListener = new OnTouchListener() {
//        public boolean onTouch(View v, MotionEvent event) {
//            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                if (mShowing) {
//                    hide();
//                }
//            }
//            return false;
//        }
//    };

    public void setMediaPlayer(com.ssl.curriculum.math.component.videoview.MediaPlayerControl player) {
        mPlayer = player;
        updatePausePlay();
        initVolumeLevel();
    }

    @Override
    public void setAnchorView(View view) {
        mAnchor = view;

        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        removeAllViews();
        View v = makeControllerView();
        addView(v, frameParams);
    }

    protected View makeControllerView() {
        LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRoot = inflate.inflate(R.layout.video_media_contoller, null);

        initControllerView(mRoot);

        return mRoot;
    }

    private void initControllerView(View v) {
        mPauseButton = (ImageButton) v.findViewById(R.id.pause);
        if (mPauseButton != null) {
            mPauseButton.requestFocus();
            mPauseButton.setOnClickListener(mPauseListener);
        }

        mMuteButton = (ImageButton) v.findViewById(R.id.mute);
        if (mMuteButton != null) {
            mMuteButton.setOnClickListener(mMuteListener);
        }

        mProgress = (ProgressBar) v.findViewById(R.id.mediacontroller_progress);
        if (mProgress != null) {
            if (mProgress instanceof SeekBar) {
                SeekBar seeker = (SeekBar) mProgress;
                seeker.setOnSeekBarChangeListener(mSeekListener);
            }
            mProgress.setMax(1000);
        }

        mVolumeLevel = (VerticalProgressBar) v.findViewById(R.id.volumeBar);
        if (mVolumeLevel != null) {
            if (mVolumeLevel instanceof VerticalSeekBar) {
                VerticalSeekBar volume = (VerticalSeekBar) mVolumeLevel;
                volume.setOnSeekBarChangeListener(mVolumeLevelListener);
            }
            mVolumeLevel.setMax(100);
            mVolumeLevel.setProgress(50);
        }

        mBrightnessLevel = (VerticalProgressBar) v.findViewById(R.id.brightnessBar);
        if (mBrightnessLevel != null) {
            if (mBrightnessLevel instanceof VerticalSeekBar) {
                VerticalSeekBar brightness = (VerticalSeekBar) mBrightnessLevel;
                brightness.setOnSeekBarChangeListener(mBrightnessLevelListener);
            }
            Settings.System.putInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL); // TODO возвращать исходное значение после выхода из приложения
            mBrightnessLevel.setMax(100);
            mBrightnessLevel.setProgress((int) (getWindow().getAttributes().screenBrightness * 100));
        }

        mRightButtons = (ViewFlipper) v.findViewById(R.id.rightButtons);
        if (mRightButtons != null) {
            mBrightnessButton = (ImageButton) v.findViewById(R.id.brightness);
            if (mBrightnessButton != null) {
                mBrightnessButton.setOnClickListener(mBrightnessListener);
            }

            mVolumeButton = (ImageButton) v.findViewById(R.id.volume);
            if (mVolumeButton != null) {
                mVolumeButton.setOnClickListener(mVolumeListener);
            }
        }
    }

    private Window getWindow() {
        return mContext == null ? null : ((Activity) mContext).getWindow();
    }

    public void show() {
        show(sDefaultTimeout);
    }

    private void disableUnsupportedButtons() {
        try {
            if (mPauseButton != null && !mPlayer.canPause()) {
                mPauseButton.setEnabled(false);
            }
        } catch (IncompatibleClassChangeError ex) {
            // We were given an old version of the interface, that doesn't have
            // the canPause/canSeekXYZ methods. This is OK, it just means we
            // assume the media can be paused and seeked, and so we don't disable
            // the buttons.
        }
    }

    public void show(int timeout) {
        if (!mShowing && mAnchor != null) {
            setProgress();
            if (mPauseButton != null) {
                mPauseButton.requestFocus();
            }
            disableUnsupportedButtons();

            int[] anchorpos = new int[2];
            mAnchor.getLocationOnScreen(anchorpos);

            WindowManager.LayoutParams p = new WindowManager.LayoutParams();
            p.gravity = Gravity.TOP | Gravity.LEFT;
            p.width = mAnchor.getWidth();
            p.height = mAnchor.getHeight();
            p.x = anchorpos[0] + mAnchor.getWidth() - p.width;
            p.y = anchorpos[1] + mAnchor.getHeight() - p.height;
            p.format = PixelFormat.TRANSLUCENT;
            p.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
            p.flags |= WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
            p.token = null;
            p.windowAnimations = 0; // android.R.style.DropDownAnimationDown;

            if (mAnchor.getWidth() < 768) {
                scaleDrawables(true);
            } else {
                scaleDrawables(false);
            }

            mShowing = true;
        }
        super.show(timeout);

        updatePausePlay();

        // cause the progress bar to be updated even if mShowing
        // was already true.  This happens, for example, if we're
        // paused with the progress bar showing the user hits play.
        mHandler.sendEmptyMessage(SHOW_PROGRESS);

        Message msg = mHandler.obtainMessage(FADE_OUT);
        if (timeout != 0) {
            mHandler.removeMessages(FADE_OUT);
            mHandler.sendMessageDelayed(msg, timeout);
        }
    }

    private void scaleDrawables(boolean small) {
        scaleDrawable(mPauseButton, small);
        scaleDrawable(mBrightnessButton, small);
        scaleDrawable(mMuteButton, small);
        scaleDrawable(mVolumeButton, small);
        scaleVerticalBar(mVolumeLevel);
        scaleVerticalBar(mBrightnessLevel);
    }

    private void scaleVerticalBar(VerticalProgressBar view) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.height = mAnchor.getHeight() / 2;
        view.setLayoutParams(lp);
    }

    private void scaleDrawable(ImageView view, boolean small) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (small) {
            lp.width = view.getDrawable().getMinimumWidth() * 2 / 3;
            lp.height = view.getDrawable().getMinimumHeight() * 2 / 3;
        } else {
            lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        view.setLayoutParams(lp);
    }

    public boolean isShowing() {
        return mShowing;
    }

    /**
     * Remove the controller from the screen.
     */
    public void hide() {
        if (mAnchor == null)
            return;

        if (mShowing) {
            try {
                mHandler.removeMessages(SHOW_PROGRESS);
//                mWindowManager.removeView(mDecor);
            } catch (IllegalArgumentException ex) {
                Log.w("MediaController", "already removed");
            }
            mShowing = false;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int pos;
            switch (msg.what) {
                case FADE_OUT:
                    hide();
                    break;
                case SHOW_PROGRESS:
                    pos = setProgress();
                    if (!mDragging && mShowing && mPlayer.isPlaying()) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                    }
                    break;
            }
        }
    };

    private int setProgress() {
        if (mPlayer == null || mDragging) {
            return 0;
        }
        int position = mPlayer.getCurrentPosition();
        int duration = mPlayer.getDuration();
        if (mProgress != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                mProgress.setProgress((int) pos);
            }
            int percent = mPlayer.getBufferPercentage();
            mProgress.setSecondaryProgress(percent * 10);
        }

        return position;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        } else {
            show();
            return true;
        }
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        show();
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (event.getRepeatCount() == 0 && event.getAction() == KeyEvent.ACTION_DOWN && (
                keyCode == KeyEvent.KEYCODE_HEADSETHOOK ||
                        keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE ||
                        keyCode == KeyEvent.KEYCODE_SPACE)) {
            doPauseResume();
            show();
            if (mPauseButton != null) {
                mPauseButton.requestFocus();
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP) {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                updatePausePlay();
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN ||
                keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            // don't show the controls for volume adjustment
            return super.dispatchKeyEvent(event);
        } else if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
            hide();

            return true;
        } else {
            show();
        }
        return super.dispatchKeyEvent(event);
    }

    private boolean isFullScreen = false;

    private void onFullScreen() {
        mPlayer.setFullscreen(!isFullScreen);
        isFullScreen = !isFullScreen;
    }

    private View.OnClickListener mPauseListener = new View.OnClickListener() {
        public void onClick(View v) {
            doPauseResume();
            show();
        }
    };

    private View.OnClickListener mMuteListener = new View.OnClickListener() {

        public void onClick(View v) {
            if (mMuteSavedVolume == 0) {
                mMuteSavedVolume = (float) mVolumeLevel.getProgress() / 100;
                mVolumeLevel.setProgress(0);
                mPlayer.setVolume(0, 0);
            } else {
                mVolumeLevel.setProgress((int) (mMuteSavedVolume * 100));
                mPlayer.setVolume(mMuteSavedVolume, mMuteSavedVolume);
                mMuteSavedVolume = 0;
            }
            show();
        }
    };

    private View.OnClickListener mBrightnessListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mRightButtons.setDisplayedChild(FLIPPER_CHILD_BRIGHTNESS);
            mBrightnessButton.requestFocusFromTouch();
            show();
        }
    };

    private View.OnClickListener mVolumeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mRightButtons.setDisplayedChild(FLIPPER_CHILD_VOLUME);
            mVolumeButton.requestFocusFromTouch();
            show();
        }
    };

    private void updatePausePlay() {
        if (mRoot == null || mPauseButton == null)
            return;

        if (mPlayer.isPlaying()) {
            mPauseButton.setImageResource(R.drawable.ic_pause);
        } else {
            mPauseButton.setImageResource(R.drawable.ic_play);
        }
    }

    private void initVolumeLevel() {
        mPlayer.setVolume((float) 0.5, (float) 0.5);
    }

    private void doPauseResume() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        } else {
            mPlayer.start();
        }
        updatePausePlay();
    }

    private SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {
        public void onStartTrackingTouch(SeekBar bar) {
            show(3600000);

            mDragging = true;

            // By removing these pending progress messages we make sure
            // that a) we won't update the progress while the user adjusts
            // the seekbar and b) once the user is done dragging the thumb
            // we will post one of these messages to the queue again and
            // this ensures that there will be exactly one message queued up.
            mHandler.removeMessages(SHOW_PROGRESS);
        }

        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
            if (!fromuser) {
                // We're not interested in programmatically generated changes to
                // the progress bar's position.
                return;
            }

            long duration = mPlayer.getDuration();
            long newposition = (duration * progress) / 1000L;
            mPlayer.seekTo((int) newposition);
        }

        public void onStopTrackingTouch(SeekBar bar) {
            mDragging = false;
            setProgress();
            updatePausePlay();
            show();

            // Ensure that progress is properly updated in the future,
            // the call to show() does not guarantee this because it is a
            // no-op if we are already showing.
            mHandler.sendEmptyMessage(SHOW_PROGRESS);
        }
    };

    private VerticalSeekBar.OnSeekBarChangeListener mVolumeLevelListener = new VerticalSeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(VerticalSeekBar seekBar) {
            show();
        }

        @Override
        public void onStartTrackingTouch(VerticalSeekBar seekBar) {
            show(3600000);
        }

        @Override
        public void onProgressChanged(VerticalSeekBar seekBar, int progress,
                                      boolean fromUser) {
            if (!fromUser) {
                return;
            }

            float volume = (float) progress / 100;
            mPlayer.setVolume(volume, volume);
        }
    };

    private VerticalSeekBar.OnSeekBarChangeListener mBrightnessLevelListener = new VerticalSeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(VerticalSeekBar seekBar) {
            show();
        }

        @Override
        public void onStartTrackingTouch(VerticalSeekBar seekBar) {
            show(3600000);
        }

        @Override
        public void onProgressChanged(VerticalSeekBar seekBar, int progress,
                                      boolean fromUser) {
            if (!fromUser) {
                return;
            }

            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.screenBrightness = (float) progress / 100;
            getWindow().setAttributes(lp);
        }
    };

    private GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            onFullScreen();
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            onFullScreen();
        }
    };

    @Override
    public void setEnabled(boolean enabled) {
        if (mPauseButton != null) {
            mPauseButton.setEnabled(enabled);
        }
        if (mProgress != null) {
            mProgress.setEnabled(enabled);
        }
        disableUnsupportedButtons();
        super.setEnabled(enabled);
    }
}
