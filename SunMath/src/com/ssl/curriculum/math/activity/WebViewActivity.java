package com.ssl.curriculum.math.activity;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.webkit.WebView;

import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.utils.AssetCopyUtil;
import com.ssl.curriculum.math.utils.UnpackZipUtil;

/**
 * Created with IntelliJ IDEA. User: mendlin Date: 12-8-14 Time: 下午4:44 To
 * change this template use File | Settings | File Templates.
 */
public class WebViewActivity extends Activity {
	private WebView webView;
	private String loadUrl;
	private ProgressDialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		setContentView(R.layout.webview_layout);
		
		loadUrl = getIntent().getStringExtra("indexPath");
		if(!loadUrl.startsWith("file://")){
			loadUrl = "file://"+loadUrl;
		}
		
		webView = (WebView) findViewById(R.id.webview_main);
		webView.getSettings().setJavaScriptEnabled(true);
	 
		dialog.dismiss();
		webView.loadUrl(loadUrl);
		System.out.println(loadUrl);
	}
}
