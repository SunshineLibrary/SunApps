package com.sunshine.sunresourcecenter;

import com.sunshine.metadata.provider.*;
import com.sunshine.metadata.provider.MetadataContract.BookCollections;
import com.sunshine.metadata.provider.MetadataContract.Books;
import com.sunshine.sunresourcecenter.R;
import com.sunshine.sunresourcecenter.adapter.CategoryGridAdapter;
import com.sunshine.sunresourcecenter.adapter.ResourceGridAdapter;
import com.sunshine.sunresourcecenter.adapter.ResourceListGridAdapter;
import com.sunshine.sunresourcecenter.contentresolver.ResourceContentResolver;
import com.sunshine.sunresourcecenter.model.ResourceGridItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {

	interface TabSwitched {
		public void OnTabSwitched(int index);
	}
	
	private enum GridType{
		GRIDTYPE_RES_TODOWNLOAD,
		GRIDTYPE_RES_INPROGRESS, 
		GRIDTYPE_RESLIST,
		GRIDTYPE_CATEGORY,
		GRIDTYPE_RECOMMAND
	};
	
	private enum ViewType {
		RES_READING,
		RES_ALL,
		RES_RECENT,
		RES_READED,
		RES_READ_HISTORY,
		DOWN_RECOMMAND, 
		DOWN_HOT, //show collections hot
		DOWN_CATEGORY, //show cates
		DOWN_LIKE, //show collections like
		DOWN_LIST, //show lists
		DOWN_LIST_RES, //show collections in list
		DOWN_CATEGORY_RES, //show collections in cate
		COLLECTION, //show res in collection
		SEARCH, //search result
	};
	private GridType currentGridType;
	private ViewType currentViewType;
	private String currentArg;
	private Stack<GridType> formerGridType;
	private Stack<ViewType> formerViewType;
	private Stack<Integer> formerSelectedItem;
	private Stack<String> formerArg;
	private Stack<CharSequence> formerTitle;
	private ResourceType currentResType;
	private List<Object> gridItems;
	private GridView gridView;
	private LinearLayout resnav;
	private LinearLayout downnav;
	private LinearLayout typenav;
	private LinearLayout recommandView;
	private RelativeLayout collectionBack;
	private Spinner mainnav;
	private EditText searchBar;
	private ResourceContentResolver resolver;
	private TextView collectionTitle;
	private ImageButton btnBack;
	private ToggleButton searchToggleButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		init();
		
		
		// change via tab state
		currentGridType = GridType.GRIDTYPE_RES_INPROGRESS;
		currentViewType = ViewType.RES_READING;
		currentResType = ResourceType.BOOK;
		recommandView.setVisibility(View.INVISIBLE);
		
		// bound data to grids
		//gridItems = boundGridData(currentResType, currentGridType);

		// show gridView of different types
		showGridView(currentResType, currentGridType, currentViewType);

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
				case COLLECTION:
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
						//show res in a collection
						pushGridViewState(currentGridType, currentViewType, gridView.getSelectedItemPosition(), currentArg==null?"":currentArg, collectionTitle.getText());
						
						currentGridType = GridType.GRIDTYPE_RES_TODOWNLOAD;
						currentViewType = ViewType.COLLECTION;
						downnav.setVisibility(View.INVISIBLE);
						typenav.setVisibility(View.INVISIBLE);
						collectionBack.setVisibility(View.VISIBLE);
						collectionTitle.setText(item.getTitle());
						showGridView(currentResType, currentGridType, currentViewType, gridItems.get(position).toString());
					}else{
						intent = new Intent();
						intent.setClass(MainActivity.this, ResourceInfoActivity.class);
						intent.putExtra("bookId", resolver.getSingleResIdOfCollection(gridItems.get(position).toString()));
						intent.putExtra("type", currentResType);
						startActivity(intent);
					}
					
					break;
					
				case DOWN_CATEGORY:
					pushGridViewState(currentGridType, currentViewType, gridView.getSelectedItemPosition(), currentArg==null?"":currentArg, collectionTitle.getText());
					
					currentGridType = GridType.GRIDTYPE_RES_TODOWNLOAD;
					currentViewType = ViewType.DOWN_CATEGORY_RES;
					
					downnav.setVisibility(View.INVISIBLE);
					typenav.setVisibility(View.INVISIBLE);
					collectionBack.setVisibility(View.VISIBLE);
					collectionTitle.setText(gridItems.get(position).toString());
					
					showGridView(currentResType, currentGridType, currentViewType, gridItems.get(position).toString());
					break;
					
				case DOWN_LIST:
					currentGridType = GridType.GRIDTYPE_RES_TODOWNLOAD;
					currentViewType = ViewType.DOWN_LIST_RES;
					showGridView(currentResType, currentGridType, currentViewType,gridItems.get(position).toString());
					break;
				default:
					break;
				}
				
//				Toast.makeText(MainActivity.this, gridItems.get(position).toString(),
//						Toast.LENGTH_SHORT).show();

			}
		});
		
		
		//back from resource in a collection
		btnBack.setOnClickListener(new OnClickListener(){
        	
			@Override
			public void onClick(View v) { 
				if(formerGridType.size()>0 && formerViewType.size()>0&&formerArg.size()>0&&formerTitle.size()>0&&formerSelectedItem.size()>0){
					currentGridType = formerGridType.pop();
					currentViewType = formerViewType.pop();
					
					showGridView(currentResType, currentGridType, currentViewType, formerArg.pop());
					collectionTitle.setText(formerTitle.pop());
					gridView.setSelection(formerSelectedItem.pop());
				}
				if(formerArg.size()==0){
					downnav.setVisibility(View.VISIBLE);
					typenav.setVisibility(View.VISIBLE);
					collectionBack.setVisibility(View.INVISIBLE);
				}
			}
        	
        });

		searchBar.setOnKeyListener(new OnKeyListener(){

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER){
					//searchCurrentGrid(searchBar.getText().toString(),currentResType,currentGridType,currentViewType);
					currentViewType = ViewType.SEARCH;
					currentGridType = GridType.GRIDTYPE_RES_TODOWNLOAD;
					searchToggleButton.setVisibility(View.VISIBLE);
					searchToggleButton.setChecked(true);
					searchAllGrid(searchBar.getText().toString());
					
					InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);   
					if(imm.isActive()){  
						imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0 );  
					}
				
					//Toast.makeText(MainActivity.this, searchBar.getText().toString(),Toast.LENGTH_SHORT).show();
					
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
					currentGridType = GridType.GRIDTYPE_RES_INPROGRESS;
					currentViewType = ViewType.RES_READING;
					break;
				case 1:// all
					currentGridType = GridType.GRIDTYPE_RES_INPROGRESS;
					currentViewType = ViewType.RES_ALL;
					break;
				case 2:// recent_downloaded
					currentGridType = GridType.GRIDTYPE_RES_INPROGRESS;
					currentViewType = ViewType.RES_RECENT;
					break;
				case 3:// learn_new
					currentGridType = GridType.GRIDTYPE_RES_INPROGRESS;
					currentViewType = ViewType.RES_READED;
					break;
				case 4:// readed
					currentGridType = GridType.GRIDTYPE_RES_INPROGRESS;
					currentViewType = ViewType.RES_READ_HISTORY;
					break;
				}
				gridView.setVisibility(View.VISIBLE);
				recommandView.setVisibility(View.INVISIBLE);
				showGridView(currentResType, currentGridType, currentViewType);

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
				showGridView(currentResType, currentGridType, currentViewType);
			}
		};
		
		final TabSwitched downnav_switch = new TabSwitched() {

			@Override
			public void OnTabSwitched(int index) {
				
				gridView.setVisibility(View.INVISIBLE);
				//search
				if(index != 5){
					searchToggleButton.setVisibility(View.INVISIBLE);
					searchBar.setText(null);
				}
				
				switch (index) {
				case 0: // recommend
					currentGridType = GridType.GRIDTYPE_RECOMMAND;
					currentViewType = ViewType.DOWN_RECOMMAND;
					break;
				case 1: // hot
					currentGridType = GridType.GRIDTYPE_RES_TODOWNLOAD;
					currentViewType = ViewType.DOWN_HOT;
					break;
				case 2: // category
					currentGridType = GridType.GRIDTYPE_CATEGORY;
					currentViewType = ViewType.DOWN_CATEGORY;
					break;
				case 3: // like
					currentGridType = GridType.GRIDTYPE_RES_TODOWNLOAD;
					currentViewType = ViewType.DOWN_LIKE;
					break;
				case 4: // list
					currentGridType = GridType.GRIDTYPE_RESLIST;
					currentViewType = ViewType.DOWN_LIST;
					break;
				default:
					break;
				}
				gridView.setVisibility(View.VISIBLE);
				recommandView.setVisibility(View.INVISIBLE);
				showGridView(currentResType, currentGridType, currentViewType);
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
				
				clearGridViewState();
				
				if (position == 0) {
					resnav.setVisibility(View.VISIBLE);
					downnav.setVisibility(View.INVISIBLE);
					collectionBack.setVisibility(View.INVISIBLE);
					searchBar.setVisibility(View.INVISIBLE);
					resnav_switch.OnTabSwitched(getSimulatedTabSelected(resnav));
				} else {
					resnav.setVisibility(View.INVISIBLE);
					downnav.setVisibility(View.VISIBLE);
					collectionBack.setVisibility(View.INVISIBLE);
					searchBar.setVisibility(View.VISIBLE);
					downnav_switch.OnTabSwitched(getSimulatedTabSelected(downnav));
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	private void init(){
		
		gridView = (GridView) findViewById(R.id.gridview);
		resnav = (LinearLayout) findViewById(R.id.resnav);
		downnav = (LinearLayout) findViewById(R.id.downnav);
		typenav = (LinearLayout) findViewById(R.id.typenav);
		mainnav = (Spinner) findViewById(R.id.mainnav);
		collectionBack = (RelativeLayout) findViewById(R.id.collection_back);
		recommandView = (LinearLayout) findViewById(R.id.recommand_view);
		collectionTitle = (TextView) findViewById(R.id.title_collction);
		btnBack = (ImageButton) findViewById(R.id.btn_collection_back);
		searchBar = (EditText) findViewById(R.id.searchbar);
		searchToggleButton = (ToggleButton) findViewById(R.id.downnav_search);
		
		resolver = new ResourceContentResolver(MainActivity.this.getContentResolver(), this.getResources());
		
		formerGridType = new Stack<GridType>();
		formerViewType = new Stack<ViewType>();
		formerSelectedItem = new Stack<Integer>();
		formerArg = new Stack<String>();
		formerTitle = new Stack<CharSequence>();
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
	
	/**
	 * searchbar function
	 * search the current gridItems ,see if it contains the input keywords
	 * @param key
	 * @param resType
	 * @param theGridType
	 * @param theViewType
	 */
	private void searchCurrentGrid(String key,ResourceType resType, GridType theGridType, ViewType theViewType){
		List<Object> curList = gridItems;
		List<Object> itemList = new ArrayList<Object>();
		for (Object object : curList) {
			ResourceGridItem resourceGridItem = (ResourceGridItem) object;
			if(resourceGridItem.getTitle().contains(key))
				itemList.add(object);
		}
		gridItems = itemList;
		
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
	
	/**
	 * search the DB to return res(books) related to the input keywords
	 * @param key
	 * @param resType
	 * @param theGridType
	 * @param theViewType
	 */
	private void searchAllGrid(String key){
		//		??just show res grid in reading page?
		//no. in search page now.
		
		//String[] projection = null;
		//String  selection = "title like '%" + key +"%'";
		//List<Object> itemList = setGridData(key, resType, theGridType, theViewType, projection, selection, null);
		
		//ResourceGridAdapter adapter = new ResourceGridAdapter(itemList, true, this);
		//gridView.setAdapter(adapter);
		
		showGridView(currentResType, currentGridType, currentViewType, key);
	}
	
	private List<Object> setGridData(String key,ResourceType resType, GridType theGridType, ViewType theViewType,
			String[] projection,String  selection,String argId){
		
		switch(resType){
		case BOOK:
			return resolver.getBookCollections(projection, selection);
			
		case AUDIO:
			
			return null;
		case VEDIO:
			
			return null;
			
		default: 
			return null;
		}
	}
	
	private void pushGridViewState(GridType theGridType, ViewType theViewType, int selectedItem, String arg, CharSequence title){
		
		formerGridType.push(theGridType);
		formerViewType.push(theViewType);
		formerSelectedItem.push(selectedItem);
		formerArg.push(arg);
		formerTitle.push(title);
		
	}
	
	private void clearGridViewState(){
		formerArg.clear();
		formerTitle.clear();
		formerGridType.clear();
		formerViewType.clear();
		formerSelectedItem.clear();
	}
	
	private void showGridView(ResourceType resType, GridType theGridType, ViewType theViewType){
		showGridView(resType, theGridType, theViewType, null);
	}
	
	//argId could be listId, categoryId or collectionId
	private void showGridView(ResourceType resType,
			GridType theGridType, ViewType theViewType, String arg) {
		
		currentArg=arg;
		
		gridItems = boundGridData(resType, theViewType, arg);
		List<Object> itemList = gridItems;
		
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
	
	private List<Object> boundGridData(ResourceType resType, ViewType theViewType, String arg) {
		
		String[] projection = null;
		String  selection = null;
		
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
				return resolver.getBooks(projection, Books._COLLECTION_ID + " = '"+arg+"'");
				
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
				return resolver.getBookCollections(projection, " tags like '%" + arg + "%' ");
			
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
				
				return resolver.getBookCategory();
			case DOWN_LIST:

				return null;
			
			case SEARCH:
				
				return resolver.getBookCollections(projection, " title like '%" + arg + "%' ");

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

}
