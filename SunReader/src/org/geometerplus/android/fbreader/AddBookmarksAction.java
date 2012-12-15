
package org.geometerplus.android.fbreader;

import org.geometerplus.fbreader.fbreader.FBReaderApp;

class AddBookmarksAction extends RunActivityAction {
	AddBookmarksAction(FBReader baseActivity, FBReaderApp fbreader) {
		super(baseActivity, fbreader, BookmarkAddActivity.class);
	}
}