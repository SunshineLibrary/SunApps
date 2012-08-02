package com.ssl.curriculum.math.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
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
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.component.HorizontalListView;
import com.ssl.curriculum.math.component.NavigationListView;
import com.sunshine.metadata.provider.MetadataContract.Activities;
import com.sunshine.metadata.provider.MetadataContract.Sections;
import com.ssl.curriculum.math.presenter.NavigationMenuPresenter;

public class NaviActivity extends Activity {

	private static String[] ActivitiesInfo = new String[] { Activities._ID,
			Activities._NAME, Activities._SECTION_ID };

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

	public static void loadActivitiesData(final Context context,
			HorizontalListView horizontalListView, int position) {
		ContentResolver contentResolver = context.getContentResolver();
		Cursor actCursor = contentResolver.query(Activities.CONTENT_URI,
				ActivitiesInfo, Activities._SECTION_ID + "=?",
				new String[] { String.valueOf(position) }, null);
		SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(context,
				R.layout.viewitem, actCursor,
				new String[] { Activities._NAME }, new int[] { R.id.title });
		horizontalListView.setAdapter(mAdapter);
		horizontalListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,

					int position, long id) {
						Intent intent = new Intent();
						intent.setClass(context, MainActivity.class);
						((NaviActivity) context).startLearn(intent);

					}

				});

	}

	private void startLearn(Intent intent) {
		// TODO Auto-generated method stub
		startActivity(intent);

	}

	private void initUI() {
		setContentView(R.layout.navigation_layout);
		navigationListView = (NavigationListView) findViewById(R.id.navi_list_view);
		menuTitle = (TextView) findViewById(R.id.navigation_menu_title);
		backImageView = (ImageView) findViewById(R.id.navigation_menu_back_btn);
		mztext = (TextView) findViewById(R.id.textview_navi_activity_lesson_title);
		test_linear = (LinearLayout) this.findViewById(R.id.test_linear);
		desListView = (ListView) this
				.findViewById(R.id.listview_navi_activity_lesson_description);
		horizontalListView = (HorizontalListView) findViewById(R.id.listview);
	}

	private void initComponent() {
		menuPresenter = new NavigationMenuPresenter(this, navigationListView,
				menuTitle, mztext, test_linear, desListView, horizontalListView);
		navigationListView.setNextLevelMenuChangedListener(menuPresenter);
		backImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				menuPresenter.menuBack();
			}
		});

	}

}
