
package org.geometerplus.android.fbreader;

import org.geometerplus.fbreader.fbreader.ActionCode;
import org.geometerplus.fbreader.fbreader.FBReaderApp;

import android.content.Intent;

public class ShowActionBarAction extends FBAndroidAction {

	ShowActionBarAction(FBReader baseActivity, FBReaderApp fbreader) {
		super(baseActivity, fbreader);
	}
		
	@Override
	protected void run(Object ... params) {
		Intent intent= new Intent(BaseActivity.getApplicationContext(), FBReader.class);
		intent.setAction(ActionCode.SHOW_ACTIONBAR);
		BaseActivity.startActivity(intent);
	}
}