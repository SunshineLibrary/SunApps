package com.ssl.curriculum.math.presenter;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.ssl.curriculum.math.activity.NaviActivity;
import com.ssl.curriculum.math.anim.FlipAnimationManager;
import com.ssl.curriculum.math.listener.PageFlipListener;
import com.ssl.curriculum.math.model.activity.ActivityData;
import com.ssl.curriculum.math.service.ActivityContentProvider;
import com.ssl.curriculum.math.service.EdgeContentProvider;
import com.sunshine.metadata.provider.MetadataContract.Activities;

public class MainActivityPresenter {
	public static String BTN_LEFT = "left_btn";
	public static String BTN_RIGHT = "right_btn";
	public static String BTN_NAVI = "nav_btn";
	public static String FLIPPER = "view_flipper";
	
	private static final int TYPE_TEXT = Activities.TYPE_TEXT;
	private static final int TYPE_VIDEO = Activities.TYPE_VIDEO;
	private static final int TYPE_AUDIO = Activities.TYPE_AUDIO;
	private static final int TYPE_HTML = Activities.TYPE_HTML;
	private static final int TYPE_QUIZ = Activities.TYPE_QUIZ;
	
	private EdgeContentProvider edgeProvider;
	private ActivityContentProvider activityProvider;
	private PageFlipListener flipListener;
	private HashMap<String,View> UIBindings = new HashMap<String,View>();
	private final Activity activity;
	
	public MainActivityPresenter(Activity activity){
		this.activity = activity;
		this.edgeProvider = new EdgeContentProvider(this.activity);
		this.activityProvider = new ActivityContentProvider(this.activity);
	}
	
	public void bindUIElement(String bindKey, View UIElement){
		UIBindings.put(bindKey, UIElement);
	}
	
	public void setPageFlipListener(PageFlipListener pfl){
		this.flipListener = pfl;
	}
	
	public EdgeContentProvider getEdgeProvider(){
		return this.edgeProvider;
	}
	
	public ActivityContentProvider getActvityProvider(){
		return this.activityProvider;
	}
	
	public void present(ActivityData activity,int mode){
		/** I am not using a CASE expression here because it requires constants **/
		final ViewFlipper viewFlipper = (ViewFlipper) this.UIBindings.get(FLIPPER);
		TextView tv = new TextView(this.activity);
		if(activity.getType() == TYPE_VIDEO){
			tv.setText("VideoTypedObject");
		}else if(activity.getType() == TYPE_TEXT){
			tv.setText("TextTypedObject");
		}else{
			tv.setText("OtherTypedObject - We know this works");
		}
		if(viewFlipper != null){
			System.out.println("ViewLength : " + viewFlipper.getChildCount());
			if(mode == 1){
				viewFlipper.addView(tv);
				viewFlipper.showNext();
				while(viewFlipper.getChildCount() > 1){
					viewFlipper.removeViewAt(0);
				}
			}else if(mode == -1){
				viewFlipper.addView(tv, 0);
				viewFlipper.showPrevious();
			}
		}
	}
	
	public void initListeners(){
		final FlipAnimationManager flipAnimationManager = FlipAnimationManager.getInstance(this.activity);
		final MainActivityPresenter self = this;
		final ViewFlipper viewFlipper = (ViewFlipper) this.UIBindings.get(FLIPPER);
        try{
			this.UIBindings.get(BTN_LEFT).setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	                viewFlipper.setInAnimation(flipAnimationManager.getFlipInFromRightAnimation());
	                viewFlipper.setOutAnimation(flipAnimationManager.getFlipOutToLeftAnimation());
	                self.flipListener.onShowPrevious();
	                //viewFlipper.showPrevious();
	            }
	        });
	        
	        this.UIBindings.get(BTN_RIGHT).setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	                viewFlipper.setInAnimation(flipAnimationManager.getFlipInFromLeftAnimation());
	                viewFlipper.setOutAnimation(flipAnimationManager.getFlipOutToRightAnimation());
	                self.flipListener.onShowNext();
	                //viewFlipper.showNext();
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
