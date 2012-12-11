package org.geometerplus.android.fbreader;

import org.geometerplus.fbreader.fbreader.FBReaderApp;
import org.geometerplus.fbreader.library.Bookmark;
import org.sunshinelibrary.sunreader.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

public class BookmarkAddActivity extends Activity{
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
		addBookmark();
		Toast.makeText(getApplicationContext(), R.string.add_bookmark_success, Toast.LENGTH_LONG);		
	}
	
	
	private void addBookmark() {
		final FBReaderApp fbreader = (FBReaderApp)FBReaderApp.Instance();
		fbreader.addInvisibleBookmark();	
	}
}
