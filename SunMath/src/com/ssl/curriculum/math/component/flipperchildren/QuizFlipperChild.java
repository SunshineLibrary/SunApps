package com.ssl.curriculum.math.component.flipperchildren;

import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.model.activity.QuizActivityData;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class QuizFlipperChild extends LinearLayout{

	public QuizFlipperChild(Context context, AttributeSet attrs, QuizActivityData quiz) {
		super(context, attrs);
		try{
			View.inflate(context,R.layout.video_flip_layout,this);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}