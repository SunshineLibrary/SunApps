package com.ssl.curriculum.math.task;

import com.ssl.curriculum.math.model.activity.VideoActivityData;
import com.ssl.curriculum.math.presenter.activity.VideoActivityPresenter;
import com.ssl.curriculum.math.service.VideoActivityContentProvider;

import android.os.AsyncTask;

public class FetchVideoActivityTask extends AsyncTask<Void, Void, VideoActivityData> {
	private VideoActivityContentProvider vacp;
	private VideoActivityPresenter presenter;
	private int id = 0;
	public FetchVideoActivityTask(VideoActivityPresenter presenter, VideoActivityContentProvider provider){
		vacp = provider;
		this.id = presenter.getId();
		this.presenter = presenter;
	}
	
	@Override
	protected VideoActivityData doInBackground(Void... arg0) {
		return vacp.fetchVideoData(this.id);
	}

	@Override
	protected void onPostExecute(VideoActivityData result) {
		this.presenter.setVideoData(result);
	}
}
