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

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
public class HtmlActivityView extends ActivityView implements
		View.OnClickListener {

	int activityId;
	Context context;
	CacheHtmlTask task;
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
		task = new CacheHtmlTask();
		task.execute(activityId);
		
	}

	private void initUI() {
		LayoutInflater layoutInflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(
				R.layout.flipper_html_layout, this, false);
		addView(viewGroup);

	}

	private void initComponents(final Context context) {
		this.context = context;
		Button button = (Button) findViewById(R.id.flipper_html_button);
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
			Log.e("PdfActivityView", "onClick", e);			
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
		intent.putExtra("indexPath", indexPath);
		intent.setClass(context, WebViewActivity.class);
		context.startActivity(intent);
	}

	private class ForceClearCacheHtmlTask extends CacheHtmlTask{
		
		@Override
		protected File doInBackground(Integer... params) {
			int activityId = params[0];
			File baseDir = context.getCacheDir();
			File cacheDir = new File(baseDir, "_html_cache+" + activityId);
			FileUtil.rmr(cacheDir);
			return super.doInBackground(params);
		}
	}
	
	private class CacheHtmlTask extends AsyncTask<Integer, Integer, File> {

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
		
		@Override
		protected File doInBackground(Integer... params) {
			int activityId = params[0];
			File baseDir = context.getCacheDir();
			clearCache(baseDir);
			
			File cacheFile = new File(baseDir, "_html_cache_zip_"+activityId+".zip");
			File cacheDir = new File(baseDir, "_html_cache_+" + activityId);
			if(cacheDir.exists() && cacheDir.isDirectory()){
				return new File(cacheDir, "index.html");
			}
			
            if(!cacheFile.exists()){
            	InputStream in = null;
            	OutputStream out = null;
            	try {
            		cacheFile.createNewFile();
                	ParcelFileDescriptor pfd = getContext().getContentResolver().openFileDescriptor(MetadataContract.Activities.getActivityPdfUri(activityId), "r");
					in = new BufferedInputStream(new ParcelFileDescriptor.AutoCloseInputStream(pfd));
					try{
					out = new BufferedOutputStream(new FileOutputStream(cacheFile));
					IOUtils.transfer(in, out);	
					}finally{
						if(out != null){
							out.close();
						}
					}
					UnpackZipUtil.unZipUtf8(cacheFile, cacheDir);
					File f1=new File(cacheDir, "index.html");
					if(f1.exists()) return f1;
					File f2=new File(cacheDir, "index.htm");
					if(f2.exists()) return f2;
					return null;
				} catch (IOException e) {
					Log.e("HtmlActivityView", "CacheHtmlTask", e);
					return null;
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
					File f1=new File(cacheDir, "index.html");
					if(f1.exists()) return f1;
					File f2=new File(cacheDir, "index.htm");
					if(f2.exists()) return f2;
					return null;
            	}catch (IOException e) {
            		Log.e("HtmlActivityView", "CacheHtmlTask", e);
            		return null;
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
