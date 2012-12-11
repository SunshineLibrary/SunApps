package org.geometerplus.android.fbreader.preferences;

import org.geometerplus.android.fbreader.FBReader;
import org.geometerplus.fbreader.fbreader.ColorProfile;
import org.geometerplus.fbreader.fbreader.FBReaderApp;
import org.geometerplus.zlibrary.core.library.ZLibrary;
import org.geometerplus.zlibrary.core.options.ZLIntegerRangeOption;
import org.geometerplus.zlibrary.core.options.ZLStringOption;
import org.geometerplus.zlibrary.text.view.ZLTextView;
import org.geometerplus.zlibrary.text.view.style.ZLTextStyleCollection;
import org.geometerplus.zlibrary.ui.android.library.ZLAndroidLibrary;
import org.sunshinelibrary.sunreader.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class SunReaderPreference extends Activity{
	private final FBReaderApp fbReader = (FBReaderApp)FBReaderApp.Instance();
	private final ZLAndroidLibrary androidLibrary = (ZLAndroidLibrary)ZLAndroidLibrary.Instance();
	private final ColorProfile profile = fbReader.getColorProfile();
	private final ZLTextView view = fbReader.getTextView();
	private final ZLTextView.PagePosition pagePosition = view.pagePosition();
	private TextView lightText;
	private TextView pageText;
	private StringBuilder tvLight;
	private StringBuilder tvPage;
	private SeekBar lightSeek;
	private SeekBar pageSeek;
	private EditText pageEdit;
	private int pageToGo;
	private static String UNCHANGED = "unchanged";
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.preference);
		WindowManager.LayoutParams windowParams = getWindow().getAttributes();
        windowParams.height = 760;
        windowParams.width = 712;
        getWindow().setAttributes(windowParams);
		lightText=(TextView)findViewById(R.id.tv_lightseek);
		tvLight=new StringBuilder();
        lightSeek=(SeekBar)findViewById(R.id.light_seek);
        int bright = ZLibrary.Instance().getScreenBrightness();
        tvLight.append(bright).append('%');
        lightSeek.setProgress(bright);
        lightText.setText(tvLight);
        lightSeek.setOnSeekBarChangeListener(sbLis);
        
        pageText=(TextView)findViewById(R.id.tv_pgseek);
        tvPage = new StringBuilder();
        pageSeek = (SeekBar)findViewById(R.id.page_seek);
        tvPage.append(pagePosition.Current);
        pageEdit = (EditText)findViewById(R.id.jump_pagenum);
        pageEdit.setText(tvPage);
        pageEdit.clearFocus();
        
        pageSeek.setMax(pagePosition.Total - 1);
		pageSeek.setProgress(pagePosition.Current - 1);
		int PecentInt = (pagePosition.Current-1)*100/(pagePosition.Total-1);
		int PecentDeci = (pagePosition.Current-1)*1000/(pagePosition.Total-1)-PecentInt*10;
		tvPage = new StringBuilder();
		tvPage.append(PecentInt).append('.').append(PecentDeci).append('%');
		pageText.setText(tvPage);
		pageSeek.setOnSeekBarChangeListener(sbLis2);
	
	}
	
	
	private OnSeekBarChangeListener sbLis=new OnSeekBarChangeListener(){
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			tvLight=new StringBuilder();
			int bright = lightSeek.getProgress()+20;
			tvLight.append(bright).append('%');
			lightText.setText(tvLight);
			ZLibrary.Instance().setScreenBrightness(bright);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
 
    };
	
    private OnSeekBarChangeListener sbLis2=new OnSeekBarChangeListener(){
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			tvPage = new StringBuilder();
			int page = pageSeek.getProgress()+1;
			int PecentInt = (page-1)*100/(pagePosition.Total-1);
			int PecentDeci = (page-1)*1000/(pagePosition.Total-1)-PecentInt*10;
			tvPage.append(PecentInt).append('.').append(PecentDeci).append('%');
			pageText.setText(tvPage);
			tvPage = new StringBuilder();
			tvPage.append(page);
			pageEdit.setText(tvPage);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			pageToGo =  pageSeek.getProgress();
		}
 
    };
    
	public void onDefaultFontsize(View v){
		ZLIntegerRangeOption myOption = ZLTextStyleCollection.Instance().getBaseStyle().FontSizeOption;
		myOption.setValue(20);
		setResult(FBReader.RESULT_REPAINT);
		finish();
	}
	
	public void onFontsizeSmaller(View v){
		ZLIntegerRangeOption myOption = ZLTextStyleCollection.Instance().getBaseStyle().FontSizeOption;
		myOption.setValue(myOption.getValue()-2);
		setResult(FBReader.RESULT_REPAINT);
		finish();
	}
	public void onFontsizeLarger(View v){
		ZLIntegerRangeOption myOption = ZLTextStyleCollection.Instance().getBaseStyle().FontSizeOption;
		myOption.setValue(myOption.getValue()+2);
		setResult(FBReader.RESULT_REPAINT);
		finish();
	}
	public void setScreenBrightness(){
		ZLibrary.Instance().setScreenBrightness(0);
	}
	public void onSepiaSelected(View v){
		ZLStringOption myOption=profile.WallpaperOption;
		myOption.setValue("wallpapers/sepia.jpg");
		setResult(FBReader.RESULT_REPAINT);
		finish();
	}
	
	public void onStyleDefaultSelected(View v){
		ZLStringOption myOption=profile.WallpaperOption;
		myOption.setValue("");
		setResult(FBReader.RESULT_REPAINT);
		finish();
	}
	
	public void onWarmyellowSelected(View v){
		ZLStringOption myOption=profile.WallpaperOption;
		myOption.setValue("wallpapers/wood.jpg");
		setResult(FBReader.RESULT_REPAINT);
		finish();
	}
	 
	public void onGraygreenSelected(View v){
		ZLStringOption myOption=profile.WallpaperOption;
		myOption.setValue("wallpapers/graygreen.jpg");
		setResult(FBReader.RESULT_REPAINT);
		finish();
	}
	public void backToMain(View v){
		setResult(FBReader.RESULT_REPAINT);
		finish();
	}
	
	public void gotoSelectedPage(View v){
		pageToGo = Integer.parseInt(pageEdit.getText().toString());
		if (pageToGo == 1) {
			view.gotoHome();
		} else {
			view.gotoPage(pageToGo);
		}
		fbReader.getViewWidget().reset();
		fbReader.getViewWidget().repaint();
		finish();
	}
}
