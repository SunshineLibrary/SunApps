package com.ssl.curriculum.math.component.activity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.activity.WebViewActivity;
import com.ssl.curriculum.math.component.viewer.ActivityViewer;
import com.ssl.curriculum.math.model.activity.LinkedActivityData;
import com.ssl.curriculum.math.utils.UnpackZipUtil;
import com.ssl.metadata.provider.MetadataContract;

/**
 * Created with IntelliJ IDEA. User: mendlin Date: 12-8-13 Time: 下午10:09 To
 * change this template use File | Settings | File Templates.
 */
public class HtmlActivityView extends ActivityView implements
		View.OnClickListener {

	ParcelFileDescriptor htmlFileDescriptor;
	int activityId;
	Context context;
	CacheHtmlTask task;

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
		task.execute(activityId);
	}

	private class CacheHtmlTask extends AsyncTask<Integer, Integer, String> {

		void transfer(InputStream in, OutputStream out) throws IOException {
			byte[] buf = new byte[512];
			int len = in.read(buf);
			while (len > 0) {
				out.write(buf, 0, len);
				len = in.read(buf);
			}
			out.flush();
		}

		@Override
		protected String doInBackground(Integer... params) {
			int activityId = params[0];
			File baseDir = context.getCacheDir();
			File cacheFile = new File(baseDir, "_" + "aa" + ".zip");

			InputStream in = null;
			OutputStream out = null;
			try {
				cacheFile.createNewFile();
				ParcelFileDescriptor html = getContext() .getContentResolver() .openFileDescriptor(
								MetadataContract.Activities .getActivityHtmlUri(activityId), "r");
				in = new BufferedInputStream(
						new ParcelFileDescriptor.AutoCloseInputStream(html));
				out = new BufferedOutputStream(new FileOutputStream(cacheFile));
				transfer(in, out);
				String indexpath = UnpackZipUtil.unZip(
						cacheFile.getAbsolutePath(),
						context.getCacheDir().toString());
				return indexpath;
			} catch (IOException e) {
				Log.e("HtmlActivityView", "CacheHtmlTask", e);
				return null;
			} finally {
				try {
					if (in != null) {
						in.close();
					}
					if (out != null) {
						out.close();
					}
				} catch (IOException e) {
					Log.wtf("HtmlActivityView", e);
				}
			}
		}

		@Override
		protected void onPostExecute(String indexPath) {
			super.onPostExecute(indexPath);
			Intent intent = new Intent();
			intent.putExtra("indexPath", indexPath);
			intent.setClass(context, WebViewActivity.class);
			context.startActivity(intent);
		}

	}

}
