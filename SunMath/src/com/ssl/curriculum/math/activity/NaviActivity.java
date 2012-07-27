package com.ssl.curriculum.math.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.component.NavigationListView;
import com.sunshine.metadata.provider.MetadataContract.Lessons;
import com.ssl.curriculum.math.presenter.DetailsPagePresenter;
import com.ssl.curriculum.math.presenter.NavigationMenuPresenter;

public class NaviActivity extends Activity {

	private String[] MZ = new String[] { Lessons._ID,Lessons._NAME };

	private NavigationListView navigationListView;
	private NavigationMenuPresenter menuPresenter;
	private TextView menuTitle;
	private ImageView backImageView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();
		initComponent();
		loadRightData();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		menuPresenter.loadMenuData();
		something();
	}

	private void loadRightData() {

		ListView desListView = (ListView) findViewById(R.id.listview_navi_activity_lesson_description);

		ContentResolver contentResolver = this.getBaseContext()
				.getContentResolver();
		
		Cursor wenbenCursor = contentResolver.query(Lessons.CONTENT_URI, MZ, null, null,
				null);
		SimpleCursorAdapter wenbenadapter = new SimpleCursorAdapter(this,
				R.layout.navi_activity_lesson_description_item, wenbenCursor,
				new String[] { Lessons._ID }, new int[] { R.id.title_des });

		desListView.setAdapter(wenbenadapter);

		/*
		  String mingzi = null; ContentResolver contentResolver2 =
		  this.getBaseContext() .getContentResolver();
		  
		  Uri uri2 = Uri.parse("content://com.sunshine.metadata.provider/lessons");
		  
		  //Uri resultUri = ContentUris.withAppendedId(uri2, 2);
		  
		  Cursor MZCursor = contentResolver2 .query(uri, MZ, null, null,
		  null); int some= MZCursor.getColumnIndex(Lessons._NAME);
		  mingzi=MZCursor.getString(some); TextView mztext = (TextView)
		  findViewById(R.id.textview_navi_activity_lesson_title);
		  mztext.setText(mingzi);*/
		 
	}

	private void something() {
		HorizontalListView listview = (HorizontalListView) findViewById(R.id.listview);
		listview.setAdapter(mAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,

			int position, long id) {
				Intent intent = new Intent();
				intent.setClass(NaviActivity.this, MainActivity.class);
				startActivity(intent);

			}

		});

	}

	private static String[] dataObjects = new String[] { "Text #1", "Text #2",
			"Text #3", "Text #4", "Text #5", "Text #6", "Text #7", "Text #8",
			"Text #9" };

	private BaseAdapter mAdapter = new BaseAdapter() {

		public int getCount() {
			return dataObjects.length;
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View retval = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.viewitem, null);
			TextView title = (TextView) retval.findViewById(R.id.title);
			title.setText(dataObjects[position]);

			return retval;
		}

	};

	private void initUI() {
		setContentView(R.layout.listviewdemo);
		navigationListView = (NavigationListView) findViewById(R.id.navi_list_view);
		menuTitle = (TextView) findViewById(R.id.navigation_menu_title);
		backImageView = (ImageView) findViewById(R.id.navigation_menu_back_btn);
	}

	private void initComponent() {
		menuPresenter = new NavigationMenuPresenter(this, navigationListView,
				menuTitle);
		navigationListView.setNextLevelMenuChangedListener(menuPresenter);
		backImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				menuPresenter.menuBack();
			}
		});

	}

}
