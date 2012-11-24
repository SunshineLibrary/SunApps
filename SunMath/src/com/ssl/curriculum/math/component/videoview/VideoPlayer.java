package com.ssl.curriculum.math.component.videoview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.ParcelFileDescriptor;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.listener.TapListener;
import com.ssl.metadata.provider.MetadataContract;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

public class VideoPlayer extends RelativeLayout implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, SurfaceHolder.Callback {
    private static final String TAG = "VideoPlayer";

    private Context context;
    private View controlPanel;
    private ProgressBar playerProgress;
    private TappableSurfaceView surface;
    private MediaPlayer player;
    private SurfaceHolder holder;
    private ImageButton playButton;
    private ImageButton rollbackButton;
    private ViewGroup content;
    private ViewGroup mFullScreenLayout;
    private View savedContentView;
    private ImageButton fullScreenButton;

    private TapListener tapListener;

    private int width;
    private int height;
    private long lastActionTime = 0L;

    private boolean isPaused;
    private boolean toFullScreen = false;

    private Runnable progressRunnable;
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
    private int savedPlayedPosition;
    private int activityId;

    public VideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        uncaughtException();
        initUI();
        initListener();
        initRunnable();
    }

    private void uncaughtException() {
        uncaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread thread, Throwable ex) {
                Log.e(TAG, "Uncaught exception", ex);
                showErrorDialog(ex);
            }
        };
        Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
    }

    private void initUI() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        content = (ViewGroup) inflater.inflate(R.layout.video_player, null);
        this.addView(content);

        surface = (TappableSurfaceView) findViewById(R.id.video_player_surface);
        controlPanel = findViewById(R.id.video_player_control_panel);
        playerProgress = (ProgressBar) findViewById(R.id.video_player_time_line);
        playButton = (ImageButton) findViewById(R.id.video_player_media_btn);
        rollbackButton = (ImageButton) findViewById(R.id.video_player_media_rollback);
        
        holder = surface.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        fullScreenButton = (ImageButton) findViewById(R.id.video_player_full_screen_btn);

        mFullScreenLayout = (ViewGroup) inflater.inflate(R.layout.video_player_full_screen, null);
    }

    private void initListener() {
        tapListener = new TapListener() {
            public void onTap(MotionEvent event) {
                lastActionTime = SystemClock.elapsedRealtime();
                if (controlPanel.getVisibility() == View.GONE)
                    controlPanel.setVisibility(View.VISIBLE);
//                else
//                    controlPanel.setVisibility(View.GONE);
                //hereLiu:typup lead to play or pause
                if(player!=null){
                	if(player.isPlaying()){
                		//play to pause
                		playButton.setImageResource(R.drawable.ic_media_play);
                        player.pause();
                        isPaused = true;
                	}else if(isPaused){
                		//pause to play
                		 player.start();
                	     onStart();
                	}
                }
            }
        };
        surface.addTapListener(tapListener);
        holder.addCallback(this);

        playButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                handlePlayBtnClick();
            }
        });
        
        rollbackButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
                handleRollBackBtnClick();
            }
        });
        
        fullScreenButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFullScreenBtnClick();
            }
        });
    }

    private void initRunnable() {
        progressRunnable = new Runnable() {
            public void run() {
                if (lastActionTime > 0 && SystemClock.elapsedRealtime() - lastActionTime > 3000) {
                    hideControlPanel();
                }      

                if (player != null) {
                    playerProgress.setProgress(player.getCurrentPosition());
                }

                if (!isPaused) {
                    surface.postDelayed(progressRunnable, 1000);
                }
            }
        };
    }

    private void handleFullScreenBtnClick() {
        toFullScreen = !toFullScreen;
        setToFullScreen(toFullScreen);
    }

    private void handlePlayBtnClick() {
        lastActionTime = SystemClock.elapsedRealtime();
        if (player == null) play();
        else if (player.isPlaying()) pause();
        else resume();
    }
    
    private void handleRollBackBtnClick() {
    	if (player == null || !player.isPlaying()) return;
    	int position = player.getCurrentPosition();
    	if(position > 10000) playVedioFromPosition(position - 10000);
    	else playVedioFromPosition(0);
    }
    
    private void play() {
        playVideo();
        hideControlPanel();
        onStart();
        //toFullScreen = true;
        //setToFullScreen(toFullScreen);
    }

    public void pause() {
        if (player == null) return;
        playButton.setImageResource(R.drawable.ic_media_play);
        player.pause();
        isPaused = true;
    }

    private void resume() {
        player.start();
        onStart();
    }

    public void onStart() {
        playButton.setImageResource(R.drawable.ic_media_pause);
        surface.postDelayed(progressRunnable, 1000);
        isPaused = false;
    }

    public void onDestroy() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void playVideo() {
        try {
            player = new MediaPlayer();
            player.setScreenOnWhilePlaying(true);
            player.setDataSource(getVideoFileDescriptor(activityId));
            player.setDisplay(holder);
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.prepareAsync();
            player.setOnPreparedListener(this);
            player.setOnCompletionListener(this);
            
        } catch (Exception t) {
            t.printStackTrace();
            showErrorDialog(t);
        }
    }

    public void setToFullScreen(boolean toFullScreen) {
        if (player == null) return;
        savedPlayedPosition = player.getCurrentPosition();
        player.pause();
        if (toFullScreen) toFullScreen();
        else toOriginalScreen();
    }

    private void toOriginalScreen() {
        Activity activity = (Activity) getContext();
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ViewGroup container = (ViewGroup) getParent();
        container.removeView(this);
        activity.setContentView(savedContentView);
        container = (RelativeLayout) activity.findViewById(R.id.content_screen_video_frame);
        container.addView(this);
    }

    private void toFullScreen() {
        Activity activity = (Activity) getContext();
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        savedContentView = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        ViewGroup container = (ViewGroup) getParent();
        container.removeView(this);
        activity.setContentView(mFullScreenLayout);
        container = (FrameLayout) activity.findViewById(R.id.video_player_full_screen_container);
        container.addView(this);
    }

    private void hideControlPanel() {
        lastActionTime = 0;
        controlPanel.setVisibility(View.GONE);
    }

    @Override
    public void onCompletion(MediaPlayer arg0) {
        playButton.setEnabled(false);
    }

    @Override
    public void onPrepared(MediaPlayer mediaplayer) {
        width = player.getVideoWidth();
        height = player.getVideoHeight();
        if (width == 0 || height == 0) return;

        holder.setFixedSize(width, height);
        playerProgress.setProgress(0);
        playerProgress.setMax(player.getDuration());
        player.start();//should be call player to paly
        //Log.i("mediaplayer", "player prepared");
        playButton.setEnabled(true);
        rollbackButton.setEnabled(true);
        System.out.println("begin play!!!");
    }

    private void showErrorDialog(Throwable t) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Exception!").setMessage(t.toString()).setPositiveButton("OK", null).show();
    }
    
    private void playVedioFromPosition(int position){
    	try {
            player.reset();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(getVideoFileDescriptor(activityId));
            player.setDisplay(holder);
            player.prepare();
            player.seekTo(position);
            player.start();
        } catch (IOException e) {
            Log.e(TAG, "when switch to different screen, recreate the media player error!");
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (player == null || player.isPlaying()) return;
        Log.i("mediaplayer", "surfaceCreated");
        playVedioFromPosition(savedPlayedPosition);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public FileDescriptor getVideoFileDescriptor(int activityId) {
        ParcelFileDescriptor pfdInput = null;
        try {
            pfdInput = getContext().getContentResolver().openFileDescriptor(MetadataContract.Activities.getActivityVideoUri(activityId), "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return pfdInput.getFileDescriptor();
    }

    public void setVideoId(int id) {
        activityId = id;
    }


    public void reset() {
        if (player == null) return;
        try {
            player.stop();
            player.reset();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(getVideoFileDescriptor(activityId));
            player.setDisplay(holder);
            player.prepareAsync();
        } catch (IOException e) {
            Log.e(TAG, "when switch to different screen, recreate the media player error!");
        }
    }
}
