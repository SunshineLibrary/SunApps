package com.ssl.curriculum.math.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.component.HorizontalListView;
import com.ssl.curriculum.math.component.NavigationListView;
import com.ssl.curriculum.math.component.NavigationMenuItem;
import com.sunshine.metadata.provider.MetadataContract.Activities;
import com.sunshine.metadata.provider.MetadataContract.Sections;
import com.ssl.curriculum.math.presenter.NavigationMenuPresenter;

public class NaviActivity extends Activity {

	private String[] MZ = new String[] { Activities._ID, Activities._NAME };

	private String[] MZ2 = new String[] { Sections._ID, Sections._NAME };

	private NavigationListView navigationListView;
	private NavigationMenuPresenter menuPresenter;
	private TextView menuTitle;
	private ImageView backImageView;
	private TextView mztext;
	private LinearLayout test_linear;
	private ListView desListView;
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
		something();
	}
	//public static void any(ListView anylistview,int index){
		//anylistview.setBackgroundResource(R.drawable.bigbtn_hover);
		
	
	//}

	public static void loadRightData(Context context, ListView someListView, int position) {
	    ContentResolver contentResolver = context.getContentResolver();
		String[] info = new String[] { Sections._ID, Sections._DESCRIPTION };
		Cursor wenbenCursor = contentResolver.query(Sections.CONTENT_URI, info,Sections._ID + "=?", new String[]{String.valueOf(position)}, null);
		SimpleCursorAdapter wenbenadapter = new SimpleCursorAdapter(context,
				R.layout.navi_activity_lesson_description_item, wenbenCursor,
				new String[] {Sections._DESCRIPTION}, new int[] { R.id.title_des });
		someListView.setAdapter(wenbenadapter);

	}
	public void something() {
		HorizontalListView listview = (HorizontalListView) findViewById(R.id.listview);
		ContentResolver contentResolver = this.getBaseContext()
				.getContentResolver();
		Cursor actCursor = contentResolver.query(Activities.CONTENT_URI, MZ,
				null, null, null);
		SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this,
				R.layout.viewitem, actCursor,
				new String[] { Activities._NAME }, new int[] { R.id.title });
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

	/*
	 * private static String[] dataObjects = new String[] { "Text #1",
	 * "Text #2", "Text #3", "Text #4", "Text #5", "Text #6", "Text #7",
	 * "Text #8", "Text #9" };
	 * 
	 * 
	 * {
	 * 
	 * public int getCount() { return MZ.length; }
	 * 
	 * public Object getItem(int position) { return null; }
	 * 
	 * public long getItemId(int position) { return 0; }
	 * 
	 * public View getView(int position, View convertView, ViewGroup parent) {
	 * View retval =
	 * LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem,
	 * null);
	 * 
	 * TextView title = (TextView) retval.findViewById(R.id.title);
	 * title.setText(dataObjects[position]);
	 * 
	 * return retval; }
	 * 
	 * };
	 */

	private void initUI() {
		setContentView(R.layout.navigation_layout);
		navigationListView = (NavigationListView) findViewById(R.id.navi_list_view);
		menuTitle = (TextView) findViewById(R.id.navigation_menu_title);
		backImageView = (ImageView) findViewById(R.id.navigation_menu_back_btn);
		mztext = (TextView) findViewById(R.id.textview_navi_activity_lesson_title);
		test_linear = (LinearLayout) this.findViewById(R.id.test_linear);
		desListView = (ListView) this.findViewById(R.id.listview_navi_activity_lesson_description);
	}

	private void initComponent() {
		menuPresenter = new NavigationMenuPresenter(this, navigationListView,
				menuTitle, mztext, test_linear, desListView);
		navigationListView.setNextLevelMenuChangedListener(menuPresenter);
		backImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				menuPresenter.menuBack();
			}
		});

	}

}
