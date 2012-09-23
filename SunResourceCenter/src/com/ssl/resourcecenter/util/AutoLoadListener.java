package com.ssl.resourcecenter.util;

import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;


public class AutoLoadListener implements OnScrollListener{

	public interface AutoLoadCallBack {
		void execute();
	}

//	private int getLastVisiblePosition = 0,lastVisiblePositionY=0;
	private AutoLoadCallBack  mCallback;
	public AutoLoadListener(AutoLoadCallBack callback)
	{
		this.mCallback = callback;
	}
	
	public void onScrollStateChanged(AbsListView view, int scrollState) {

		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			//roll to bottom 
			if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
				
				mCallback.execute();
				
//				View v=(View) view.getChildAt(view.getChildCount()-1);
//				int[] location = new  int[2] ;
//				v.getLocationOnScreen(location);//get entire abs location
//		        int y=location [1];
//
//		        Log.e("x"+location[0],"y"+location[1]);
//				if (view.getLastVisiblePosition()!=getLastVisiblePosition
//						&& lastVisiblePositionY!=y)//rolled to bottom
//				{
//					
//					getLastVisiblePosition=view.getLastVisiblePosition();
//					lastVisiblePositionY=y;
//					
//					//return;
//				}
//				else if (view.getLastVisiblePosition()==getLastVisiblePosition
//						&& lastVisiblePositionY==y)//second time rolled to bottom
//				{
//					mCallback.execute(String.valueOf(getLastVisiblePosition));
//				}
			}
			
			//reinit
//			getLastVisiblePosition=0;	
//			lastVisiblePositionY=0;
		}
	}

	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		
	}
}