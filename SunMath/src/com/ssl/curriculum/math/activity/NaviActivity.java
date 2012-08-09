package com.ssl.curriculum.math.activity;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.component.HorizontalListView;
import com.ssl.curriculum.math.component.NavigationListView;
import com.sunshine.metadata.provider.MetadataContract;
import com.sunshine.metadata.provider.MetadataContract.Activities;
import com.sunshine.metadata.provider.MetadataContract.Sections;
import com.ssl.curriculum.math.presenter.NavigationMenuPresenter;

public class NaviActivity extends Activity {


	private static String[] ActivitiesInfo = new String[] { Activities._ID,
			Activities._NAME, Activities._SECTION_ID};

	private static String[] SectionInfo = new String[] { Sections._ID,
			Sections._DESCRIPTION };

	private NavigationListView navigationListView;
	private NavigationMenuPresenter menuPresenter;
	private TextView menuTitle;
	private ImageView backImageView;
	private TextView mztext;
	private LinearLayout test_linear;
	private ListView desListView;
	private HorizontalListView horizontalListView;
	private static ImageView mImageView;
	private static ContentResolver contentResolver;
	private static Context context;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();
		initComponent();
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		menuPresenter.loadMenuData();
	}

	public static void loadSectionData(Context context, ListView someListView,
			int position) {
		NaviActivity.context=context;
		ContentResolver contentResolver = context.getContentResolver();
		Cursor wenbenCursor = contentResolver.query(Sections.CONTENT_URI,
				SectionInfo, Sections._ID + "=?",
				new String[] { String.valueOf(position) }, null);
		SimpleCursorAdapter wenbenadapter = new SimpleCursorAdapter(context,
				R.layout.navi_activity_lesson_description_item, wenbenCursor,
				new String[] { Sections._DESCRIPTION },
				new int[] { R.id.title_des });
		someListView.setAdapter(wenbenadapter);

	}
	
	
	

	public static void loadActivitiesData(final Context context,HorizontalListView horizontalListView, final int sectionId, final ImageView mImageView){
		 
		NaviActivity.context=context; contentResolver = context.getContentResolver();
		
		SimpleCursorAdapter thumbAdapter = new SimpleCursorAdapter(context,R.layout.navi_activity_horizontal_item, contentResolver.query(Activities.CONTENT_URI,ActivitiesInfo, Activities._SECTION_ID + "=?",new String[] { String.valueOf(sectionId) }, null),new String[] { Activities._NAME }, new int[] { R.id.title}){
			
			public Object getItem(int position) {
		        return position;
		    }

		    public long getItemId(int position) {
		        return position;
		    }
		    
		    public View getView(int position, View view, ViewGroup viewGroup) {
		    	View retval = LayoutInflater.from(viewGroup.getContext()).inflate(
						R.layout.navi_activity_horizontal_item, null);
		    	
		    	
				TextView thutext=(TextView)retval.findViewById(R.id.title);
				ImageView thumbimage=(ImageView)retval.findViewById(R.id.image);
				
				
	        	InputStream is = null;
	        	
	        	 Cursor cursor = contentResolver.query(Activities.CONTENT_URI,ActivitiesInfo, Activities._SECTION_ID + "=?",new String[] { String.valueOf(sectionId) }, null);
	        	
	        	cursor.moveToPosition(position); 
	        	int actname = cursor.getColumnIndex(Activities._NAME);
	        	String activityname = cursor.getString(actname);
	        	thutext.setText(activityname);
	        	//System.out.println(activityname);
 	
	        	
				try {
					is = NaviActivity.context.getContentResolver().openInputStream(MetadataContract.Activities.getActivityThumbnailUri(sectionId));
					System.out.println("could find it");
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					System.out.println("could not find it");
				}
				
	    		Bitmap bitmap = BitmapFactory.decodeStream(is);    
	    		BitmapDrawable bd = new BitmapDrawable(bitmap);
 	    		thumbimage.setBackgroundDrawable(bd);
 	    		
	        	mImageView.setImageResource(R.drawable.ic_navi_section_thumbnail);
				return retval;
		    }	
			
		};
		
        horizontalListView.setAdapter(thumbAdapter);
				
				horizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,

					int position, long id) {
						Intent intent = new Intent();
						intent.setClass(context, MainActivity.class);
						((NaviActivity) context).startLearn(intent);

					}

				});
	}
	
	protected void startLearn(Intent intent) {
		startActivity(intent);
		
	}
	
	private void initUI(){
		setContentView(R.layout.navigation_layout);
		navigationListView = (NavigationListView) findViewById(R.id.navi_list_view);
		menuTitle = (TextView) findViewById(R.id.navigation_menu_title);
		backImageView = (ImageView) findViewById(R.id.navigation_menu_back_btn);
		mztext = (TextView) findViewById(R.id.textview_navi_activity_lesson_title);
		test_linear = (LinearLayout) this.findViewById(R.id.test_linear);
		desListView = (ListView) this.findViewById(R.id.listview_navi_activity_lesson_description);
		horizontalListView = (HorizontalListView) findViewById(R.id.listview);
		mImageView=(ImageView)findViewById(R.id.imageview_navi_activity_lesson_icon);

	}

	private void initComponent() {
		menuPresenter = new NavigationMenuPresenter(this, navigationListView,
				menuTitle, mztext, test_linear, desListView, horizontalListView,mImageView/*,thumbimage*/);
		navigationListView.setNextLevelMenuChangedListener(menuPresenter);
		backImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				menuPresenter.menuBack();
			}
		});

	}

}
