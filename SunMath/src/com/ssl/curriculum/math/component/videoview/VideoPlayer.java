package com.ssl.curriculum.math.component.videoview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.ssl.curriculum.math.R;

public class VideoPlayer extends RelativeLayout implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, SurfaceHolder.Callback {

    private static final String TAG = "VideoPlayer";
    private long lastActionTime = 0L;
    private View controlPanel;
    private ProgressBar timeline;
    private ImageButton playButton;
    private TappableSurfaceView surface;
    private SurfaceHolder holder;
    private MediaPlayer player;
    private int width;

    private int height;
    private Context context;
    private Uri uri;
    private boolean isPaused;
    private ViewGroup content;
    private ViewGroup mFullScreenLayout;
    private View savedContentView;
    private ImageButton fullScreenButton;
    private boolean isFullScreen = false;

    public VideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        Thread.setDefaultUncaughtExceptionHandler(onBlooey);

        initUI();
        initListener();
    }

    private void initUI() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        content = (ViewGroup) inflater.inflate(R.layout.video_player, null);

//        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFullScreenLayout = (ViewGroup) inflater.inflate(R.layout.video_player_full_screen, null);

        this.addView(content);

        surface = (TappableSurfaceView) findViewById(R.id.video_player_surface);
        surface.addTapListener(onTap);
        holder = surface.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        controlPanel = findViewById(R.id.video_player_control_panel);
        timeline = (ProgressBar) findViewById(R.id.video_player_time_line);

        playButton = (ImageButton) findViewById(R.id.video_player_media);
        fullScreenButton = (ImageButton) findViewById(R.id.video_player_full_screen);
    }

    private void initListener() {
        playButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                lastActionTime = SystemClock.elapsedRealtime();

                if (player == null) {
                    startVideo();
                    onStart();
                    return;
                }

                if (player.isPlaying())
                    pause();
                else
                    start();
            }

            private void pause() {
                playButton.setImageResource(R.drawable.ic_media_play);
                player.pause();
            }

            private void start() {
                playButton.setImageResource(R.drawable.ic_media_pause);
                player.start();
            }
        });

        fullScreenButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isFullScreen = !isFullScreen;
                setFullscreen(isFullScreen);
            }
        });
    }

    public void setVideoURI(Uri uri) {
        this.uri = uri;
    }

    public void startVideo() {
        if (uri == null) return;

        playVideo(uri);
        clearPanels();
    }

    public void onStart() {
        isPaused = false;
        surface.postDelayed(onEverySecond, 1000);
    }

    private Runnable onEverySecond = new Runnable() {
        public void run() {
            if (lastActionTime > 0 &&
                    SystemClock.elapsedRealtime() - lastActionTime > 3000) {
                clearPanels();
            }

            if (player != null) {
                timeline.setProgress(player.getCurrentPosition());
            }

            if (!isPaused) {
                surface.postDelayed(onEverySecond, 1000);
            }
        }
    };

    protected void onPause() {
        isPaused = true;
    }

    protected void onDestroy() {
        if (player != null) {
            player.release();
            player = null;
        }

        surface.removeTapListener(onTap);
    }


    private void playVideo(Uri uri) {
        try {
            playButton.setEnabled(false);

            if (player == null) {
                player = new MediaPlayer();
                player.setScreenOnWhilePlaying(true);
            } else {
                player.stop();
                player.reset();
            }

            player.setDataSource(this.context, uri);
            player.setDisplay(holder);
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setOnPreparedListener(this);
            player.prepareAsync();
//			player.setOnBufferingUpdateListener(this);
            player.setOnCompletionListener(this);
        } catch (Throwable t) {
            Log.e(TAG, "Exception in media prep", t);
            goBlooey(t);
        }
    }

    public void setFullscreen(boolean fullscreen) {
        boolean isOnPause = false;
        Activity activity = (Activity) getContext();

        if (fullscreen) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            if (player!=null && player.isPlaying()) {
                player.pause();
                isOnPause = true;
            }

            savedContentView = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            ViewGroup container = (ViewGroup) getParent();
            container.removeView(this);
            activity.setContentView(mFullScreenLayout);

            container = (FrameLayout) activity.findViewById(R.id.video_player_full_screen_container);
            container.addView(this);

            if(isOnPause) player.start();
        } else {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            if (player!=null && player.isPlaying()) {
                player.pause();
                isOnPause = true;
            }

            ViewGroup container = (ViewGroup) getParent();
            container.removeView(this);
            activity.setContentView(savedContentView);
            container = (RelativeLayout) activity.findViewById(R.id.content_screen_video_frame);
            container.addView(this);

            if(isOnPause) player.start();
        }
    }

    private void clearPanels() {
        lastActionTime = 0;
        controlPanel.setVisibility(View.GONE);
    }

    private TappableSurfaceView.TapListener onTap =
            new TappableSurfaceView.TapListener() {
                public void onTap(MotionEvent event) {
                    lastActionTime = SystemClock.elapsedRealtime();

                    if (controlPanel.getVisibility() == View.GONE)
                        controlPanel.setVisibility(View.VISIBLE);
                    else
                        controlPanel.setVisibility(View.GONE);
                }
            };

    @Override
    public void onCompletion(MediaPlayer arg0) {
        playButton.setEnabled(false);
    }

    @Override
    public void onPrepared(MediaPlayer mediaplayer) {
        width = player.getVideoWidth();
        height = player.getVideoHeight();

        if (width != 0 && height != 0) {
            holder.setFixedSize(width, height);
            timeline.setProgress(0);
            timeline.setMax(player.getDuration());
            player.start();
        }

        playButton.setEnabled(true);
    }

    private Thread.UncaughtExceptionHandler onBlooey =
            new Thread.UncaughtExceptionHandler() {
                public void uncaughtException(Thread thread, Throwable ex) {
                    Log.e(TAG, "Uncaught exception", ex);
                    goBlooey(ex);
                }
            };

    private void goBlooey(Throwable t) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Exception!")
                .setMessage(t.toString())
                .setPositiveButton("OK", null)
                .show();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //not implemented
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //not implemented
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //not implemented
    }

}
