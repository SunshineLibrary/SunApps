/*
 * Copyright (C) 2009-2012 Geometer Plus <contact@geometerplus.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package org.geometerplus.android.fbreader;

import java.io.File;
import java.util.*;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.*;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.view.LayoutInflater.Factory;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.geometerplus.zlibrary.core.application.ZLApplication;
import org.geometerplus.zlibrary.core.filesystem.ZLFile;
import org.geometerplus.zlibrary.core.library.ZLibrary;
import org.geometerplus.zlibrary.core.options.ZLIntegerRangeOption;

import org.geometerplus.zlibrary.text.view.ZLTextView;
import org.geometerplus.zlibrary.text.hyphenation.ZLTextHyphenator;

import org.sunshinelibrary.sunreader.R;
import org.geometerplus.zlibrary.ui.android.application.ZLAndroidApplicationWindow;
import org.geometerplus.zlibrary.ui.android.library.*;
import org.geometerplus.zlibrary.ui.android.view.AndroidFontUtil;

import org.geometerplus.fbreader.fbreader.ActionCode;
import org.geometerplus.fbreader.fbreader.FBReaderApp;
import org.geometerplus.fbreader.bookmodel.BookModel;
import org.geometerplus.fbreader.library.Book;
import org.geometerplus.fbreader.tips.TipsManager;

import org.geometerplus.android.fbreader.library.SQLiteBooksDatabase;
import org.geometerplus.android.fbreader.library.KillerCallback;
import org.geometerplus.android.fbreader.api.*;
import org.geometerplus.android.fbreader.tips.TipsActivity;

import org.geometerplus.android.util.UIUtil;

//import com.sunreader.data.NewWordEntity;
//import com.sunreader.sun.NewWord;
import org.geometerplus.android.util.Utils;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.actionbarsherlock.view.MenuInflater;
import com.ssl.metadata.provider.MetadataContract.Books;
import com.ssl.metadata.provider.MetadataContract.Files;

public final class FBReader extends ZLAndroidActivity {
	public static final String BOOK_PATH_KEY = "BookPath";
	public static final String BOOK_TITLE_KEY = "bookTitle";
	public static final int REQUEST_PREFERENCES = 1;
	public static final int REQUEST_BOOK_INFO = 2;
	public static final int REQUEST_CANCEL_MENU = 3;

	public static final int RESULT_DO_NOTHING = RESULT_FIRST_USER;
	public static final int RESULT_REPAINT = RESULT_FIRST_USER + 1;
	public static final int RESULT_RELOAD_BOOK = RESULT_FIRST_USER + 2;

	private static final String DBFILE = "xiandaihanyucidian.db";
	private int myFullScreenFlag;
	public ActionBar bar;
	private FBReaderApp fbReader;
	
	private int bookId = -1;

	private static final String PLUGIN_ACTION_PREFIX = "___";
	private final List<PluginApi.ActionInfo> myPluginActions =
		new LinkedList<PluginApi.ActionInfo>();
	private final BroadcastReceiver myPluginInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final ArrayList<PluginApi.ActionInfo> actions = getResultExtras(true).<PluginApi.ActionInfo>getParcelableArrayList(PluginApi.PluginInfo.KEY);
			if (actions != null) {
				synchronized (myPluginActions) {
					final FBReaderApp fbReader = (FBReaderApp)FBReaderApp.Instance();
					int index = 0;
					while (index < myPluginActions.size()) {
						fbReader.removeAction(PLUGIN_ACTION_PREFIX + index++);
					}
					myPluginActions.addAll(actions);
					index = 0;
					for (PluginApi.ActionInfo info : myPluginActions) {
						fbReader.addAction(
							PLUGIN_ACTION_PREFIX + index++,
							new RunPluginAction(FBReader.this, fbReader, info.getId())
						);
					}
				}
			}
		}
	};

	@Override
	protected ZLFile fileFromIntent(Intent intent) {
		int id = intent.getIntExtra("bookId",-1);
		String title = intent.getStringExtra(BOOK_TITLE_KEY);
		bar.setTitle(title);
		this.bookId = id;
		
		if(id!=-1){			
//			Log.i("Open book", "id:"+id);
//			Log.i("Open book", Books.getBookUri(id).getEncodedPath());
//			/mnt/sdcard/Books/8
			ContentResolver c = getContentResolver();
			String path = Books.getBookUri(id).getPath();
			Cursor cur = null;
			try{
				cur = c.query(Files.CONTENT_URI, null, Files._URI_PATH +"=\""+path+"\"", null, null);
				if(cur.moveToFirst()){
					return ZLFile.createFileByPath(cur.getString(cur.getColumnIndex(Files._FILE_PATH)));
				}else{
					return null;
				}
			}finally{
				if(cur!=null)cur.close();
			}
			
		}
		String filePath = intent.getStringExtra(BOOK_PATH_KEY);
		
		
		if (filePath == null) {
			final Uri data = intent.getData();
			if (data != null) {
				filePath = data.getPath();
			}
		}
		return filePath != null ? ZLFile.createFileByPath(filePath) : null;
	}


	@Override
	protected Runnable getPostponedInitAction() {
		return new Runnable() {
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
//						new TipRunner().start();
						DictionaryUtil.init(FBReader.this);
					}
				});
			}
		};
	}

	@Override
	public void onCreate(Bundle icicle) {
		
		super.onCreate(icicle);
		fbReader = (FBReaderApp)FBReaderApp.Instance();
		final ZLAndroidLibrary zlibrary = (ZLAndroidLibrary)ZLibrary.Instance();
		myFullScreenFlag =
				zlibrary.ShowStatusBarOption.getValue() ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
			getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN, myFullScreenFlag
			);
			
		bar = getSupportActionBar();
		
		bar.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_background));
		bar.setLogo(R.drawable.back_style);
		
		bar.setDisplayUseLogoEnabled(true);
		bar.setDisplayHomeAsUpEnabled(true);
		bar.setHomeButtonEnabled(true);
		bar.setDisplayShowTitleEnabled(true);
		bar.setTitle(null);		
		bar.hide();
		
	
		
//		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN, WindowManager.LayoutParams. FLAG_FULLSCREEN);  
		if (fbReader.getPopupById(TextSearchPopup.ID) == null) {
			new TextSearchPopup(fbReader);
		}
		if (fbReader.getPopupById(NavigationPopup.ID) == null) {
			new NavigationPopup(fbReader);
		}
		if (fbReader.getPopupById(SelectionPopup.ID) == null) {
			new SelectionPopup(fbReader);
		}

		fbReader.addAction(ActionCode.SHOW_LIBRARY, new ShowLibraryAction(this, fbReader));
		fbReader.addAction(ActionCode.SHOW_PREFERENCES, new ShowPreferencesAction(this, fbReader));
		fbReader.addAction(ActionCode.SHOW_BOOK_INFO, new ShowBookInfoAction(this, fbReader));
		fbReader.addAction(ActionCode.SHOW_TOC, new ShowTOCAction(this, fbReader));
		fbReader.addAction(ActionCode.SHOW_BOOKMARKS, new ShowBookmarksAction(this, fbReader));
		fbReader.addAction(ActionCode.ADD_BOOKMARKS, new AddBookmarksAction(this, fbReader));
		fbReader.addAction(ActionCode.SHOW_NETWORK_LIBRARY, new ShowNetworkLibraryAction(this, fbReader));
		
		fbReader.addAction(ActionCode.SHOW_MENU, new ShowMenuAction(this, fbReader));
		fbReader.addAction(ActionCode.SHOW_NAVIGATION, new ShowNavigationAction(this, fbReader));
		fbReader.addAction(ActionCode.SEARCH, new SearchAction(this, fbReader));
		fbReader.addAction(ActionCode.SHARE_BOOK, new ShareBookAction(this, fbReader));

		fbReader.addAction(ActionCode.SELECTION_SHOW_PANEL, new SelectionShowPanelAction(this, fbReader));
		fbReader.addAction(ActionCode.SELECTION_HIDE_PANEL, new SelectionHidePanelAction(this, fbReader));
		fbReader.addAction(ActionCode.SELECTION_COPY_TO_CLIPBOARD, new SelectionCopyAction(this, fbReader));
		fbReader.addAction(ActionCode.SELECTION_SHARE, new SelectionShareAction(this, fbReader));
		fbReader.addAction(ActionCode.SELECTION_TRANSLATE, new SelectionTranslateAction(this, fbReader));
		fbReader.addAction(ActionCode.SELECTION_BOOKMARK, new SelectionBookmarkAction(this, fbReader));

		fbReader.addAction(ActionCode.PROCESS_HYPERLINK, new ProcessHyperlinkAction(this, fbReader));

		fbReader.addAction(ActionCode.SHOW_CANCEL_MENU, new ShowCancelMenuAction(this, fbReader));

		fbReader.addAction(ActionCode.SET_SCREEN_ORIENTATION_SYSTEM, new SetScreenOrientationAction(this, fbReader, ZLibrary.SCREEN_ORIENTATION_SYSTEM));
		fbReader.addAction(ActionCode.SET_SCREEN_ORIENTATION_SENSOR, new SetScreenOrientationAction(this, fbReader, ZLibrary.SCREEN_ORIENTATION_SENSOR));
		fbReader.addAction(ActionCode.SET_SCREEN_ORIENTATION_PORTRAIT, new SetScreenOrientationAction(this, fbReader, ZLibrary.SCREEN_ORIENTATION_PORTRAIT));
		fbReader.addAction(ActionCode.SET_SCREEN_ORIENTATION_LANDSCAPE, new SetScreenOrientationAction(this, fbReader, ZLibrary.SCREEN_ORIENTATION_LANDSCAPE));
		fbReader.addAction(ActionCode.SHOW_ACTIONBAR, new ShowActionBarAction(this,fbReader));
		
		if (ZLibrary.Instance().supportsAllOrientations()) {
			fbReader.addAction(ActionCode.SET_SCREEN_ORIENTATION_REVERSE_PORTRAIT, new SetScreenOrientationAction(this, fbReader, ZLibrary.SCREEN_ORIENTATION_REVERSE_PORTRAIT));
			fbReader.addAction(ActionCode.SET_SCREEN_ORIENTATION_REVERSE_LANDSCAPE, new SetScreenOrientationAction(this, fbReader, ZLibrary.SCREEN_ORIENTATION_REVERSE_LANDSCAPE));
		}
		
		CopyAndLoadDB(); 
	}
	
	 private void CopyAndLoadDB() {
	        // 第一次运行应用程序时，加载数据库到data/data/当前包的名称/database/<db_name>
	        File dir = new File("data/data/" + getPackageName() + "/databases");
	        if (!dir.exists() || !dir.isDirectory()) {
	            dir.mkdir();
	        }
	        File file = new File(dir, DBFILE);
	        if (!file.exists()) {
	            FileUtils.loadDbFile(R.raw.xiandaihanyucidian, file,
	                    getResources(), getPackageName());
	            Log.d("WineStock", "DataBase Load Successfully");

	        }
	    }

 	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		final ZLAndroidLibrary zlibrary = (ZLAndroidLibrary)ZLibrary.Instance();
//		if (!zlibrary.isKindleFire() && !zlibrary.ShowStatusBarOption.getValue()) {
//			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		super.onOptionsMenuClosed(menu);
		final ZLAndroidLibrary zlibrary = (ZLAndroidLibrary)ZLibrary.Instance();
//		if (!zlibrary.isKindleFire() && !zlibrary.ShowStatusBarOption.getValue()) {
//			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home){
			this.finish();
		}else if(item.getItemId() == R.id.addBookmark){
			FBReaderApp fbreader = (FBReaderApp)FBReaderApp.Instance();
			fbreader.updateAllBookmarksList(fbreader.addBookmark(20, true));	
			LayoutInflater inflater = getLayoutInflater();
			View layout = inflater.inflate(R.layout.toast_layout,
			                               (ViewGroup) findViewById(R.id.toast_layout_root));

			TextView text = (TextView) layout.findViewById(R.id.toast_text);
			text.setText(R.string.add_bookmark_success);
			Toast toast = new Toast(getApplicationContext());
			toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.BOTTOM, 0, 0);
			toast.setDuration(Toast.LENGTH_LONG);
			toast.setView(layout);
			toast.show();
			return true;
		}
		final ZLAndroidLibrary zlibrary = (ZLAndroidLibrary)ZLibrary.Instance();
		if (!zlibrary.isKindleFire() && !zlibrary.ShowStatusBarOption.getValue()) {
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		final Uri data = intent.getData();
		final FBReaderApp fbReader = (FBReaderApp)FBReaderApp.Instance();
		if ((intent.getFlags() & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) != 0) {
			super.onNewIntent(intent);
		} else if (Intent.ACTION_VIEW.equals(intent.getAction())
					&& data != null && "fbreader-action".equals(data.getScheme())) {
			fbReader.runAction(data.getEncodedSchemeSpecificPart(), data.getFragment());
		} else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			final String pattern = intent.getStringExtra(SearchManager.QUERY);
			final Runnable runnable = new Runnable() {
				public void run() {
					final TextSearchPopup popup = (TextSearchPopup)fbReader.getPopupById(TextSearchPopup.ID);
					popup.initPosition();
					fbReader.TextSearchPatternOption.setValue(pattern);
					if (fbReader.getTextView().search(pattern, true, false, false, false) != 0) {
						runOnUiThread(new Runnable() {
							public void run() {
								fbReader.showPopup(popup.getId());
							}
						});
					} else {
						runOnUiThread(new Runnable() {
							public void run() {
								UIUtil.showErrorMessage(FBReader.this, "textNotFound");
								popup.StartPosition = null;
							}
						});
					}
				}
			};
			UIUtil.wait("search", runnable, this);
		} else if(ActionCode.SHOW_ACTIONBAR.equals(intent.getAction())){
			 if(bar.isShowing()){
				 bar.hide();
			 }else{
				 bar.show();
			 }
		}else{
			super.onNewIntent(intent);
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		initPluginActions();

		final ZLAndroidLibrary zlibrary = (ZLAndroidLibrary)ZLibrary.Instance();

		final int fullScreenFlag =
			zlibrary.ShowStatusBarOption.getValue() ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
		if (fullScreenFlag != myFullScreenFlag) {
			finish();
			startActivity(new Intent(this, getClass()));
		}

		SetScreenOrientationAction.setOrientation(this, zlibrary.OrientationOption.getValue());

		final FBReaderApp fbReader = (FBReaderApp)FBReaderApp.Instance();
		final RelativeLayout root = (RelativeLayout)findViewById(R.id.root_view);
		((PopupPanel)fbReader.getPopupById(TextSearchPopup.ID)).setPanelInfo(this, root);
		((PopupPanel)fbReader.getPopupById(NavigationPopup.ID)).setPanelInfo(this, root);
		((PopupPanel)fbReader.getPopupById(SelectionPopup.ID)).setPanelInfo(this, root);
	}

	private void initPluginActions() {
		final FBReaderApp fbReader = (FBReaderApp)FBReaderApp.Instance();
		synchronized (myPluginActions) {
			int index = 0;
			while (index < myPluginActions.size()) {
				fbReader.removeAction(PLUGIN_ACTION_PREFIX + index++);
			}
			myPluginActions.clear();
		}

		sendOrderedBroadcast(
			new Intent(PluginApi.ACTION_REGISTER),
			null,
			myPluginInfoReceiver,
			null,
			RESULT_OK,
			null,
			null
		);
	}

	private class TipRunner extends Thread {
		TipRunner() {
			setPriority(MIN_PRIORITY);
		}

		public void run() {
			final TipsManager manager = TipsManager.Instance();
			switch (manager.requiredAction()) {
				case Initialize:
					startActivity(new Intent(
						TipsActivity.INITIALIZE_ACTION, null, FBReader.this, TipsActivity.class
					));
					break;
				case Show:
					startActivity(new Intent(
						TipsActivity.SHOW_TIP_ACTION, null, FBReader.this, TipsActivity.class
					));
					break;
				case Download:
					manager.startDownloading();
					break;
				case None:
					break;
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		try {
			sendBroadcast(new Intent(getApplicationContext(), KillerCallback.class));
		} catch (Throwable t) {
		}
		PopupPanel.restoreVisibilities(FBReaderApp.Instance());
		ApiServerImplementation.sendEvent(this, ApiListener.EVENT_READ_MODE_OPENED);
	}

	@Override
	public void onStop() {
		Log.i("test", "onstop:"+bookId);
		ApiServerImplementation.sendEvent(this, ApiListener.EVENT_READ_MODE_CLOSED);
		int progress = (int)(100.0 * ((float)fbReader.getTextView().pagePosition().Current/ (float)fbReader.getTextView().pagePosition().Total));
//		Toast.makeText(getApplicationContext(),"progress: "+progress,Toast.LENGTH_LONG).show();
		if(bookId != -1){
			Log.i("test", "progress:" + progress);
			ContentResolver cr = getContentResolver();
			ContentValues cv = new ContentValues();
			cv.put(Books._PROGRESS, progress);	
			StringBuffer selection = new StringBuffer();
			selection.append(Books._ID).append(" = ").append(bookId).append(" AND (")
				.append( Books._PROGRESS).append(" < ").append(progress).append(" OR ")
				.append(Books._PROGRESS).append(" is null)");
			cr.update(Books.CONTENT_URI, cv, selection.toString(), null);
		}
		PopupPanel.removeAllWindows(FBReaderApp.Instance(), this);
		super.onStop();
	}

	@Override
	protected FBReaderApp createApplication() {
		if (SQLiteBooksDatabase.Instance() == null) {
			new SQLiteBooksDatabase(this, "READER");
		}
		return new FBReaderApp();
	}

	@Override
	public boolean onSearchRequested() {
		final FBReaderApp fbreader = (FBReaderApp)FBReaderApp.Instance();
		final FBReaderApp.PopupPanel popup = fbreader.getActivePopup();
		fbreader.hideActivePopup();
		final SearchManager manager = (SearchManager)getSystemService(SEARCH_SERVICE);
		manager.setOnCancelListener(new SearchManager.OnCancelListener() {
			public void onCancel() {
				if (popup != null) {
					fbreader.showPopup(popup.getId());
				}
				manager.setOnCancelListener(null);
			}
		});
		startSearch(fbreader.TextSearchPatternOption.getValue(), true, null, false);
		return true;
	}

	public void showSelectionPanel() {
		final FBReaderApp fbReader = (FBReaderApp)FBReaderApp.Instance();
		final ZLTextView view = fbReader.getTextView();
		((SelectionPopup)fbReader.getPopupById(SelectionPopup.ID))
			.move(view.getSelectionStartY(), view.getSelectionEndY());
		fbReader.showPopup(SelectionPopup.ID);
	}

	public void hideSelectionPanel() {
		final FBReaderApp fbReader = (FBReaderApp)FBReaderApp.Instance();
		final FBReaderApp.PopupPanel popup = fbReader.getActivePopup();
		if (popup != null && popup.getId() == SelectionPopup.ID) {
			fbReader.hideActivePopup();
		}
	}

	private void onPreferencesUpdate(int resultCode) {
		final FBReaderApp fbReader = (FBReaderApp)FBReaderApp.Instance();
		switch (resultCode) {
			case RESULT_DO_NOTHING:
				break;
			case RESULT_REPAINT:
			{
				AndroidFontUtil.clearFontCache();
				final BookModel model = fbReader.Model;
				if (model != null) {
					final Book book = model.Book;
					if (book != null) {
						book.reloadInfoFromDatabase();
						ZLTextHyphenator.Instance().load(book.getLanguage());
					}
				}
				fbReader.clearTextCaches();
				fbReader.getViewWidget().repaint();
				break;
			}
			case RESULT_RELOAD_BOOK:
				fbReader.reloadBook();
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case REQUEST_PREFERENCES:
			case REQUEST_BOOK_INFO:
				onPreferencesUpdate(resultCode);
				break;
			case REQUEST_CANCEL_MENU:
				((FBReaderApp)FBReaderApp.Instance()).runCancelAction(resultCode - 1);
				break;
		}
	}

	public void navigate() {
		((NavigationPopup)FBReaderApp.Instance().getPopupById(NavigationPopup.ID)).runNavigation();
	}

	private Menu addSubMenu(Menu menu, String id) {
		final ZLAndroidApplication application = (ZLAndroidApplication)getApplication();
		return application.myMainWindow.addSubMenu(menu, id);
	}

	private void addMenuItem(Menu menu, String actionId, String name, int order) {
		final ZLAndroidApplication application = (ZLAndroidApplication)getApplication();
		application.myMainWindow.addMenuItem(menu, actionId, null, name, order);
	}

	private void addMenuItem(Menu menu, String actionId, int iconId, int order) {
		final ZLAndroidApplication application = (ZLAndroidApplication)getApplication();
		application.myMainWindow.addMenuItem(menu, actionId, iconId, null, order);
	}

	private void addMenuItem(Menu menu, String actionId, int order) {
		final ZLAndroidApplication application = (ZLAndroidApplication)getApplication();
		application.myMainWindow.addMenuItem(menu, actionId, null, null, order);
	}

	private void addSubMenuBookmark(Menu menu, int order){
		final ZLAndroidApplication application = (ZLAndroidApplication)getApplication();
		application.myMainWindow.addSubMenuBookmark(menu, order);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.layout.menu, menu);
	//	addMenuItem(menu, ActionCode.SHOW_LIBRARY, R.drawable.ic_menu_library,R.id.library);
	//	addMenuItem(menu, ActionCode.SHOW_NETWORK_LIBRARY, R.drawable.ic_menu_networklibrary);
		addMenuItem(menu, ActionCode.SHOW_TOC, R.drawable.menu_style,R.id.toc);
	//	addMenuItem(menu, ActionCode.SHOW_BOOKMARKS, R.drawable.icon_bookmark);
	//	addMenuItem(menu, ActionCode.SWITCH_TO_NIGHT_PROFILE, R.drawable.ic_menu_night);
	//	addMenuItem(menu, ActionCode.SWITCH_TO_DAY_PROFILE, R.drawable.ic_menu_day);
	//	addMenuItem(menu, ActionCode.SEARCH, R.drawable.ic_menu_search);
    //		addMenuItem(menu, ActionCode.SHARE_BOOK, R.drawable.ic_menu_search);
		
		
	//	addMenuItem(menu, ActionCode.SHOW_BOOK_INFO);
       
		addSubMenuBookmark(menu,1);

		addMenuItem(menu, ActionCode.SHOW_PREFERENCES,R.drawable.setting_style,R.id.preferences);
//		addMenuItem(subMenu, ActionCode.SET_SCREEN_ORIENTATION_SYSTEM);
//		addMenuItem(subMenu, ActionCode.SET_SCREEN_ORIENTATION_SENSOR);
//		addMenuItem(subMenu, ActionCode.SET_SCREEN_ORIENTATION_PORTRAIT);
//		addMenuItem(subMenu, ActionCode.SET_SCREEN_ORIENTATION_LANDSCAPE);
//		if (ZLibrary.Instance().supportsAllOrientations()) {
//			addMenuItem(subMenu, ActionCode.SET_SCREEN_ORIENTATION_REVERSE_PORTRAIT);
//			addMenuItem(subMenu, ActionCode.SET_SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
//		}
//		addMenuItem(menu, ActionCode.INCREASE_FONT);
//		addMenuItem(menu, ActionCode.DECREASE_FONT);
//		addMenuItem(menu, ActionCode.SHOW_NAVIGATION,R.drawable.icon_back);
//		synchronized (myPluginActions) {
//			int index = 0;
//			for (PluginApi.ActionInfo info : myPluginActions) {
//				if (info instanceof PluginApi.MenuActionInfo) {
//					addMenuItem(
//						menu,
//						PLUGIN_ACTION_PREFIX + index++,
//						((PluginApi.MenuActionInfo)info).MenuItemName
//					);
//				}
//			}
//		}


//		final ZLAndroidApplication application = (ZLAndroidApplication)getApplication();
//		application.myMainWindow.refresh();

		return true;
	}
	public void dictionarySearch() {
		// Log.e("---dictionarySearch---", "--dictionarySearch--" + "yi xia");
//		showButton(DICTIONARY);
//		showLayout(DICTIONARY);
		String mySelectText = fbReader.BookTextView.getSelectedText();
		String[] textArr = mySelectText.split("@");
		// System.out.println("--:" + textArr[0] + "---:" + textArr[1] + "----:"
		// + textArr[2]+"-");
		final String[] newText = new String[textArr.length];
		newText[0] = textArr[0];
		for (int i = 1; i < textArr.length; i++) {
			// newText[i] = textArr[i-1].concat(textArr[i]);
			StringBuilder builder = new StringBuilder(textArr[0]);
			for (int j = 1; j <= i; j++) {
				builder.append(textArr[j]);
			}
			newText[i] = builder.toString();
		}
		Log.e("mySelectText.size", "size:" + mySelectText.length());
		Log.e("textArr.size", "size:" + textArr.length);
		Log.e("newText.size", "size:" + newText.length);
		boolean flag = false;
		StringBuilder exsitBuilder = new StringBuilder();
		String englishText = textArr[0].replaceAll("\\p{Punct}", "");
		char[] charArr = englishText.toCharArray();
		for(int j = 0; j<charArr.length; j++){
			if(Utils.isChineseEncode(charArr[j])){
				flag = true;
			}
		}
		if (flag == false || textArr[0].replaceAll("\\p{Punct}", "").length() <= 1) {
			for (int i = 0; i < textArr.length; i++) {
				if(flag == false){
					String resultText = getChineseByHanzi(newText[0].replaceAll("\\p{Punct}", ""));
//					tvDictionaryName.setText(newText[0].replaceAll("\\p{Punct}", ""));
//					tvDictionaryExplanation.setText(resultText);
					String name = newText[0].replaceAll("\\p{Punct}", "");
					showDictionary(name,resultText);
//					Toast.makeText(this, name + ": " +resultText, Toast.LENGTH_LONG).show();
				}
				Log.e("dictionarySearch", "newText.size():" + newText.length);
				if (newText.length >= 2) {
					String resultText = getChineseByHanzi(newText[i]
							.replaceAll("\\p{Punct}", ""));
					Log.e("Result Text",resultText);
					if (!(resultText.equals(getString(R.string.no_explaination)))) {
						exsitBuilder.append(newText[i].replaceAll("\\p{Punct}",
								""));
						exsitBuilder.append("@");
					}
				}
			}
		}

		String builderStr = exsitBuilder.toString();
		final String[] exsitText = builderStr.split("@");
		Log.e("dictionarySearch()", "exsitText.length:" + exsitText[0].length());
		if (textArr[0].replaceAll("\\p{Punct}", "").length() >= 24) {
//			showLayout(NOTE);
//			showButton(NOTE);
//			myNote = textArr[0];
		} else if (exsitText[0].length() == 0) {
			String resultText = null;
			if (textArr[0].replaceAll("\\p{Punct}", "").length() > 1
					&& textArr[0].replaceAll("\\p{Punct}", "").length() <= 2) {
				resultText = getChineseByHanzi(textArr[0].replaceAll(
						"\\p{Punct}", ""));
//				String name = textArr[0].replaceAll("\\p{Punct}", "");
//				Toast.makeText(this, name + ": " +resultText, Toast.LENGTH_LONG).show();
			} else {
				resultText = getString(R.string.no_explaination);
			}
//			if (!(resultText.equals("Ê≤°ÊúâÊü•ËØ¢Âà∞ÂØπÂ∫îÁöÑËß£Èáä!"))) {
//				NewWord newWord = new NewWord(getApplicationContext());
//				if (!(newWord.isExistInNewWord(mySelectText))) {
//					NewWordEntity newWordEntity = new NewWordEntity();
//					newWordEntity.setTitle(textArr[0].replaceAll("\\p{Punct}",
//							""));
//					newWordEntity.setContent(resultText);
//					newWord.insertNewWord(newWordEntity);
//					Toast.makeText(getApplicationContext(), "ÁîüËØçÊ∑ªÂä†ÊàêÂäü...",
//							Toast.LENGTH_LONG).show();
//				} else {
//					Toast.makeText(getApplicationContext(), "ËØ•ÂçïËØçÂ∑≤ÁªèÂ≠òÂú®‰∫éÁîüËØç‰∏≠...",
//							Toast.LENGTH_LONG).show();
//				}
//			}

		} else {
			String myDefaultStr = getChineseByHanzi(textArr[0].replaceAll(
					"\\p{Punct}", ""));
			String name = newText[0].replaceAll("\\p{Punct}", "");
			if (exsitText.length == 1) {
				if (!(myDefaultStr.equals(getString(R.string.no_explaination)))) {
					showDictionary(name,myDefaultStr);
				}
			}
			Log.e("textArr[0].length():",
					"L:" + textArr[0].replaceAll("\\p{Punct}", "").length());
			if (textArr[0].replaceAll("\\p{Punct}", "").length() == 1) {
				if (exsitText.length > 1) {
					Log.e("existText","AlertDialog");
					new AlertDialog.Builder(this)
							.setTitle(getString(R.string.SingleSelection))
							.setIcon(android.R.drawable.ic_dialog_info)
							.setSingleChoiceItems(exsitText, 0,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
											String mySelectText = exsitText[which];
											String myBiaoDian = getString(R.string.BiaoDian);
											int biaoDianSize = 0;
											for (int i = 0; i < mySelectText
													.length(); i++) {
												if (myBiaoDian.contains(String
														.valueOf(mySelectText
																.indexOf(i)))) {
													biaoDianSize++;
												}
											}
											if ((mySelectText.length() - biaoDianSize) <= 4) {
												mySelectText = mySelectText
														.replaceAll(
																"\\p{Punct}",
																"");
												// Log.e("dictionarySearch",
												// "dictionarySearch<=4");
												String searchResult = getChineseByHanzi(mySelectText);
//												tvDictionaryName
//														.setText(mySelectText);
//												mySelectName = mySelectText;
//												tvDictionaryExplanation
//														.setText(searchResult);
												String name = mySelectText;
												showDictionary(name,searchResult);
//												Toast.makeText(FBReader.this, name + ": " + searchResult, Toast.LENGTH_LONG).show();
												if (!(searchResult
														.equals(getString(R.string.no_explaination)))) {
//													NewWord newWord = new NewWord(
//															getApplicationContext());
//													if (!(newWord
//															.isExistInNewWord(mySelectText))) {
////														NewWordEntity newWordEntity = new NewWordEntity();
////														newWordEntity
////																.setTitle(mySelectText);
////														newWordEntity
////																.setContent(searchResult);
////														newWord.insertNewWord(newWordEntity);
//														Toast.makeText(
//																getApplicationContext(),
//																"ÁîüËØçÊ∑ªÂä†ÊàêÂäü...",
//																Toast.LENGTH_LONG)
//																.show();
//													} else {
//														Toast.makeText(
//																getApplicationContext(),
//																"ËØ•ÂçïËØçÂ∑≤ÁªèÂ≠òÂú®‰∫éÁîüËØç‰∏≠...",
//																Toast.LENGTH_LONG)
//																.show();
//													}
												}
											}
											// String searchResult =
											// getChineseByHanzi("‰∏Ä‰∏ã");

										}
									}).setNegativeButton(getString(R.string.Cancel), null).show();
				}

			} else if (textArr[0].replaceAll("\\p{Punct}", "").length() >= 2
					&& textArr[0].replaceAll("\\p{Punct}", "").length() < 24) {
				String mySelectResult = getChineseByHanzi(textArr[0].replaceAll("\\p{Punct}", ""));
				name = textArr[0].replaceAll("\\p{Punct}", "");
				Toast.makeText(FBReader.this, name + ": " + mySelectResult, Toast.LENGTH_LONG).show();
//				tvDictionaryName.setText(newText[0]);
//				tvDictionaryExplanation.setText(mySelectResult);
			
			} else if (textArr[0].replaceAll("\\p{Punct}", "").length() >= 24) {
				// Log.e("dictionarySearch",
				// "dictionarySearch>=else");
//				showLayout(NOTE);
//				showButton(NOTE);
//				myNote = textArr[0];
			}
		}

	}

	public String getChineseByHanzi(String hanzi) {
		Log.e("getChineseByHanzi", "hanzi:" + hanzi);
		File dbFile = new File("data/data/" + getPackageName() + "/databases", DBFILE);
		SQLiteDatabase db = SQLiteDatabase.openDatabase(
				dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
//		SQLiteDatabase db = SQLiteDatabase.openDatabase(Environment.getExternalStorageDirectory()+"/xiandaihanyucidian.db",null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.rawQuery(
				"select definition from dict where term=? limit 1",
				new String[] { hanzi.replaceAll("\\p{Punct}", "").trim() });
		if (db.isOpen()) {
			if (cursor.moveToFirst()) {
				String text = cursor.getString(0);
				cursor.close();
				db.close();
				text = text.replaceAll("BS", getString(R.string.BS));
				text = text.replaceAll("BH", getString(R.string.BH));
				Log.e("getChineseByHanzi->:", text);
				return text;
			}
		}
		cursor.close();
		db.close();
		return getString(R.string.no_explaination);
	}
	
	public void showDictionary(String name, String resultText){
		if(!resultText.equalsIgnoreCase(getString(R.string.no_explaination))){
			resultText = resultText.substring(9,resultText.length()-3);
		}
		Dialog alertDialog = new AlertDialog.Builder(this). 
                setTitle(name). 
                setMessage(resultText). 
                
                setNegativeButton(getString(R.string.back), new DialogInterface.OnClickListener() { 
                     
                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
                        // TODO Auto-generated method stub  
                    } 
                }).show();
	}
	    
}
