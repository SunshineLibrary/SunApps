package com.sunshine.sunresourcecenter;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ResourceInfoActivity extends Activity {
	ImageButton backButton;
	ImageView cover;
	TextView originname, author, translator, publisher, publish_year, title, author_intro, intro;
	Button readButton, downButton;
	ContentResolver resolver;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_info);
        
        readButton = (Button) findViewById(R.id.info_button_read);
        downButton = (Button) findViewById(R.id.info_button_down);
        backButton = (ImageButton)findViewById(R.id.info_back_button);
        cover = (ImageView) findViewById(R.id.info_cover);
        originname = (TextView) findViewById(R.id.info_originname);
        author = (TextView) findViewById(R.id.info_author);
        translator = (TextView) findViewById(R.id.info_translator);
        publisher = (TextView) findViewById(R.id.info_publisher);
        publish_year = (TextView) findViewById(R.id.info_publish_year);
        title = (TextView) findViewById(R.id.info_restitle);
        author_intro = (TextView) findViewById(R.id.info_author_intro);
        intro = (TextView) findViewById(R.id.info_intro);
        resolver = this.getContentResolver();
        
        Intent intent = this.getIntent();
        intent.getStringExtra("bookId");
                
        //downButton
        
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
    
    private void setButtonEnable(Button button, boolean enable){
    	
    	button.setEnabled(enable);
    	if(enable){
    		
    		button.setBackgroundColor(getResources().getColor(R.color.chrome));
    		
    	}else {

    		button.setBackgroundColor(getResources().getColor(R.color.info_grey));
    		
    	}
    }
    
}
