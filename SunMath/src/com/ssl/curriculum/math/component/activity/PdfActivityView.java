package com.ssl.curriculum.math.component.activity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.ssl.curriculum.math.component.viewer.ActivityViewer;
import com.ssl.curriculum.math.model.activity.LinkedActivityData;
import com.ssl.curriculum.math.utils.FileUtil;
import com.ssl.curriculum.math.utils.IOUtils;
import com.ssl.metadata.provider.MetadataContract;

/**
 * Created with IntelliJ IDEA.
 * User: mendlin
 * Date: 12-8-13
 * Time: 下午10:09
 * To change this template use File | Settings | File Templates.
 */
public class PdfActivityView extends ActivityView implements View.OnClickListener {

	int activityId;
	TextView titleTextView;
	TextView noteTextView;
	Context context;
	CachePdfTask task;
	
    public PdfActivityView(Context context, ActivityViewer activityViewer) {
        super(context, activityViewer);
        initUI();
        initComponents(context);        
    }
    
    @Override
    public void setActivity(LinkedActivityData activityData) {
        super.setActivity(activityData);        
        activityId = activityData.activityId;
        titleTextView.setText(activityData.name);
        noteTextView.setText(activityData.notes);
        task = new CachePdfTask();
        task.execute(activityId);
    }

    private void initUI() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.flipper_pdf_layout, this, false);
        addView(viewGroup);
        titleTextView = (TextView) findViewById(R.id.flipper_pdf_title);
        noteTextView = (TextView) findViewById(R.id.flipper_pdf_notes);
    }

    private void initComponents(final Context context) {
    	this.context = context;
        Button button = (Button) findViewById(R.id.flipper_pdf_button);
        button.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		
		try{
			synchronized (this) {
				if(task.getStatus().equals(AsyncTask.Status.FINISHED)){
					openPdfFile(task.get());
				}else{
					Toast.makeText(context, 
				                "正在缓存PDF文件，稍后自动打开", 
				                Toast.LENGTH_SHORT).show();
					autoOpen = true;									
				}			
			}	
		}catch (Exception e) {
			Log.e("PdfActivityView", "onClick", e);			
		}
	}

	private void openPdfFile(File pdfFile){
		if(pdfFile==null){
			return;
		}
		Intent intent = new Intent(Intent.ACTION_VIEW);
		Uri path = Uri.fromFile(pdfFile);
		intent.setDataAndType(path, "application/pdf");
		try {
            context.startActivity(intent);
        } 
        catch (ActivityNotFoundException e) {
            Toast.makeText(context, 
                "未安装PDF阅读器", 
                Toast.LENGTH_SHORT).show();
        }
	}
	
	boolean autoOpen = false;
	
	private class CachePdfTask extends AsyncTask<Integer,Integer,File> {

		void clearCache(File baseDir){
			File[] cachesFiles = baseDir.listFiles(new FilenameFilter() {				
				@Override
				public boolean accept(File dir, String filename) {
					return filename.startsWith("_pdf_cache_") && filename.endsWith(".pdf");
				}
			});
			Arrays.sort(cachesFiles, new Comparator<File>(){
				@Override
				public int compare(File lhs, File rhs) {
					return Long.valueOf(lhs.lastModified()).compareTo(rhs.lastModified());
				}});
			if(cachesFiles.length>3){
				for(int i=0;i<cachesFiles.length-3;i++){
					FileUtil.rmr(cachesFiles[i]);
				}
			}
		}
		
		@SuppressLint("WorldReadableFiles")		
        @Override
        protected File doInBackground(Integer... params) {    
        	int activityId = params[0];
            File baseDir = context.getFilesDir();
            clearCache(baseDir);
            String cacheFileName = "_pdf_cache_"+activityId+".pdf";
            File cacheFile = new File(baseDir, cacheFileName);
            if(!cacheFile.exists()){
            	InputStream in = null;
            	OutputStream out = null;
            	try {
            		cacheFile.createNewFile();
                	ParcelFileDescriptor pfd = getContext().getContentResolver().openFileDescriptor(MetadataContract.Activities.getActivityPdfUri(activityId), "r");
					in = new BufferedInputStream(new ParcelFileDescriptor.AutoCloseInputStream(pfd));
					out = new BufferedOutputStream(context.openFileOutput(cacheFileName, Context.MODE_WORLD_READABLE));
					IOUtils.transfer(in, out);					
				} catch (IOException e) {
					Log.e("PdfActivityView", "CachePdfTask", e);
					return null;
				} finally{
					try{
						if(in!=null){
							in.close();
						}
						if(out!=null){
							out.close();
						}
					}catch (IOException e) {
						Log.wtf("PdfActivityView", e);
					}
				}
            }
            return cacheFile;
        }

        @Override
        protected void onPostExecute(File o) {
            super.onPostExecute(o);  
            synchronized (PdfActivityView.this) {
            	if(autoOpen){
                	openPdfFile(o);
                }
			}            
        }
    }
	
}
