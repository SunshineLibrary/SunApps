package com.ssl.curriculum.math.service;

import com.sunshine.metadata.provider.MetadataContract.Activities;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

public class ActivityContentProvider {
	private Context context;
	public ActivityContentProvider (Context context){
		this.context = context;
	}
	
	private Cursor query(String[] columns){
		ContentResolver cr = context.getContentResolver();
		return cr.query(Activities.CONTENT_URI, columns, null,null,null);
	}
}
