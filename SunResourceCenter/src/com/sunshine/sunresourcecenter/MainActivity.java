package com.sunshine.sunresourcecenter;

import com.sunshine.metadata.provider.*;
import com.sunshine.metadata.provider.MetadataContract.BookCollections;
import com.sunshine.metadata.provider.MetadataContract.Books;

import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
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
import android.widget.RelativeLayout;
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
		RES_READED,
		RES_READ_HISTORY,
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
	private LinearLayout recommandView;
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
		recommandView = (LinearLayout) findViewById(R.id.recommand_view);
		
		resolver = MainActivity.this.getContentResolver();
		
		// change via tab state
		currentGridType = gridType.GRIDTYPE_RES_INPROGRESS;
		currentViewType = viewType.RES_READING;
		currentResType = ResourceType.BOOK;
		recommandView.setVisibility(View.INVISIBLE);
		
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
				case RES_READED:
				case RES_READ_HISTORY:
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
					showGridView(gridItems, currentResType, currentGridType, currentViewType,gridItems.get(position).toString(),null);
					break;
				case DOWN_LIST:
					currentGridType = gridType.GRIDTYPE_RES_TODOWNLOAD;
					currentViewType = viewType.DOWN_LIST_RES;
					showGridView(gridItems, currentResType, currentGridType, currentViewType,null,gridItems.get(position).toString());
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
					currentViewType = viewType.RES_READED;
					break;
				case 4:// readed
					currentGridType = gridType.GRIDTYPE_RES_INPROGRESS;
					currentViewType = viewType.RES_READ_HISTORY;
					break;
				}
				gridView.setVisibility(View.VISIBLE);
				recommandView.setVisibility(View.INVISIBLE);
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
				recommandView.setVisibility(View.INVISIBLE);
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
				recommandView.setVisibility(View.INVISIBLE);
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
			gridType theGridType, viewType theViewType){
		showGridView(itemList, resType, theGridType, theViewType, null, null);
	}

	private void showGridView(List<Object> itemList, ResourceType resType,
			gridType theGridType, viewType theViewType, String categoryId, String listId) {
		
		gridItems = boundGridData(resType, theViewType, categoryId, listId);
		itemList = gridItems;
		
		switch (theGridType) {
		case GRIDTYPE_RES_TODOWNLOAD:
			// res grid in download page
			ResourceGridAdapter adapter = new ResourceGridAdapter(itemList,
					false, this);
			//gridView.
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
		case GRIDTYPE_RECOMMAND:
			gridView.setVisibility(View.INVISIBLE);
			recommandView.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}

	}
	
	
	private List<Object> boundGridData(ResourceType resType, viewType theViewType, String categoryId, String listId) {
		// need modify!! content resolver here
		ArrayList<ResourceGridItem> resGridItems = new ArrayList<ResourceGridItem>();
		ArrayList<CategoryGridItem> cateGridItems = new ArrayList<CategoryGridItem>();
		ArrayList<ResourceListGridItem> listGridItems = new ArrayList<ResourceListGridItem>();
		Cursor cur = null;
		Uri contentUri = null;
		String[] projection = null, cols = null;
		String  selection = null;
		
		String s = Books._PROGRESS;
		switch(resType){
		case BOOK:
			switch (theViewType) {
			//books
			case RES_READING:
				if(selection == null) {
					// AND progress > 0 AND progress < 100
					//selection = "download_status = 'DOWNLOADED'";
					}
				
			case RES_ALL:
				if(selection == null) {
					//selection = "download_status = 'DOWNLOADED'";
					}
				
			case RES_RECENT:
				if(selection == null) {
					// AND download_time > ...
					//selection = "download_status = 'DOWNLOADED' ";
					}
				
			
				
			case RES_READED:
				//WEN GU ZHI XIN
				if(selection == null) {
					// AND readed
					//selection = "download_status = 'DOWNLOADED'";
					}
				
				if(contentUri == null){
					contentUri = Books.CONTENT_URI;
				}
				if(cols == null){
					cols = new String[]{Books._ID, Books._TITLE, Books._AUTHOR, Books._DESCRIPTION, Books._PROGRESS};
				}
			//collections
			case DOWN_HOT:
				if(selection == null) {
					//AND HOT
					//selection = "download_status = 'NOT_DOWNLOADED'";
					}
				
			case DOWN_LIKE:
				if(selection == null) {
					//AND LIKE
					//selection = "download_status = 'NOT_DOWNLOADED'";
					}
			case DOWN_CATEGORY_RES:
				if(selection == null) {
					//AND category_id = categoryId
					//selection = "download_status = 'NOT_DOWNLOADED'";
					}
			case DOWN_LIST_RES:
				if(selection == null) {
					//AND list_id = listId
					//selection = "download_status = 'NOT_DOWNLOADED'";
					}
				
				//
				if(cols == null){
					cols = new String[]{BookCollections._ID, BookCollections._NAME, BookCollections._AUTHOR, BookCollections._DESCRIPTION, null};
				}
				if(contentUri == null){
					contentUri = BookCollections.CONTENT_URI;
				}
				
				try {
					cur = resolver.query(contentUri, projection, selection, null, null);		

					int idCol = cur.getColumnIndexOrThrow(cols[0]);
					int titleCol = cur.getColumnIndexOrThrow(cols[1]);
					int authorCol = cur.getColumnIndexOrThrow(cols[2]);
					int descriptionCol = cur.getColumnIndexOrThrow(cols[3]);
					//int prgressCol = cur.getColumnIndexOrThrow(cols[4]);
					
					while (cur.moveToNext()) {
						//cur.getInt(prgressCol)
						resGridItems.add(new ResourceGridItem(cur.getString(idCol), cur.getString(titleCol), cur.getString(authorCol),"", R.drawable.ic_launcher, 10, cur.getString(descriptionCol)));
					}
				} finally {

				}

				return (List) resGridItems;
			case RES_READ_HISTORY:
				//YUE DU LI CHENG
				return null;
				
			case DOWN_RECOMMAND:
				return null;
			case DOWN_CATEGORY:

				return (List) cateGridItems;
			
			case DOWN_LIST:

				return (List) listGridItems;

			default:
				return null;

			}
			
		case AUDIO:
			
			return null;
		case VEDIO:
			
			return null;
		default: 
			return null;
		}
	}
	
	private String getTags() {

		return null;
	}
}
