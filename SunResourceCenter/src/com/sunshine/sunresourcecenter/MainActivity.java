package com.sunshine.sunresourcecenter;

import com.sunshine.metadata.provider.*;
import com.sunshine.metadata.provider.MetadataContract.BookCollections;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {

	interface TabSwitched {
		public void OnTabSwitched(int index);
	}
	
	private enum gridType{
		GRIDTYPE_RES_TODOWNLOAD,
		GRIDTYPE_RES_INPROGRESS, 
		GRIDTYPE_RESLIST,
		GRIDTYPE_CATEGORY,
		GRIDTYPE_RECOMMAND
	};
	
	private enum viewType {
		RES_READING,
		RES_ALL,
		RES_RECENT,
		RES_LEARN_NEW,
		RES_READED,
		DOWN_RECOMMAND,
		DOWN_HOT,
		DOWN_CATEGORY,
		DOWN_LIKE,
		DOWN_LIST,
		DOWN_LIST_RES,
		DOWN_CATEGORY_RES
	};
	private gridType currentGridType;
	private viewType currentViewType;
	private ResourceType currentResType;
	private List<Object> gridItems;
	private GridView gridView;
	private LinearLayout resnav;
	private LinearLayout downnav;
	private LinearLayout typenav;
	private Spinner mainnav;
	private ContentResolver resolver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		gridView = (GridView) findViewById(R.id.gridview);
		resnav = (LinearLayout) findViewById(R.id.resnav);
		downnav = (LinearLayout) findViewById(R.id.downnav);
		typenav = (LinearLayout) findViewById(R.id.typenav);
		mainnav = (Spinner) findViewById(R.id.mainnav);
		
		resolver = MainActivity.this.getContentResolver();
		// change via tab state
		currentGridType = gridType.GRIDTYPE_RES_INPROGRESS;
		currentViewType = viewType.RES_READING;
		currentResType = ResourceType.BOOK;

		// bound data to grids
		//gridItems = boundGridData(currentResType, currentGridType);

		// show gridView of different types
		showGridView(gridItems, currentResType, currentGridType, currentViewType);

		// grid listener
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Intent intent;
				
				switch(currentViewType){
				case RES_READING:
				case RES_ALL:
				case RES_RECENT:
				case RES_LEARN_NEW:
				case RES_READED:
					//book
					intent = new Intent();
					intent.putExtra("bookId", gridItems.get(position).toString());
					intent.setClass(MainActivity.this, ResourceInfoActivity.class);
					startActivity(intent);
					break;
					
				case DOWN_RECOMMAND:
				case DOWN_HOT:
				case DOWN_LIKE:
				case DOWN_LIST_RES:
				case DOWN_CATEGORY_RES:
					//collections
					intent = new Intent();
					intent.setClass(MainActivity.this, ResourceInfoActivity.class);
					startActivity(intent);
					break;
					
				case DOWN_CATEGORY:
					currentGridType = gridType.GRIDTYPE_RES_TODOWNLOAD;
					currentViewType = viewType.DOWN_CATEGORY_RES;
					showGridView(gridItems, currentResType, currentGridType, currentViewType);
					break;
				case DOWN_LIST:
					currentGridType = gridType.GRIDTYPE_RES_TODOWNLOAD;
					currentViewType = viewType.DOWN_LIST_RES;
					showGridView(gridItems, currentResType, currentGridType, currentViewType);
					break;
				default:
					break;
				}
				
//				Toast.makeText(MainActivity.this, gridItems.get(position).toString(),
//						Toast.LENGTH_SHORT).show();

			}
		});
		
		final TabSwitched resnav_switch = new TabSwitched() {

			@Override
			public void OnTabSwitched(int index) {
				switch (index) {
				case 0:// reading
					currentGridType = gridType.GRIDTYPE_RES_INPROGRESS;
					currentViewType = viewType.RES_READING;
					break;
				case 1:// all
					currentGridType = gridType.GRIDTYPE_RES_INPROGRESS;
					currentViewType = viewType.RES_ALL;
					break;
				case 2:// recent_downloaded
					currentGridType = gridType.GRIDTYPE_RES_INPROGRESS;
					currentViewType = viewType.RES_RECENT;
					break;
				case 3:// learn_new
					currentGridType = gridType.GRIDTYPE_RES_INPROGRESS;
					currentViewType = viewType.RES_LEARN_NEW;
					break;
				case 4:// readed
					currentGridType = gridType.GRIDTYPE_RES_INPROGRESS;
					currentViewType = viewType.RES_READED;
					break;
				}
				gridView.setVisibility(View.VISIBLE);
				showGridView(gridItems, currentResType, currentGridType, currentViewType);

			}
		};
		final TabSwitched typenav_switch = new TabSwitched() {

			@Override
			public void OnTabSwitched(int index) {
				gridView.setVisibility(View.INVISIBLE);
				switch (index) {
				case 0:// book
					currentResType = ResourceType.BOOK;
					break;
				case 1:// video
					currentResType = ResourceType.VEDIO;
					break;
				case 2:// audio
					currentResType = ResourceType.AUDIO;
					break;
				}
				gridView.setVisibility(View.VISIBLE);
				showGridView(gridItems, currentResType, currentGridType, currentViewType);
			}
		};
		final TabSwitched downnav_switch = new TabSwitched() {

			@Override
			public void OnTabSwitched(int index) {
				gridView.setVisibility(View.INVISIBLE);
				switch (index) {
				case 0: // recommend
					currentGridType = gridType.GRIDTYPE_RECOMMAND;
					currentViewType = viewType.DOWN_RECOMMAND;
					break;
				case 1: // hot
					currentGridType = gridType.GRIDTYPE_RES_TODOWNLOAD;
					currentViewType = viewType.DOWN_HOT;
					break;
				case 2: // category
					currentGridType = gridType.GRIDTYPE_CATEGORY;
					currentViewType = viewType.DOWN_CATEGORY;
					break;
				case 3: // like
					currentGridType = gridType.GRIDTYPE_RES_TODOWNLOAD;
					currentViewType = viewType.DOWN_LIKE;
					break;
				case 4: // list
					currentGridType = gridType.GRIDTYPE_RESLIST;
					currentViewType = viewType.DOWN_LIST;
					break;
				default:
					break;
				}
				gridView.setVisibility(View.VISIBLE);
				showGridView(gridItems, currentResType, currentGridType, currentViewType);
			}
		};

		simulateTabs(resnav, resnav_switch);
		simulateTabs(typenav, typenav_switch);
		simulateTabs(downnav, downnav_switch);
		
		String[] curs = getResources().getStringArray(R.array.main_menu);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_item, curs);
		adapter.setDropDownViewResource(R.layout.spinner_dropdown);
		mainnav.setAdapter(adapter);
		mainnav.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View spinner,
					int position, long id) {
				if (position == 0) {
					resnav.setVisibility(View.VISIBLE);
					downnav.setVisibility(View.INVISIBLE);
					resnav_switch
							.OnTabSwitched(getSimulatedTabSelected(resnav));
				} else {
					resnav.setVisibility(View.INVISIBLE);
					downnav.setVisibility(View.VISIBLE);
					downnav_switch.OnTabSwitched(getSimulatedTabSelected(downnav));
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void simulateTabs(final ViewGroup container,
			final TabSwitched callback) {
		final int l = container.getChildCount();
		for (int i = 0; i < l; i++) {
			View v = container.getChildAt(i);
			if (v instanceof CompoundButton) {
				((CompoundButton) v)
						.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								if (isChecked) {
									int checked = -1;
									for (int i = 0; i < l; i++) {
										View v = container.getChildAt(i);
										if (v instanceof CompoundButton
												&& v != buttonView) {
											((CompoundButton) v)
													.setChecked(false);
										} else if (v == buttonView) {
											checked = i;
										}
									}
									if (checked >= 0) {
										callback.OnTabSwitched(checked);
									}
								} else {
									boolean anyChecked = false;
									for (int i = 0; i < l; i++) {
										View v = container.getChildAt(i);
										if (v instanceof CompoundButton) {
											anyChecked = anyChecked
													|| ((CompoundButton) v)
															.isChecked();
										}
									}
									if (!anyChecked) {
										buttonView.setChecked(true);
									}
								}
							}
						});
			}
		}
	}

	private int getSimulatedTabSelected(final ViewGroup container) {
		final int l = container.getChildCount();
		int selected = -1;
		for (int i = 0; i < l; i++) {
			View v = container.getChildAt(i);
			if (v instanceof CompoundButton) {
				if (((CompoundButton) v).isChecked()) {
					selected = i;
				}
			}
		}
		return selected;
	}

	private void showGridView(List<Object> itemList, ResourceType resType,
			gridType theGridType, viewType theViewType) {
		
		gridItems = boundGridData(resType, theViewType);
		itemList = gridItems;
		
		switch (theGridType) {
		case GRIDTYPE_RES_TODOWNLOAD:
			// res grid in download page
			ResourceGridAdapter adapter = new ResourceGridAdapter(itemList,
					false, this);
			gridView.setAdapter(adapter);
			break;
		case GRIDTYPE_RES_INPROGRESS:
			// res grid in reading page
			ResourceGridAdapter adapter2 = new ResourceGridAdapter(itemList,
					true, this);
			gridView.setAdapter(adapter2);
			break;
		case GRIDTYPE_RESLIST:
			// resource list page
			ResourceListGridAdapter adapter3 = new ResourceListGridAdapter(
					itemList, this);
			gridView.setAdapter(adapter3);
			break;
		case GRIDTYPE_CATEGORY:
			// category page
			CategoryGridAdapter adapter4 = new CategoryGridAdapter(itemList,
					this);
			gridView.setAdapter(adapter4);
			break;

		default:
			break;
		}

	}
	
	private List<Object> boundGridData(ResourceType resType, viewType theViewType) {
		// need modify!! content resolver here
		
		switch (theViewType) {

		case RES_READING:
		case RES_ALL:
		case RES_RECENT:
		case RES_READED:
		case RES_LEARN_NEW:
		case DOWN_HOT:
		case DOWN_LIKE:
		case DOWN_CATEGORY_RES:
		case DOWN_LIST_RES:
			ArrayList<ResourceGridItem> gridItems = new ArrayList<ResourceGridItem>();

//			if (resType == ResourceType.BOOK) {
//				try {
//					// String[] projection = new String[]{};
//					Cursor c = resolver.query(BookCollections.CONTENT_URI,
//							null, null, null, null);
//
//					int idCol = c.getColumnIndexOrThrow(BookCollections._ID);
//					int titleCol = c.getColumnIndexOrThrow(BookCollections._NAME);
//					int authorCol = c.getColumnIndexOrThrow(BookCollections._AUTHOR);
//					int descriptionCol = c.getColumnIndexOrThrow(BookCollections._DESCRIPTION);
//
//					while (c.moveToNext()) {
//						gridItems.add(new ResourceGridItem(c.getString(titleCol), c.getString(authorCol),"", R.drawable.ic_launcher, 0, c.getString(descriptionCol)));
//					}
//				} finally {
//
//				}
//			}

			// title for test
			String[] titles = new String[] { "¹þÀû²¨ÌØ´óÕ½ºùÂ«ÍÞ", "¹þÀû²¨ÌØ´óÕ½°ÂÌØÂü",
					"¹þÀû²¨ÌØÓëËïÎò¿Õ", "¹þÀû²¨ÌØÓëòùòðÏÀ", "¹þÀû²¨ÌØÓëºùÂ«ÍÞ²»µÃ²»ËµµÄ¹ÊÊÂ" };
			// author for test
			String[] author = new String[] { "author", "author", "author",
					"author", "author" };
			// tag for test
			String[] tags = new String[] { "tag1/tag2", "tag1/tag2",
					"tag1/tag2", "tag1/tag2", "tag1/tag2" };
			// description for test
			String[] description = new String[] {
					"À²À²À²À²À²À²À²£¬À²À²À²À²À²À²À²À²£¬À²À²À²À²À²À²À²£¬À²À²À²À²À²À²À²¡£\n\rÀ²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²",
					"‡å", "‡å", "‡å", "‡å" };
			// image for test
			int[] images = { R.drawable.ic_launcher, R.drawable.ic_launcher,
					R.drawable.togglebutton_type_book, R.drawable.search,
					R.drawable.ic_launcher };

			int[] progress = { 30, 40, 60, 70, 99 };

			for (int i = 0; i < titles.length; i++) {
				 gridItems.add(new ResourceGridItem(String.valueOf(i), titles[i],author[i], tags[i], images[i], progress[i],description[i]));
			}

			return (List) gridItems;
		
		case DOWN_RECOMMAND:
			return null;
		case DOWN_CATEGORY:
			String[] cateNames = new String[] { "ÖÐ¹úÎÄÑ§", "ÖÐ¹úÎÄÑ§", "ÖÐ¹úÎÄÑ§", "ÖÐ¹úÎÄÑ§",
					"ÖÐ¹úÎÄÑ§", "ÖÐ¹úÎÄÑ§", "ÖÐ¹úÎÄÑ§" };
			int[] cateCount = new int[] { 1, 2, 3, 4, 5, 6, 7, 8 };

			ArrayList<CategoryGridItem> gridItems3 = new ArrayList<CategoryGridItem>();
			for (int i = 0; i < cateNames.length; i++) {
				gridItems3.add(new CategoryGridItem(cateNames[i], cateCount[i],
						0, null));
			}

			return (List) gridItems3;
		
		case DOWN_LIST:
			// title for test
						String[] listtitles = new String[] { "¹þÀû²¨ÌØµÄÊéµ¥", "ºùÂ«ÍÞµÄÊéµ¥", "Ð¡Ã÷µÄÊéµ¥",
								"Ð¡Ã÷µÄÊéµ¥", "Ð¡Ã÷µÄÊéµ¥", "Ð¡Ã÷µÄÊéµ¥", "Ð¡Ã÷µÄÊéµ¥", "Ð¡Ã÷µÄÊéµ¥" };
						// author for test
						String[] listauthor = new String[] { "¹þÀû²¨ÌØ", "ºùÂ«ÍÞ", "Ð¡Ã÷", "Ð¡Ã÷",
								"Ð¡Ã÷", "Ð¡Ã÷", "Ð¡Ã÷", "Ð¡Ã÷" };
						// tag for test
						String[] listtags = new String[] { "tag1/tag2/tag3/tag4",
								"tag1/tag2", "tag1/tag2", "tag1/tag2", "tag1/tag2",
								"tag1/tag2", "tag1/tag2", "tag1/tag2" };
						// description for test
						String[] listintro = new String[] {
								"À²À²À²À²À²À²À²£¬À²À²À²À²À²À²À²À²£¬À²À²À²À²À²À²À²£¬À²À²À²À²À²À²À²¡£\n\rÀ²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²À²",
								"‡å", "‡å", "‡å", "‡å", "‡å", "‡å", "‡å" };

						int[] count = new int[] { 12, 10, 3, 122, 1, 2, 4, 18 };

						ArrayList<ResourceListGridItem> gridItems2 = new ArrayList<ResourceListGridItem>();
						for (int i = 0; i < listtitles.length; i++) {
							gridItems2.add(new ResourceListGridItem(listtitles[i],
									listauthor[i], listtags[i], count[i], listintro[i]));
						}

						return (List) gridItems2;

		default:
			return null;

		}

	}
	
	private String getTags() {

		return null;
	}
}
