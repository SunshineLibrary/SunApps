package com.ssl.curriculum.math.service;

import java.util.concurrent.Callable;

import com.sunshine.metadata.provider.MetadataContract.Activities;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

public class ActivityContentProvider {
	private Context context;
	public ActivityContentProvider (Context context){
		this.context = context;
	}
	
	protected Cursor query(String[] columns, String queryString){
		ContentResolver cr = context.getContentResolver();
		return cr.query(Activities.CONTENT_URI, columns, queryString,null,null);
	}
}
