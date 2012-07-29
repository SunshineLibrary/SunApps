package com.sunshine.sunresourcecenter;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class ResourceInfoActivity extends Activity {
	ImageButton backButton;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_info);
        
        backButton = (ImageButton)findViewById(R.id.info_back_button);
        backButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub   
                
				v.setBackgroundResource(R.drawable.back02);       
				
				ResourceInfoActivity.this.finish();
			}
        	
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_resource_info, menu);
        return true;
    }

    
}
