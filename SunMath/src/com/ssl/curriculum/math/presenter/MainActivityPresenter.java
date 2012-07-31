package com.ssl.curriculum.math.presenter;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.ssl.curriculum.math.activity.MainActivity;
import com.ssl.curriculum.math.activity.NaviActivity;
import com.ssl.curriculum.math.anim.FlipAnimationManager;
import com.ssl.curriculum.math.component.flipperchildren.GalleryThumbnailPageFlipperChild;
import com.ssl.curriculum.math.component.flipperchildren.QuizFillInFlipperChild;
import com.ssl.curriculum.math.component.flipperchildren.QuizFlipperChild;
import com.ssl.curriculum.math.component.flipperchildren.VideoFlipperChild;
import com.ssl.curriculum.math.listener.PageFlipListener;
import com.ssl.curriculum.math.model.activity.ActivityData;
import com.ssl.curriculum.math.model.activity.QuizActivityData;
import com.ssl.curriculum.math.model.activity.VideoActivityData;
import com.ssl.curriculum.math.service.ActivityContentProvider;
import com.ssl.curriculum.math.service.EdgeContentProvider;
import com.sunshine.metadata.provider.MetadataContract.Activities;

import java.util.HashMap;

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
	private static final int TYPE_GALLERY = Activities.TYPE_GALLERY;

	private EdgeContentProvider edgeProvider;
	private ActivityContentProvider activityProvider;
	private PageFlipListener flipListener;
	private HashMap<String,View> UIBindings = new HashMap<String,View>();
	private final MainActivity activity;
	
	public MainActivityPresenter(MainActivity activity){
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
		final ViewFlipper viewFlipper = (ViewFlipper) this.UIBindings.get(FLIPPER);
		if(viewFlipper == null)
			return;
		
		View activityView = null;
		
		switch(activity.getType()){
			case TYPE_VIDEO:{
				activityView = new VideoFlipperChild(this.activity, null, (VideoActivityData) activity);
			}break;
			case TYPE_TEXT:{
				activityView = new TextView(this.activity);
				((TextView) activityView).setText("TextTypedObject - Damn");
			}break;
			case TYPE_AUDIO:{
				activityView = new TextView(this.activity);
				((TextView) activityView).setText("Audio - Wahoo!");
			}break;
			case TYPE_HTML:{
				activityView = new TextView(this.activity);
				((TextView) activityView).setText("HTML");
			}break;
			case TYPE_QUIZ:{
				activityView = new QuizFlipperChild(this.activity, null, (QuizActivityData) activity);
            }break;
            case TYPE_GALLERY: {
                GalleryThumbnailPageFlipperChild galleryThumbnailPage = new GalleryThumbnailPageFlipperChild(this.activity);
                galleryThumbnailPage.setGalleryItemClickedListener(this.activity.getGalleryThumbnailItemClickListener());
                activityView = galleryThumbnailPage;
            }break;
			default:{
				activityView = new TextView(this.activity);
				((TextView) activityView).setText("You should never see this. Probably.");
			}
		}
		if(viewFlipper != null){
			viewFlipper.addView(activityView);
			
			if(mode == 1){
				viewFlipper.showNext();	
			}else if(mode == -1){
				viewFlipper.showPrevious();
			}
			
			while(viewFlipper.getChildCount() > 1){
				viewFlipper.removeViewAt(0);
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
	            }
	        });
	        
	        this.UIBindings.get(BTN_RIGHT).setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	                viewFlipper.setInAnimation(flipAnimationManager.getFlipInFromLeftAnimation());
	                viewFlipper.setOutAnimation(flipAnimationManager.getFlipOutToRightAnimation());
	                self.flipListener.onShowNext();
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
