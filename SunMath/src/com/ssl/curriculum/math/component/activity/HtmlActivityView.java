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
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
public class HtmlActivityView extends ActivityView implements View.OnClickListener {

	int activityId;
	Context context;
	CacheHtmlTask task;
	TextView titleTextView;
	TextView noteTextView;
	boolean autoOpen = false;
	
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
        noteTextView.setText(activityData.notes==null?"":activityData.notes);
        task = new CacheHtmlTask();
		task.execute(activityId);
	}

	private void initUI() {
		LayoutInflater layoutInflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(
				R.layout.flipper_external_layout, this, false);
		addView(viewGroup);
	    titleTextView = (TextView) findViewById(R.id.flipper_external_title);
        noteTextView = (TextView) findViewById(R.id.flipper_external_notes);
    
	}

	private void initComponents(final Context context) {
		this.context = context;
		Button button = (Button) findViewById(R.id.flipper_external_button);
		button.setOnClickListener(this);
	}

	public void onClick(View v) {
		try{
			synchronized (this) {
				if(task.getStatus().equals(AsyncTask.Status.FINISHED)){
					openHtml(task.get());
				}else{
					Toast.makeText(context, 
				                "正在缓存HTML文件，稍后自动打开", 
				                Toast.LENGTH_SHORT).show();
					autoOpen = true;									
				}			
			}	
		}catch (Exception e) {
			Log.e("HtmlActivityView", "onClick", e);
		}
	}
	
	public void openHtml(File indexPath){
		if(indexPath==null||!indexPath.exists()){
			Toast.makeText(context, 
	                "缓存HTML文件时出错，请重试打开该文件", 
	                Toast.LENGTH_SHORT).show();
			synchronized (this) {
				autoOpen = false;
			}
			task = new ForceClearCacheHtmlTask();
			task.execute(activityId);
			return;
		}
		Intent intent = new Intent();
		intent.putExtra("indexPath", indexPath.getAbsolutePath());
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
				Pattern.compile("^\\/?index.html?$",Pattern.CASE_INSENSITIVE),
				Pattern.compile("^\\/?(.*\\/)*index.html?$",Pattern.CASE_INSENSITIVE),
				Pattern.compile("^\\/?(.*\\/)*(.*).html?$",Pattern.CASE_INSENSITIVE)
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
					try{
						in = new BufferedInputStream(new ParcelFileDescriptor.AutoCloseInputStream(pfd));
						out = new BufferedOutputStream(new FileOutputStream(cacheFile));
						IOUtils.transfer(in, out);	
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
					UnpackZipUtil.unZipUtf8(cacheFile, cacheDir);
					return findHtml(cacheDir, indexPatterns);
				} catch (IOException e) {
					Log.e("HtmlActivityView", "CacheHtmlTask", e);
					File f = findHtml(cacheDir, indexPatterns);
					if(f!=null){
						Toast.makeText(context, 
				                "缓存HTML文件时出错，文件可能有部分内容显示错误", 
				                Toast.LENGTH_SHORT).show();
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
	            	UnpackZipUtil.unZipUtf8(cacheFile, cacheDir);
	            	return findHtml(cacheDir, indexPatterns);
            	}catch (IOException e) {
            		Log.e("HtmlActivityView", "CacheHtmlTask", e);
            		File f = findHtml(cacheDir, indexPatterns);
					if(f!=null){
						Toast.makeText(context, 
				                "缓存HTML文件时出错，文件可能有部分内容显示错误", 
				                Toast.LENGTH_SHORT).show();
					}
					return f;
				}
            }
		}

		@Override
		protected void onPostExecute(File indexPath) {
			super.onPostExecute(indexPath);
			synchronized (HtmlActivityView.this) {
            	if(autoOpen){
                	openHtml(indexPath);
                }
			}  
		}
	}
}
