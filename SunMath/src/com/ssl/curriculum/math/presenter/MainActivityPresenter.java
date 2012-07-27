package com.ssl.curriculum.math.presenter;

import java.util.HashMap;

import com.ssl.curriculum.math.activity.GalleryFlipperActivity;
import com.ssl.curriculum.math.activity.MainActivity;
import com.ssl.curriculum.math.activity.NaviActivity;
import com.ssl.curriculum.math.anim.FlipAnimationManager;
import com.ssl.curriculum.math.listener.GalleryItemClickedListener;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ViewFlipper;

public class MainActivityPresenter {
	public static String BTN_LEFT = "left_btn";
	public static String BTN_RIGHT = "right_btn";
	public static String BTN_NAVI = "nav_btn";
	public static String FLIPPER = "view_flipper";
	
	private HashMap<String,View> UIBindings = new HashMap<String,View>();
	private final Activity activity;
	
	public MainActivityPresenter(Activity activity){
		this.activity = activity;
	}
	
	private void presentActivityById(int id){
		//TODO: Update flip according to id
	}
	
	public void bindUIElement(String bindKey, View UIElement){
		UIBindings.put(bindKey, UIElement);
	}
	
	
	public void initListeners(){
		final FlipAnimationManager flipAnimationManager = FlipAnimationManager.getInstance(this.activity);
		final ViewFlipper viewFlipper = (ViewFlipper) this.UIBindings.get(FLIPPER);

		final MainActivityPresenter self = this;
        try{
			this.UIBindings.get(BTN_LEFT).setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	                viewFlipper.setInAnimation(flipAnimationManager.getFlipInFromRightAnimation());
	                viewFlipper.setOutAnimation(flipAnimationManager.getFlipOutToLeftAnimation());
	                viewFlipper.showPrevious();
	            }
	        });
	        
	        this.UIBindings.get(BTN_RIGHT).setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	                viewFlipper.setInAnimation(flipAnimationManager.getFlipInFromLeftAnimation());
	                viewFlipper.setOutAnimation(flipAnimationManager.getFlipOutToRightAnimation());
	                viewFlipper.showNext();
	            }
	        });
	        
	        this.UIBindings.get(BTN_NAVI).setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	                Intent intent = new Intent(self.activity, NaviActivity.class);
	                self.activity.startActivity(intent);
	            }
	        });
        }catch(NullPointerException e){
        	System.out.println("Exception: Presenter not properly initialized.");
        }
	}
}
