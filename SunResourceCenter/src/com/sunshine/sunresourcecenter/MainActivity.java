package com.sunshine.sunresourcecenter;

import com.sunshine.metadata.provider.*;
import com.sunshine.metadata.provider.MetadataContract.BookCollections;
import com.sunshine.metadata.provider.MetadataContract.Books;
import com.sunshine.sunresourcecenter.R;
import com.sunshine.sunresourcecenter.adapter.CategoryGridAdapter;
import com.sunshine.sunresourcecenter.adapter.ResourceGridAdapter;
import com.sunshine.sunresourcecenter.adapter.ResourceListGridAdapter;
import com.sunshine.sunresourcecenter.griditem.ResourceGridItem;

import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
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
		DOWN_CATEGORY_RES,
		COLLECTION
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
	private EditText searchBar;
	private ResourceContentResolver resolver;

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
		searchBar = (EditText) findViewById(R.id.searchbar);
		
		resolver = new ResourceContentResolver(MainActivity.this.getContentResolver());
		
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
					intent.putExtra("type", currentResType);
					intent.setClass(MainActivity.this, ResourceInfoActivity.class);
					startActivity(intent);
					break;
					
				case DOWN_RECOMMAND:
				case DOWN_HOT:
				case DOWN_LIKE:
				case DOWN_LIST_RES:
				case DOWN_CATEGORY_RES:
					
					ResourceGridItem item = (ResourceGridItem)gridItems.get(position);
					//Toast.makeText(MainActivity.this, String.valueOf(item.getResCount()),Toast.LENGTH_SHORT).show();
					if(item.getResCount() > 1){
						//collections
						currentGridType = gridType.GRIDTYPE_RES_TODOWNLOAD;
						currentViewType = viewType.COLLECTION;
						showGridView(gridItems, currentResType, currentGridType, currentViewType, gridItems.get(position).toString());
					}else{
						intent = new Intent();
						intent.setClass(MainActivity.this, ResourceInfoActivity.class);
						intent.putExtra("bookId", resolver.getSingleResIdOfCollection(gridItems.get(position).toString()));
						intent.putExtra("type", currentResType);
						startActivity(intent);
					}
					
					break;
					
				case DOWN_CATEGORY:
					currentGridType = gridType.GRIDTYPE_RES_TODOWNLOAD;
					currentViewType = viewType.DOWN_CATEGORY_RES;
					showGridView(gridItems, currentResType, currentGridType, currentViewType,gridItems.get(position).toString());
					break;
				case DOWN_LIST:
					currentGridType = gridType.GRIDTYPE_RES_TODOWNLOAD;
					currentViewType = viewType.DOWN_LIST_RES;
					showGridView(gridItems, currentResType, currentGridType, currentViewType,gridItems.get(position).toString());
					break;
				default:
					break;
				}
				
//				Toast.makeText(MainActivity.this, gridItems.get(position).toString(),
//						Toast.LENGTH_SHORT).show();

			}
		});
		
		searchBar.setOnKeyListener(new OnKeyListener(){

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER){
					searchCurrentGrid(searchBar.getText().toString());
					InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);   
					if(imm.isActive()){  
						imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0 );  
					}
					Toast.makeText(MainActivity.this, searchBar.getText().toString(),Toast.LENGTH_SHORT).show();
					return true;
				}
				return false;
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
	
	private void searchCurrentGrid(String key){
		
	}
	
	private void showGridView(List<Object> itemList, ResourceType resType,
			gridType theGridType, viewType theViewType){
		showGridView(itemList, resType, theGridType, theViewType, null);
	}
	
	//argId could be listId, categoryId or collectionId
	private void showGridView(List<Object> itemList, ResourceType resType,
			gridType theGridType, viewType theViewType, String argId) {
		
		gridItems = boundGridData(resType, theViewType, argId);
		itemList = gridItems;
		
		switch (theGridType) {
		case GRIDTYPE_RES_TODOWNLOAD:
			// res grid in download page
			ResourceGridAdapter adapter = new ResourceGridAdapter(itemList, false, this);
			gridView.setAdapter(adapter);
			break;
		case GRIDTYPE_RES_INPROGRESS:
			// res grid in reading page
			ResourceGridAdapter adapter2 = new ResourceGridAdapter(itemList, true, this);
			gridView.setAdapter(adapter2);
			break;
		case GRIDTYPE_RESLIST:
			// resource list page
			ResourceListGridAdapter adapter3 = new ResourceListGridAdapter(itemList, this);
			gridView.setAdapter(adapter3);
			break;
		case GRIDTYPE_CATEGORY:
			// category page
			CategoryGridAdapter adapter4 = new CategoryGridAdapter(itemList, this);
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
	
	
	private List<Object> boundGridData(ResourceType resType, viewType theViewType, String argId) {
		
		String[] projection = null;
		String  selection = null;
		
		String s = Books._PROGRESS;
		switch(resType){
		case BOOK:
			switch (theViewType) {
			//books
			case RES_READING:
				// AND progress > 0 AND progress < 100
				//selection = "download_status = 'DOWNLOADED'";
				return resolver.getBooks(projection, selection);
					
			case RES_ALL:
				//selection = "download_status = 'DOWNLOADED'";
				return resolver.getBooks(projection, selection);
				
			case RES_RECENT:
				// AND download_time > ...
				//selection = "download_status = 'DOWNLOADED' ";
				return resolver.getBooks(projection, selection);
			
			case RES_READED:
				//WEN GU ZHI XIN
				// AND readed
				//selection = "download_status = 'DOWNLOADED'";
				return resolver.getBooks(projection, selection);
				
			case COLLECTION:
				//collections
				return resolver.getBooks(projection, "collection_id = '"+argId+"'");
				
			case DOWN_HOT:
				//AND HOT
				//selection = "download_status = 'NOT_DOWNLOADED'";
				return resolver.getBookCollections(projection, selection);
				
			case DOWN_LIKE:
				//AND LIKE
				//selection = "download_status = 'NOT_DOWNLOADED'";
				return resolver.getBookCollections(projection, selection);
			
			case DOWN_CATEGORY_RES:
				//nested
				//AND category_id = categoryId
				//selection = "download_status = 'NOT_DOWNLOADED'";
				return resolver.getBookCollections(projection, selection);
			
			case DOWN_LIST_RES:
				//nested
				//AND list_id = listId
				//selection = "download_status = 'NOT_DOWNLOADED'";
				return resolver.getBookCollections(projection, selection);
				
			case RES_READ_HISTORY:
				//YUE DU LI CHENG
				return null;
				
			case DOWN_RECOMMAND:
				return null;
			case DOWN_CATEGORY:

				return null;
			
			case DOWN_LIST:

				return null;

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
