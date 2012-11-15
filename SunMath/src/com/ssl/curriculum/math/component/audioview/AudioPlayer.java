package com.ssl.curriculum.math.component.audioview;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.component.videoview.TappableSurfaceView;
import com.ssl.metadata.provider.MetadataContract;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.ParcelFileDescriptor;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author Cong Liu
 * version 1.0
 */

public class AudioPlayer extends RelativeLayout implements OnCompletionListener,OnClickListener{
	private Context context;
	private ViewGroup content;
	
	private ImageView iv_faceView;
	private TextView tv_title;
	private SeekBar seekBar;
	private Button play_pause;
	private Button rollback;
	private Button restart;
	
	private int activityId;
	private MediaPlayer player = null;
	
	private boolean isPlaying = false;
	private boolean isPause = false;
	private boolean isFirst = true;
	private boolean isChanging = false;
	
	private Timer mTimer = null;
	private TimerTask mTimerTask = null;
	
	public AudioPlayer(Context context, AttributeSet attrs) {
		super(context, attrs);
        this.context = context;
        initUI();
        initListener();
	}

	private void initUI() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        content = (ViewGroup) inflater.inflate(R.layout.audio_one, null);
        this.addView(content);

        iv_faceView = (ImageView) findViewById(R.id.iv_faceView);
        tv_title = (TextView) findViewById(R.id.tv_title);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        play_pause = (Button) findViewById(R.id.play_pause);
        rollback = (Button) findViewById(R.id.rollback);
        restart = (Button) findViewById(R.id.restart);
    }
	
	private void initListener()
	{
		play_pause.setOnClickListener(this);
		rollback.setOnClickListener(this);
		restart.setOnClickListener(this);
		
		seekBar.setOnSeekBarChangeListener(new MyListener());
	}
	
	public void setAudioId(int id) {
        activityId = id;
    }
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.play_pause:
			if(isPause){
				player.start();
				isPlaying = true;
				isPause = false;
				play_pause.setText("暂停");
				return;
			}else if(player != null && isPlaying){
				player.pause();
				isPause = true;
				isPlaying = false;
				play_pause.setText("播放");
				return;
			}
			play(getAudioFileDescriptor(activityId));
			break;
		case R.id.rollback:
			if(player != null){
				int position = player.getCurrentPosition();
				if(position > 10000) playAudioFromPosition(position - 10000);
				else playAudioFromPosition(0);
			}
			break;
		case R.id.restart:
			if(player!=null){
				/*mTimer.cancel();
				mTimer = null;
				isFirst = true;
				
				player.stop();
				player.release();
				player = null;*/
				
				playAudioFromPosition(0);
			}
			//play(getAudioFileDescriptor(activityId));
			break;
		}
	}

	public FileDescriptor getAudioFileDescriptor(int activityId) {
        ParcelFileDescriptor pfdInput = null;
        try {
            pfdInput = getContext().getContentResolver().openFileDescriptor(MetadataContract.Activities.getActivityAudioUri(activityId), "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return pfdInput.getFileDescriptor();
    }
	
	public void play(FileDescriptor fileDescriptor){
		 try {
			 	player = new MediaPlayer();
	            player.setDataSource(getAudioFileDescriptor(activityId));
	            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
	            player.prepare();
	            player.setOnCompletionListener(this);
	            player.start();
	            isPlaying = true;
	            isPause = false;
	           play_pause.setText("暂停");
	            
	            if(isFirst){//
	            	seekBar.setMax(player.getDuration());
                   
                   mTimer = new Timer();    
                   mTimerTask = new TimerTask() {    
                       @Override    
                       public void run() {     
                           if(isChanging==true) {   
                               return;    
                           }  
                           seekBar.setProgress(player.getCurrentPosition());  
                       }    
                   };   
                   mTimer.schedule(mTimerTask, 0, 10);
                   isFirst=false;
	            }
	        } catch (Exception t) {
	            t.printStackTrace();
	            Toast.makeText(getContext(), "播放音频失败", 0).show();
	        }
	}
	
	private void playAudioFromPosition(int position){
    	try {
            player.reset();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(getAudioFileDescriptor(activityId));
            player.prepare();
            player.seekTo(position);
            player.start();
            isPlaying = true;
            isPause = false;
            play_pause.setText("暂停");
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "播放音频失败", 0).show();
        }
    }
	
	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		isPlaying = false;
		play_pause.setText("播放");
		seekBar.setProgress(0);//-----播放完毕后要把Bar置为0
		//isFirst=true;
	}
	
	public void pause() {
        if (player == null) return;
        player.pause();
        //isPaused = true;
    }
	
	public void onDestroy() {
        if (player != null) {
			mTimer.cancel();
			mTimer = null;
			
        	player.stop();
            player.release();
            player = null;
        }
    }
	
	private class MyListener implements OnSeekBarChangeListener{

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			isChanging = true;
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			isChanging = false;
			
			int currentPosition = seekBar.getProgress();
			playAudioFromPosition(currentPosition);
		}
	}
	
	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
	if(keyCode == KeyEvent.KEYCODE_BACK){
		player.stop();
		player.release();
		player = null;
	}
		return false;
	}*/
}