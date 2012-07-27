package com.ssl.curriculum.math.service;

import java.util.concurrent.Callable;

import com.ssl.curriculum.math.model.activity.ActivityData;
import com.ssl.curriculum.math.model.activity.VideoActivityData;
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
	
	public ActivityData fetchActivityById(int id){
		return new ActivityData((int) Math.round(Math.random() * 5));
		/*
		Cursor cursor = this.query(new String[] {Activities._ID, Activities._NAME, Activities._TYPE, Activities._NOTES, Activities._LENGTH, Activities._SECTION_ID}, 
			 Activities._ID + " = " + id);
		if(cursor.moveToFirst()){
			int idIndex = cursor.getColumnIndex(Activities._ID);
			int nameIndex = cursor.getColumnIndex(Activities._NAME);
			int typeIndex = cursor.getColumnIndex(Activities._TYPE);
			int notesIndex = cursor.getColumnIndex(Activities._NOTES);
			int lengthIndex = cursor.getColumnIndex(Activities._LENGTH);
			int sectionIndex = cursor.getColumnIndex(Activities._SECTION_ID);
			do{
				int type = cursor.getInt(typeIndex);
				if(type == Activities.Types.VIDEO.ordinal()){
					VideoActivityData vad = new VideoActivityData();
					vad.initVideoMetadata(cursor.getString(nameIndex), cursor.getString(notesIndex), cursor.getInt(lengthIndex));
					vad.setUniqueId(cursor.getInt(idIndex));
					vad.setSectionId(cursor.getInt(sectionIndex));
					return vad;
				}else{
					ActivityData ad = new ActivityData(type);
					ad.setUniqueId(cursor.getInt(idIndex));
					ad.setSectionId(cursor.getInt(sectionIndex));
				}
			}while(cursor.moveToNext());
		}else{	
			// Fetch Failed, Need to do something here in the future
		}
		return new ActivityData(-1);*/
	}
}
