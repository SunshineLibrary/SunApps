package com.ssl.curriculum.math.component.flipperchildren;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.component.videoview.VideoPlayer;
import com.ssl.curriculum.math.model.activity.VideoActivityData;

public class VideoFlipperChild extends LinearLayout {
	private VideoPlayer videoPlayer;
	private Context context;
	private VideoActivityData instData;
	
	public VideoFlipperChild(Context context, AttributeSet attrs, VideoActivityData vad) { 
		super(context, attrs);
		this.context = context;
		this.instData = vad;
		try{
			View.inflate(context,R.layout.video_flip_layout,this);
		}catch(Exception e){
			e.printStackTrace();
		}
		initVideoComponent();
	}
	
	private void initVideoComponent(){
		videoPlayer = (VideoPlayer) findViewById(R.id.content_screen_video_field);
        Uri video = Uri.parse("android.resource://" + this.context.getPackageName() + "/" + R.raw.speaking);
        videoPlayer.setVideoURI(video);
        TextView title = (TextView) this.findViewById(R.id.video_title);
        title.setText(instData.getTitle());
        TextView descr = (TextView) this.findViewById(R.id.video_descr);
        descr.setText(instData.getDescription());
	}
	
}
