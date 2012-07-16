package com.example.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ViewFlipper;
//import android.widget.SlidingDrawer;
//import android.widget.TextView;

public class MainActivity extends Activity{

    private ViewFlipper viewFlipper = null;
    private ImageButton leftbtn;
    private ImageButton rightbtn;
    private Button  navibtn;
    
    //private SlidingDrawer mDrawer;
    //private Boolean flag=false;
    //private TextView tv;
     
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewFlipper = (ViewFlipper) this.findViewById(R.id.myViewFlipper);
        viewFlipper.setInAnimation(MainActivity.this, android.R.anim.fade_in);
        viewFlipper.setOutAnimation(MainActivity.this, android.R.anim.fade_out);
        
        this.leftbtn = (ImageButton) this.findViewById(R.id.leftbtn);
        this.rightbtn = (ImageButton) this.findViewById(R.id.rightbtn); 
        this.navibtn = (Button) this.findViewById(R.id.navibtn);
		this.leftbtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				viewFlipper.showPrevious();
			}
		});
		this.rightbtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				viewFlipper.showNext();
			}
		});
    /******  保留侧拉抽屉效果
		mDrawer=(SlidingDrawer)findViewById(R.id.slidingdrawer);
		tv=(TextView)findViewById(R.id.tv);

		mDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener()
		{
		public void onDrawerOpened() {
		flag=true;
		}

		});

		mDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener(){
		public void onDrawerClosed() {
		flag=false;
		}

		});

		mDrawer.setOnDrawerScrollListener(new SlidingDrawer.OnDrawerScrollListener(){
		public void onScrollEnded() {
		tv.setText("结束拖动");
		}
		public void onScrollStarted() {
		tv.setText("开始拖动");
		}

		});
		******/
    
    }
    
    
    
}