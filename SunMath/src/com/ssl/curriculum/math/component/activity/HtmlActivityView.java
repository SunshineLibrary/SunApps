package com.ssl.curriculum.math.component.activity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

import android.R.bool;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.activity.WebViewActivity;
import com.ssl.curriculum.math.component.viewer.ActivityViewer;
import com.ssl.curriculum.math.model.activity.LinkedActivityData;
import com.ssl.curriculum.math.utils.FileUtil;
import com.ssl.curriculum.math.utils.IOUtils;
import com.ssl.curriculum.math.utils.UnpackZipUtil;
import com.ssl.metadata.provider.MetadataContract;

/**
 * Created with IntelliJ IDEA. User: mendlin Date: 12-8-13 Time: 下午10:09 To
 * change this template use File | Settings | File Templates.
 */
public class HtmlActivityView extends ActivityView {
	
	class WebViewClientWithFramesAndGoBack extends WebViewClient{
		
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

	int activityId;
	Context context;
	CacheHtmlTask task;
	TextView titleTextView;
	WebView web;
	WebViewClientWithFramesAndGoBack client;
	Handler backHandler;
	Button backButton;
	Button fullButton;
	ProgressBar progressBar;
	boolean autoopen=false;
	LinearLayout mask;
	
	public HtmlActivityView(Context context, ActivityViewer activityViewer) {
		super(context, activityViewer);
		initUI();
		initComponents(context);
	}

	@Override
	public void setActivity(LinkedActivityData activityData) {
		super.setActivity(activityData);
		activityId = activityData.activityId;
		titleTextView.setText(activityData.name);
		mask.setVisibility(VISIBLE);
        //noteTextView.setText(activityData.notes==null?"":activityData.notes);
        task = new CacheHtmlTask();
        task.execute(activityId);
	}

	private void initUI() {
		LayoutInflater layoutInflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(
				R.layout.flipper_html_layout, this, false);
		addView(viewGroup);
	    titleTextView = (TextView) findViewById(R.id.flipper_html_title);
	    web = (WebView)findViewById(R.id.flipper_html_layout_webview);
	    backButton = (Button) findViewById(R.id.flipper_html_backbutton);
	    fullButton = (Button) findViewById(R.id.flipper_html_fullscreenbutton);
	    progressBar = (ProgressBar) findViewById(R.id.flipper_html_progress);
	    mask = (LinearLayout) findViewById(R.id.flipper_html_mask);
	    mask.setVisibility(INVISIBLE);
	    //noteTextView = (TextView) findViewById(R.id.flipper_external_notes);
    
	}

	private void initComponents(final Context context) {
		this.context = context;
		//Button button = (Button) findViewById(R.id.flipper_external_button);
		//button.setOnClickListener(this);
		web.getSettings().setJavaScriptEnabled(true);
		
		client = new WebViewClientWithFramesAndGoBack();
		web.setWebViewClient(client);
		web.setWebChromeClient(new WebChromeClient(){
        	@Override
        	public void onCloseWindow(WebView window) {
        		backHandler.sendMessage(new Message());
        	}
        });
		
		backHandler = new Handler(){
			@Override
    		public void handleMessage(android.os.Message msg) {
    			HtmlActivityView.this.back();
    		};
    	};
    	backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				back();
			}
		});
    	fullButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{
					if(task.getStatus().equals(AsyncTask.Status.FINISHED)){
							String viewing = web.getUrl();
							if(viewing.startsWith("file://")){
								openHtmlFullscreen(viewing);
							}else{
								openHtmlFullscreen(task.get().getAbsolutePath());
							}
						}else{
							Toast.makeText(context, 
						                "正在缓存HTML文件，稍后自动打开", 
						                Toast.LENGTH_SHORT).show();	
							autoopen = true;
						}			
						
				}catch (Exception e) {
					Log.e("HtmlActivityView", "onClick", e);
				}				
			}
		});
	}

	public void openHtml(File indexPath){
		if(indexPath==null||!indexPath.exists()){
			Toast.makeText(context, 
	                "缓存HTML文件时出错，正在重试缓存该文件", 
	                Toast.LENGTH_SHORT).show();
			task = new ForceClearCacheHtmlTask();
			task.execute(activityId);			
			return;
		}
		mask.setVisibility(View.INVISIBLE);
		web.loadUrl("file://"+indexPath.getAbsolutePath());
	}
	
	public void openHtmlFullscreen(String indexPath){
		Intent intent = new Intent();
		intent.putExtra("indexPath", indexPath);
		intent.setClass(context, WebViewActivity.class);
		context.startActivity(intent);
	}

	
	
	private class ForceClearCacheHtmlTask extends CacheHtmlTask{
		
		@Override
		protected File doInBackground(Integer... params) {
			int activityId = params[0];
			File baseDir = context.getCacheDir();
			File cacheDir = new File(baseDir, "_html_cache_" + activityId);
			File cacheFile = new File(baseDir, "_html_cache_zip_"+activityId+".zip");
			FileUtil.rmr(cacheDir);
			FileUtil.rmr(cacheFile);
			return super.doInBackground(params);
		}
	}
	
	private class CacheHtmlTask extends AsyncTask<Integer, Integer, File> {

		Pattern[] indexPatterns = new Pattern[]{
			Pattern.compile("^\\/?index.html?$", Pattern.CASE_INSENSITIVE),
			Pattern.compile("^\\/?(.*\\/)*index.html?$", Pattern.CASE_INSENSITIVE),
			Pattern.compile("^\\/?(.*\\/)*(.*).html?$", Pattern.CASE_INSENSITIVE)
		};
		
		
		void clearCache(File baseDir){
			File[] cachesFiles = baseDir.listFiles(new FileFilter() {

				@Override
				public boolean accept(File pathname) {
					return (pathname.isDirectory()&&pathname.getName().startsWith("_html_cache_"))||
							(pathname.isFile()&&pathname.getName().startsWith("_html_cache_zip_")&&pathname.getName().endsWith(".zip"));
				}				
				
			});
			Arrays.sort(cachesFiles, new Comparator<File>(){
				@Override
				public int compare(File lhs, File rhs) {
					return Long.valueOf(lhs.lastModified()).compareTo(rhs.lastModified());
				}});
			if(cachesFiles.length>4){
				for(int i=0;i<cachesFiles.length-4;i++){
					FileUtil.rmr(cachesFiles[i]);
				}
			}
		}
		
		File findHtml(File baseDir, Pattern... patterns){
			Queue<File> bfsfinder = new LinkedList<File>();
			File[] matches = new File[patterns.length];
			bfsfinder.offer(baseDir);
			while (bfsfinder.size()>0) {
				File f = bfsfinder.poll();
				if(f.isDirectory()){
					for(File sf:f.listFiles()){
						bfsfinder.offer(sf);
					}
				}else{
					String relName=f.getAbsolutePath();
					if(relName.startsWith(baseDir.getAbsolutePath())){
						relName = relName.substring(baseDir.getAbsolutePath().length());
						for (int i = 0; i < matches.length; i++) {
							if(matches[i]==null && patterns[i].matcher(relName).matches()){
								matches[i] = f;
							}
						}
					}else{
						continue;
					}
				}				
			}
			for (int i = 0; i < matches.length; i++) {
				if(matches[i]!=null){
					return matches[i];
				}
			}
			return null;
		}
		
		@Override
		protected File doInBackground(Integer... params) {
			int activityId = params[0];
			File baseDir = context.getCacheDir();
			clearCache(baseDir);
			
			File cacheFile = new File(baseDir, "_html_cache_zip_"+activityId+".zip");
			File cacheDir = new File(baseDir, "_html_cache_" + activityId);
			if(cacheDir.exists() && cacheDir.isDirectory()){
				return findHtml(cacheDir, indexPatterns);
			}
			
            if(!cacheFile.exists()){
            	InputStream in = null;
            	OutputStream out = null;
            	try {
            		cacheFile.createNewFile();
                	ParcelFileDescriptor pfd = getContext().getContentResolver().openFileDescriptor(MetadataContract.Activities.getActivityHtmlUri(activityId), "r");
                	final long total = pfd.getStatSize();
					try{
						in = new BufferedInputStream(new ParcelFileDescriptor.AutoCloseInputStream(pfd));
						out = new BufferedOutputStream(new FileOutputStream(cacheFile));
						if(total>0){
							IOUtils.transfer(in, out, new IOUtils.ProgressUpdater() {							
								@Override
								public void onProgressUpdate(long totalBytesProcessed) {
									publishProgress((int)(totalBytesProcessed*30/total));
								}
							});	
						}else{
							publishProgress(-1);
							IOUtils.transfer(in, out);
						}
					}finally{
						try{
							if(in!=null){
								in.close();
							}
						}finally{
							if(out != null){
								out.close();
							}
						}
					}
					if(total>0){
						UnpackZipUtil.unZipUtf8(cacheFile, cacheDir, new IOUtils.ProgressUpdater() {
							@Override
							public void onProgressUpdate(long totalBytesProcessed) {
								publishProgress(30+(int)(totalBytesProcessed*60/total));
							}
						});
					}else{
						publishProgress(-1);
						UnpackZipUtil.unZipUtf8(cacheFile, cacheDir, null);
					}
					return findHtml(cacheDir, indexPatterns);
				} catch (IOException e) {
					Log.e("HtmlActivityView", "CacheHtmlTask", e);
					File f = findHtml(cacheDir, indexPatterns);
					if(f!=null){
						backHandler.post(new Runnable() {							
							@Override
							public void run() {
								Toast.makeText(context, 
						                "缓存HTML文件时出错，文件可能有部分内容显示错误", 
						                Toast.LENGTH_LONG).show();								
							}
						});
					}
					return f;
				} finally{
					try{
						if(in!=null){
							in.close();
						}						
					}catch (IOException e) {
						Log.wtf("HtmlActivityView", e);
					}
				}
            }else{
            	try{
            		final long total = cacheFile.length();
            		if(total>0){
						UnpackZipUtil.unZipUtf8(cacheFile, cacheDir, new IOUtils.ProgressUpdater() {
							@Override
							public void onProgressUpdate(long totalBytesProcessed) {
								publishProgress(50+(int)(totalBytesProcessed*50/total));
							}
						});
					}else{
						publishProgress(-1);
						UnpackZipUtil.unZipUtf8(cacheFile, cacheDir, null);
					}
	            	return findHtml(cacheDir, indexPatterns);
            	}catch (IOException e) {
            		Log.e("HtmlActivityView", "CacheHtmlTask", e);
            		File f = findHtml(cacheDir, indexPatterns);
					if(f!=null){
						backHandler.post(new Runnable() {							
							@Override
							public void run() {
								Toast.makeText(context, 
						                "缓存HTML文件时出错，文件可能有部分内容显示错误", 
						                Toast.LENGTH_LONG).show();								
							}
						});
					}
					return f;
				}
            }
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			if(progressBar!=null){
				if(values[0]<0){
					progressBar.setIndeterminate(true);
				}else{
					progressBar.setMax(100);
					progressBar.setProgress(values[0]);
				}
			}
		}
		
		@Override
		protected void onPostExecute(File indexPath) {
			super.onPostExecute(indexPath);
			synchronized (HtmlActivityView.this) {
            	openHtml(indexPath);
            	if(autoopen){
            		openHtmlFullscreen(indexPath.getAbsolutePath());
            	}
			}  
		}
	}
	
	public void back() {
		if(web.canGoBack()){
			web.goBack();
		}
	}
}
