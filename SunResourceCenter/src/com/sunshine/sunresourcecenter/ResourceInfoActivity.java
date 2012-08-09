package com.sunshine.sunresourcecenter;

import com.sunshine.metadata.provider.MetadataContract.Books;
import com.sunshine.sunresourcecenter.R;
import com.sunshine.sunresourcecenter.griditem.ResourceGridItem;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ResourceInfoActivity extends Activity {
	ImageButton backButton;
	ImageView cover;
	TextView originname, author, translator, publisher, publish_year, title, author_intro, intro;
	Button readButton, downButton;
	ContentResolver resolver;
	LinearLayout resLayoutAll, resLayoutLeft, resLayoutRight;
	
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
        //translator = (TextView) findViewById(R.id.info_translator);
        publisher = (TextView) findViewById(R.id.info_publisher);
        publish_year = (TextView) findViewById(R.id.info_publish_year);
        title = (TextView) findViewById(R.id.info_restitle);
        author_intro = (TextView) findViewById(R.id.info_author_intro);
        intro = (TextView) findViewById(R.id.info_intro);
        resLayoutAll = (LinearLayout) findViewById(R.id.res_layout_all);
        resLayoutLeft = (LinearLayout) findViewById(R.id.res_layout_left);
        resLayoutRight = (LinearLayout) findViewById(R.id.res_layout_right);
        resolver = this.getContentResolver();
        
        int width = resLayoutAll.getWidth();
        resLayoutLeft.setMinimumWidth(width/2);
        resLayoutRight.setMinimumWidth(width/2);
        Intent intent = this.getIntent();
        String id = intent.getStringExtra("bookId");
        ResourceType type = (ResourceType)intent.getExtras().get("type");
        
        showResInfo(id, type);
        //Toast.makeText(this, String.valueOf(type) ,Toast.LENGTH_SHORT).show();
        
        //downButton
        setButtonEnable(downButton, false);
        setButtonEnable(readButton, true);
        
        backButton.setOnClickListener(new OnClickListener(){
        	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub   
                
				v.setBackgroundResource(R.drawable.back02);       
				
				ResourceInfoActivity.this.finish();
			}
        	
        });
        
        downButton.setOnClickListener(new OnClickListener(){
        	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub   
                //v.setDrawingCacheBackgroundColor(color);
			}
        	
        });
        
        readButton.setOnClickListener(new OnClickListener(){
        	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub   
                
			}
        	
        });
    }
    
    private void showResInfo(String id, ResourceType type){
    	Cursor cur = null;
    	if(id == null) return;
    	
    	switch(type){
    	
    	case BOOK:
    		
    		try {
    			cur = resolver.query(Books.CONTENT_URI, null, Books._ID+"="+id, null, null);		
    			
    			int idCol = cur.getColumnIndex(Books._ID);
    			int titleCol = cur.getColumnIndex(Books._TITLE);
    			int authorCol = cur.getColumnIndex(Books._AUTHOR);
    			int descriptionCol = cur.getColumnIndex(Books._INTRO);
    			int progressCol = cur.getColumnIndex(Books._PROGRESS);
    			//int originalCol = cur.getColumnIndex(Books.);
    			
    			
    			while (cur.moveToNext()) {
    				//String tags = getBookTags(cur.getString(idCol));
    				
    				//resGridItems.add(new ResourceGridItem(cur.getString(idCol), cur.getString(titleCol), cur.getString(authorCol) ,tags , R.drawable.ic_launcher, cur.getInt(progressCol), cur.getString(descriptionCol), 0));	
    				//originname.setText(cur.getString());
    				//publisher.setText();
    				//publish_year.setText();
    				//author_intro.setText();
    				cover.setImageResource(R.drawable.ic_launcher);
    				author.setText(cur.getString(authorCol));
    				title.setText(cur.getString(titleCol));
    				intro.setText(cur.getString(descriptionCol));
    			}
    		} finally {
    			
    			
    		}
    		
    		return;
    	default:
    		return;
    	}
    	//cursor 
    	
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
