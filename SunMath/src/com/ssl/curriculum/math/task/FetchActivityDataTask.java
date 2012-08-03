package com.ssl.curriculum.math.task;

import android.os.AsyncTask;

import com.ssl.curriculum.math.logic.ActivityFlowController;
import com.ssl.curriculum.math.model.activity.DomainActivityData;
import com.ssl.curriculum.math.service.ActivityContentProvider;

public class FetchActivityDataTask extends AsyncTask<Void, Void, DomainActivityData> {
	private ActivityContentProvider vacp;
	private ActivityFlowController flipper;
	private int id = 0;
	public FetchActivityDataTask(ActivityFlowController flipper, ActivityContentProvider provider, int id){
		vacp = provider;
		this.id = id;
		this.flipper = flipper;
	}
	
	@Override
	protected DomainActivityData doInBackground(Void... arg0) {
		return vacp.fetchActivityById(this.id);
	}

	@Override
	protected void onPostExecute(DomainActivityData result) {
		this.flipper.onReceivedActivityData(result);
	}
}
