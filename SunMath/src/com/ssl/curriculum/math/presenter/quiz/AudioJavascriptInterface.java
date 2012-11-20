package com.ssl.curriculum.math.presenter.quiz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class AudioJavascriptInterface {
	private MediaPlayer player;
	private boolean isPlaying = false;
	private boolean isPause = false;
	
	public void play() {
    	try {
    		System.out.println("进入播放！");
			
    		if(player != null && isPause){
    			player.start();
    			isPlaying = true;
    			isPause = false;
    			return;
    		}
    		
			player = new MediaPlayer();
			player.setAudioStreamType(AudioManager.STREAM_MUSIC);
			//player.setDataSource("/mnt/sdcard/test.mp3");
			File file = new File("/mnt/sdcard/test.mp3");
			FileInputStream fis = new FileInputStream(file);
			player.setDataSource(fis.getFD());
			
			player.prepare();
			player.setOnCompletionListener(new OnCompletionListener() {
				
				public void onCompletion(MediaPlayer mp) {
					isPlaying = false;
				}
			});
			
			player.start();
			isPlaying = true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    } 
	
	public void pause(){
		if(player != null && isPlaying){
			player.pause();
			isPlaying = false;
			isPause = true;
		}
	}
	
	public void onDestroy()
	{
		if(player != null){
			player.stop();
			player.release();
			player = null;
		}
	}
}
