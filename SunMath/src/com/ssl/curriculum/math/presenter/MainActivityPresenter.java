package com.ssl.curriculum.math.presenter;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ViewFlipper;

import com.ssl.curriculum.math.activity.NaviActivity;
import com.ssl.curriculum.math.anim.FlipAnimationManager;
import com.ssl.curriculum.math.model.activity.ActivityData;
import com.ssl.curriculum.math.service.EdgeContentProvider;
import com.sunshine.metadata.provider.MetadataContract.Activities;

public class MainActivityPresenter {
	public static String BTN_LEFT = "left_btn";
	public static String BTN_RIGHT = "right_btn";
	public static String BTN_NAVI = "nav_btn";
	public static String FLIPPER = "view_flipper";
	
	private static final int TYPE_TEXT = Activities.Types.TEXT.ordinal();
	private static final int TYPE_VIDEO = Activities.Types.VIDEO.ordinal();
	private static final int TYPE_AUDIO = Activities.Types.AUDIO.ordinal();
	private static final int TYPE_HTML = Activities.Types.HTML.ordinal();
	private static final int TYPE_QUIZ = Activities.Types.QUIZ.ordinal();
	
	private EdgeContentProvider edgeProvider;
	private HashMap<String,View> UIBindings = new HashMap<String,View>();
	private final Activity activity;
	
	public MainActivityPresenter(Activity activity){
		this.activity = activity;
		this.edgeProvider = new EdgeContentProvider(this.activity);
	}
	
	public void bindUIElement(String bindKey, View UIElement){
		UIBindings.put(bindKey, UIElement);
	}
	
	public EdgeContentProvider getEdgeProvider(){
		return this.edgeProvider;
	}
	
	public void present(ActivityData activity){
		/** I am not using a CASE expression here because it requires constants **/
		if(activity.getType() == TYPE_VIDEO){
			
		}else{
			
		}
	}
	
	public ActivityData getActivityById(int id){
		return new ActivityData(0);
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
