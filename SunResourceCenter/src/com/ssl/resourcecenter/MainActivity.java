package com.ssl.resourcecenter;

import com.ssl.resourcecenter.util.AutoLoadListener;
import com.ssl.resourcecenter.util.AutoLoadListener.AutoLoadCallBack;
import com.ssl.metadata.provider.MetadataContract.BookCollectionInfo;
import com.ssl.metadata.provider.MetadataContract.BookCollectionTags;
import com.ssl.metadata.provider.MetadataContract.BookCollections;
import com.ssl.metadata.provider.MetadataContract.BookInfo;
import com.ssl.metadata.provider.MetadataContract.Books;
import com.ssl.metadata.provider.MetadataContract.Downloadable;
import com.ssl.resourcecenter.adapter.CategoryGridAdapter;
import com.ssl.resourcecenter.adapter.ResourceListGridAdapter;
import com.ssl.resourcecenter.contentresolver.ResourceContentResolver;
import com.ssl.resourcecenter.enums.GridType;
import com.ssl.resourcecenter.enums.ResourceType;
import com.ssl.resourcecenter.enums.ViewType;
import com.ssl.resourcecenter.model.CategoryGridItem;
import com.ssl.resourcecenter.model.ResourceGridItem;
import com.ssl.resourcecenter.R;
import com.ssl.resourcecenter.adapter.ResourceGridAdapter;

import java.util.List;
import java.util.Stack;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
	private static final int ITEM_PER_PAGE = 6;
	
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
	private RelativeLayout collectionBack, tipNull;
	private Spinner mainnav;
	private EditText searchBar;
	private ResourceContentResolver resolver;
	private TextView collectionTitle;
	private ImageButton btnBack;
	private ToggleButton searchToggleButton;
	private ResourceGridAdapter toDownloadGridAdapter, downloadedGridAdapter;
	private ResourceListGridAdapter listGridAdapter;
	private CategoryGridAdapter categoryGridAdapter;
	
	//private 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
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
				case SEARCH:
					
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
					CategoryGridItem currentItem = (CategoryGridItem) gridItems.get(position);
					collectionTitle.setText(currentItem.getName());
					
					showGridView(currentResType, currentGridType, currentViewType, currentItem.getId());
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
		
		//search method
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
				
				case 0: // hot
					currentGridType = GridType.GRIDTYPE_RES_TODOWNLOAD;
					currentViewType = ViewType.DOWN_HOT;
					break;
				case 1: // category
					currentGridType = GridType.GRIDTYPE_CATEGORY;
					currentViewType = ViewType.DOWN_CATEGORY;
					break;
				case 2: // list
					currentGridType = GridType.GRIDTYPE_RESLIST;
					currentViewType = ViewType.DOWN_LIST;
					break;
				case 3: // recommend
					currentGridType = GridType.GRIDTYPE_RECOMMAND;
					currentViewType = ViewType.DOWN_RECOMMAND;
					break;
				case 4: // like
					currentGridType = GridType.GRIDTYPE_RES_TODOWNLOAD;
					currentViewType = ViewType.DOWN_LIKE;
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
		ArrayAdapter<String> mainNavAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, curs);
		mainNavAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
		
		mainnav.setAdapter(mainNavAdapter);
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
		setContentView(R.layout.main);
		
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
		tipNull = (RelativeLayout) findViewById(R.id.tip_null);
		
		AutoLoadListener autoLoadListener =new AutoLoadListener(callBack);
		gridView.setOnScrollListener(autoLoadListener);
		
		resolver = new ResourceContentResolver(MainActivity.this.getContentResolver(), this.getResources());
		
		formerGridType = new Stack<GridType>();
		formerViewType = new Stack<ViewType>();
		formerSelectedItem = new Stack<Integer>();
		formerArg = new Stack<String>();
		formerTitle = new Stack<CharSequence>();
		
	}
	
	//new page
	AutoLoadCallBack callBack= new AutoLoadCallBack(){

		public void execute() {
			if(null == gridItems || 0 == gridItems.size()) return;
			List<Object> itemList;
			//Toast.makeText(MainActivity.this, "new page",500).show();
			
			switch (currentGridType) {
			case GRIDTYPE_RES_TODOWNLOAD:
				// res grid in download page
				itemList = boundGridData(currentResType, currentViewType, currentArg, ITEM_PER_PAGE, gridItems.size());
				gridItems.addAll(itemList);
				toDownloadGridAdapter.notifyDataSetChanged();
				break;
			case GRIDTYPE_RES_INPROGRESS:
				// res grid in reading page
				itemList = boundGridData(currentResType, currentViewType, currentArg, ITEM_PER_PAGE, gridItems.size());
				gridItems.addAll(itemList);
				downloadedGridAdapter.notifyDataSetChanged();
				break;
			case GRIDTYPE_RESLIST:
			case GRIDTYPE_CATEGORY:
			case GRIDTYPE_RECOMMAND:
				break;
			default:
				break;
			}
		}
		
	};
	
	@Override
	protected void onResume() {
		super.onResume();
		//update grid view
		showGridView(currentResType, currentGridType, currentViewType, currentArg);
//		switch (currentGridType) {
//		case GRIDTYPE_RES_TODOWNLOAD:
//			// res grid in download page
//			toDownloadGridAdapter.notifyDataSetChanged();
//			break;
//		case GRIDTYPE_RES_INPROGRESS:
//			// res grid in reading page
//			downloadedGridAdapter.notifyDataSetChanged();
//			break;
//		case GRIDTYPE_RESLIST:
//			break;
//		case GRIDTYPE_CATEGORY:
//			categoryGridAdapter.notifyDataSetChanged();
//			break;
//		case GRIDTYPE_RECOMMAND:
//			break;
//		default:
//			break;
//		}
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
	
	private void searchAllGrid(String key){
		
		showGridView(currentResType, currentGridType, currentViewType, key);
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
		
		if(gridItems == null || gridItems.size() == 0){ 
			tipNull.setVisibility(View.VISIBLE); 
		}else{
			tipNull.setVisibility(View.INVISIBLE); 
		}
		
		switch (theGridType) {
		case GRIDTYPE_RES_TODOWNLOAD:
			// res grid in download page
			toDownloadGridAdapter = new ResourceGridAdapter(gridItems, false, this);
			
			gridView.setAdapter(toDownloadGridAdapter);
			break;
		case GRIDTYPE_RES_INPROGRESS:
			// res grid in reading page
			downloadedGridAdapter = new ResourceGridAdapter(gridItems, true, this);
			gridView.setAdapter(downloadedGridAdapter);
			break;
		case GRIDTYPE_RESLIST:
			// resource list page
			listGridAdapter = new ResourceListGridAdapter(gridItems, this);
			gridView.setAdapter(listGridAdapter);
			break;
		case GRIDTYPE_CATEGORY:
			// category page
			categoryGridAdapter = new CategoryGridAdapter(gridItems, this);
			gridView.setAdapter(categoryGridAdapter);
			break;
		case GRIDTYPE_RECOMMAND:
			gridView.setVisibility(View.INVISIBLE);
			recommandView.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}
	
	private List<Object> boundGridData(ResourceType resType, ViewType theViewType, String arg){
		return boundGridData(resType, theViewType, arg, ITEM_PER_PAGE, 0);
	}
	
	private List<Object> boundGridData(ResourceType resType, ViewType theViewType, String arg, int itemCount, int offset) {
		
		String[] projection = null;
		String selection = null;
		
		switch(resType){
		case BOOK:
			switch (theViewType) {
			//books
			case RES_READING:
				// AND progress > 0 AND progress < 100
				selection = String.format("%s = '%s' AND %s not null ", BookInfo._DOWNLOAD_STATUS, Downloadable.STATUS_DOWNLOADED,
						Books._STARTTIME);
				return resolver.getBooks(projection, selection, itemCount, offset);
					
			case RES_ALL:
				selection = String.format("%s = '%s'", BookInfo._DOWNLOAD_STATUS, Downloadable.STATUS_DOWNLOADED);
				return resolver.getBooks(projection, selection, itemCount, offset);
			case RES_RECENT:
				// Date format: YYYY-MM-DD hh:mm:ss
//				int inDays = 15; 
//				selection = String.format("%s = '%s' AND %s > datetime('now', '-%d days', 'localtime')", BookInfo._DOWNLOAD_STATUS, 
//						Downloadable.STATUS_DOWNLOADED, BookInfo._DOWNLOAD_TIME, inDays);
				selection = String.format("%s = '%s'", BookInfo._DOWNLOAD_STATUS, Downloadable.STATUS_DOWNLOADED);
				return resolver.getBooks(projection, selection, itemCount, offset);
			
			case RES_READED:
				//WEN GU ZHI XIN
				selection = String.format("%s = '%s' AND %s >= 98 ", BookInfo._DOWNLOAD_STATUS, Downloadable.STATUS_DOWNLOADED, 
						BookInfo._PROGRESS);
				return resolver.getBooks(projection, selection, itemCount, offset);
			
			case COLLECTION:
				//collections
				selection = Books._COLLECTION_ID + " = '"+arg+"'";
				return resolver.getBooks(projection, selection, itemCount, offset);
				
			case DOWN_CATEGORY:
				
				return resolver.getBookCategory();
			
				
			case DOWN_HOT:
				//AND HOT
				return resolver.getBookCollections(projection, selection, itemCount, offset);
				
			case DOWN_LIKE:
				//TODO:
				//AND LIKE !!!
				return resolver.getBookCollections(projection, selection, itemCount, offset);
			
			case DOWN_CATEGORY_RES:
				
				return resolver.getBookCollectionsWithTags(projection, arg, itemCount, offset);
			
			case DOWN_LIST_RES:
				//TODO:
				return resolver.getBookCollections(projection, selection, itemCount, offset);
				
			case RES_READ_HISTORY:
				//YUE DU LI CHENG
				return null;
				
			case DOWN_RECOMMAND:
				return null;
			
			case DOWN_LIST:

				return null;
			
			case SEARCH:
				selection = BookCollections._TITLE + " like '%" + arg + "%' ";
				return resolver.getBookCollections(projection, selection, itemCount, offset);

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
