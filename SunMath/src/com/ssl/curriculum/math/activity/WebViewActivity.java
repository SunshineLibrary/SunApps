package com.ssl.curriculum.math.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.ssl.curriculum.math.R;

/**
 * Created with IntelliJ IDEA. User: mendlin Date: 12-8-14 Time: 下午4:44 To
 * change this template use File | Settings | File Templates.
 */
public class WebViewActivity extends Activity implements OnClickListener{
	
class WebViewClientWithFramesAndGoBack extends WebViewClient {
		
		boolean startOverride=false;
    	boolean goingBack=false;
    	
    	public void goBackOnce(){
    		goingBack = true;
    	}
    	
    	
    	@Override
    	public boolean shouldOverrideUrlLoading(WebView view, String url) {
    		if(goingBack){
    			return false;
    		}else if(startOverride){
    			view.loadUrl(url);
    			return true;
    		}else{
    			return false;
    		}
    	}
    	
    	@Override
    	public void onPageFinished(WebView view, String url) {
    		super.onPageFinished(view, url);
    		startOverride = true;
    		goingBack = false;
    		super.onPageFinished(view, url);
    		startOverride = true;
    		goingBack = false;
    		view.addJavascriptInterface(new Object(){
    			@SuppressWarnings("unused") //javascript engine will invoke this by reflection
				public void close(){
    				backHandler.sendMessage(new Message());
    			}    			
    		}, "closeHandler");
    		view.loadUrl("javascript:window.close=function(){closeHandler.close();};");
    		view.loadUrl("javascript:var t=document.getElementsByTagName(\"input\");for(var i in t){if(t[i]&&t[i].setAttribute)t[i].setAttribute(\"style\",\"display:none;\")};");
    	}
    	
    	@Override
    	public void onPageStarted(WebView view, String url, Bitmap favicon) {
    		startOverride = false;
    		super.onPageStarted(view, url, favicon);
    	}
    	
	}
	
	private WebView webView;
	private String loadUrl;
	private ProgressDialog dialog;
	private WebViewClientWithFramesAndGoBack client;
	Handler backHandler;
	Button exitButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		setContentView(R.layout.webview_layout);
		loadUrl = getIntent().getStringExtra("indexPath");
		Uri u=Uri.parse(loadUrl);
		if(u.getScheme()==null||u.getScheme().length()==0){
			loadUrl = "file://"+loadUrl;
		}
		//loadUrl = "file:///" + getIntent().getCharSequenceExtra("indexPath");
		
		webView = (WebView) findViewById(R.id.webview_main);
		webView.getSettings().setJavaScriptEnabled(true);
		
		client = new WebViewClientWithFramesAndGoBack();
		webView.setWebViewClient(client);
		webView.setWebChromeClient(new WebChromeClient(){
        	@Override
        	public void onCloseWindow(WebView window) {
        		backHandler.sendMessage(new Message());
        	}
        });
		
	    
		dialog.dismiss();
		webView.loadUrl(loadUrl);
		//webView.loadUrl("file:////mnt/sdcard/Download/_html_cache_zip_142/index.htm");
		//System.out.println(loadUrl);
		backHandler = new Handler(){
        	
    		public void handleMessage(android.os.Message msg) {
    			WebViewActivity.this.onBackPressed();
    		};
    	};
    	
    	exitButton = (Button) findViewById(R.id.webview_exit_fullscreen);
    	exitButton.setOnClickListener(this);
	}
	
	@Override
	public void onBackPressed() {
		if(webView.canGoBack()){
			webView.goBack();
		}else{
			this.finish();
		}
	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent();
		i.putExtra("url", webView.getUrl());
		this.setResult(RESULT_OK, i);
		this.finish();
	}
}
