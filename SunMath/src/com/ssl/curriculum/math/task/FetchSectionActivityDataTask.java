package com.ssl.curriculum.math.task;

import android.os.AsyncTask;
import com.ssl.curriculum.math.listener.SectionActivityDataReceiver;
import com.ssl.curriculum.math.service.SectionActivityContentProvider;

public class FetchSectionActivityDataTask extends AsyncTask<Void, Void, Void> {
	private SectionActivityContentProvider contentProviderSection;
	private SectionActivityDataReceiver sectionActivityDataReceiver;
	private int activityId = 0;
    private int sectionId;

    public FetchSectionActivityDataTask(SectionActivityContentProvider providerSection, SectionActivityDataReceiver sectionActivityDataReceiver, int sectionId, int activityId){
        this.sectionActivityDataReceiver = sectionActivityDataReceiver;
        contentProviderSection = providerSection;
        this.activityId = activityId;
        this.sectionId = sectionId;
    }
	
	@Override
	protected Void doInBackground(Void... voids) {
        contentProviderSection.fetchSectionActivityData(sectionActivityDataReceiver, sectionId, activityId);
        return null;
    }
}
