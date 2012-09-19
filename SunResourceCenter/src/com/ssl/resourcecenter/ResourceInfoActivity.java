package com.ssl.resourcecenter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ssl.metadata.provider.MetadataContract.Books;
import com.ssl.metadata.provider.MetadataContract.Downloadable;
import com.sunshine.sunresourcecenter.R;
import com.ssl.resourcecenter.contentresolver.ResourceContentResolver;
import com.ssl.resourcecenter.enums.ResourceType;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ResourceInfoActivity extends Activity {
	private ImageButton backButton;
	private ImageView cover;
	private TextView originname, author, translator, publisher, publish_year, title, author_intro, intro;
	private Button readButton, downButton;
	private ContentResolver resolver;
	private LinearLayout resLayoutAll, resLayoutLeft, resLayoutRight;
	private String resId;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_info);
        
        readButton = (Button) findViewById(R.id.info_button_read);
        downButton = (Button) findViewById(R.id.info_button_down);
        backButton = (ImageButton)findViewById(R.id.info_back_button);
        cover = (ImageView) findViewById(R.id.info_cover);
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
        resId = intent.getStringExtra("bookId");
        ResourceType type = (ResourceType)intent.getExtras().get("type");
        
        int status = showResInfo(resId, type);
        //Toast.makeText(this, String.valueOf(type) ,Toast.LENGTH_SHORT).show();
        
        setButtons(status);
       
        backButton.setOnClickListener(new OnClickListener(){
        	
			@Override
			public void onClick(View v) {
				//v.setBackgroundResource(R.drawable.back02);  
				ResourceInfoActivity.this.finish();
			}
        	
        });
        
        downButton.setOnClickListener(new OnClickListener(){
        	
			@Override
			public void onClick(View v) { 
                //v.setDrawingCacheBackgroundColor(color);
				ContentValues cv = new ContentValues();
				try{
					cv.put(Books._DOWNLOAD_STATUS, Downloadable.STATUS_QUEUED);
					int r = resolver.update(Books.CONTENT_URI, cv, Books._ID + "= " + resId, null);
					Log.i("download rolls updated", r+" rolls, id:"+resId);
				}catch(Exception e){
					Log.e("update error", e.getMessage());
				}
				setButtons(Downloadable.STATUS_QUEUED);
			}
        	
        });
        
        readButton.setOnClickListener(new OnClickListener(){
        	
			@Override
			public void onClick(View v) { 
				
				ContentValues cv = new ContentValues();
				try{
	                Intent openBookIntent = new Intent();
	               
	                openBookIntent.setAction("android.fbreader.action.VIEW");
	                openBookIntent.putExtra("bookId", Integer.valueOf(resId));
	                
	                startActivity(openBookIntent);
	                
	                SimpleDateFormat sDateFormat = new SimpleDateFormat("'yyyy-MM-dd hh:mm:ss'");
	                String date = sDateFormat.format(new Date());
	                cv.put(Books._STARTTIME, date);
	                StringBuffer selection = new StringBuffer(Books._ID);
	                selection.append(" = ").append(resId).append(" AND ").append(Books._STARTTIME).append(" not null ");
					resolver.update(Books.CONTENT_URI, cv, selection.toString(), null);
					
				}catch(Exception e){
					Log.e("Exception calling SunReader", e.getMessage());
				}
                
			}
        	
        });
    }
    
    @Override
	protected void onResume() {
		super.onResume();
	}



	private int showResInfo(String id, ResourceType type){
    	Cursor cur = null;
    	int status = 0;
    	if(id == null) return status;
    	
    	switch(type){
    	
    	case BOOK:
    		
    		try {
//    			cur = resolver.query(BookInfo.CONTENT_URI, null, BookInfo._BOOK_ID+"="+id, null, null);		
//    			
//    			int idCol = cur.getColumnIndex(BookInfo._BOOK_ID);
//    			int titleCol = cur.getColumnIndex(BookInfo._TITLE);
//    			int authorCol = cur.getColumnIndex(BookInfo._AUTHOR);
//    			int descriptionCol = cur.getColumnIndex(BookInfo._INTRO);
//    			int authorIntroCol = cur.getColumnIndex(BookInfo._AUTHOR_INTRO);
//    			int publisherCol = cur.getColumnIndex(BookInfo._PUBLISHER);
//    			int pubYearCol = cur.getColumnIndex(BookInfo._PUBLICATION_YEAR);
//    			//int tagCol = cur.getColumnIndex(BookInfo._TAGS);
//    			int downStatusCol = cur.getColumnIndex(BookInfo._DOWNLOAD_STATUS);
//    			//int progressCol = cur.getColumnIndex(Books._PROGRESS);
    			
    			cur = resolver.query(Books.CONTENT_URI, null, Books._ID+"="+id, null, null);		
    			
    			int idCol = cur.getColumnIndex(Books._ID);
    			int titleCol = cur.getColumnIndex(Books._TITLE);
    			int authorCol = cur.getColumnIndex(Books._AUTHOR);
    			int descriptionCol = cur.getColumnIndex(Books._INTRO);
    			//int authorIntroCol = cur.getColumnIndex(Books._AUTHOR_INTRO);
    			int publisherCol = cur.getColumnIndex(Books._PUBLISHER);
    			int pubYearCol = cur.getColumnIndex(Books._PUBLICATION_YEAR);
    			//int tagCol = cur.getColumnIndex(BookInfo._TAGS);
    			int downStatusCol = cur.getColumnIndex(Books._DOWNLOAD_STATUS);
    			//int progressCol = cur.getColumnIndex(Books._PROGRESS);
    			
    			Bitmap bm = null;
    			
    			while (cur.moveToNext()) {
    				cover.setImageResource(R.drawable.ic_launcher);
    				try {
    					bm = ResourceContentResolver.getBitmap(cur.getString(idCol), resolver);
    				} catch (IOException e) {
    					//default image
    					bm = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);
    				}
    				cover.setImageBitmap(bm);
    				
    				//String tags = cur.getString(tagCol);
    				
    				publisher.setText(cur.getString(publisherCol));
    				publish_year.setText(cur.getString(pubYearCol));
    				//author_intro.setText(cur.getString(authorIntroCol));
    				author.setText(cur.getString(authorCol));
    				title.setText(cur.getString(titleCol));
    				intro.setText(cur.getString(descriptionCol));
    				
    				status = cur.getInt(downStatusCol);
    			}
    		} 
//    		catch (IOException e) {
//				e.printStackTrace();
//			} 
    		finally {
    			
    		}
    		if(cur != null)
    			cur.close();
    		return status;
    	case AUDIO:
    		return status;
    	case VEDIO:
    		return status;
    	default:
    		return status;
    	}
    	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_resource_info, menu);
        return true;
    }
    
    private void setButtons(int status){
    	
    	if(status == Downloadable.STATUS_DOWNLOADED){
    		setButton(downButton, false, "已下载");
     	    setButton(readButton, true, "阅读");
    	}else if(status == Downloadable.STATUS_QUEUED || status == Downloadable.STATUS_DOWNLOADING){
    		setButton(downButton, false, "正在下载");
     	    setButton(readButton, false, "阅读");
    	}else{
    		setButton(downButton, true, "下载");
     	    setButton(readButton, false, "阅读");
    	}
    	
    	
         
    }
    
    private void setButton(Button button, boolean enable, CharSequence text){
    	button.setEnabled(enable);
    	button.setText(text);
    	if(enable){
    		button.setBackgroundColor(getResources().getColor(R.color.chrome));
    	}else {
    		button.setBackgroundColor(getResources().getColor(R.color.info_grey));
    	}
    }
    
}
