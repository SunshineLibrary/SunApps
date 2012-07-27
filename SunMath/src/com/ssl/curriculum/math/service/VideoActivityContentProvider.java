package com.ssl.curriculum.math.service;

import com.ssl.curriculum.math.model.activity.VideoActivityData;
import com.sunshine.metadata.provider.MetadataContract.Activities;

import android.content.Context;
import android.database.Cursor;

public class VideoActivityContentProvider extends ActivityContentProvider {

	public VideoActivityContentProvider(Context context) {
		super(context);
	}
	
	public VideoActivityData fetchVideoData(int id){
		VideoActivityData vad = new VideoActivityData();
		Cursor cursor = this.query(new String[] {Activities._ID, Activities._NAME, Activities._NOTES, Activities._LENGTH}, 
				Activities._TYPE + " = " + Activities.Types.VIDEO.ordinal() + " AND " + Activities._ID + " = " + id);
		if(cursor.moveToFirst()){
			int idIndex = cursor.getColumnIndex(Activities._ID);
			int nameIndex = cursor.getColumnIndex(Activities._NAME);
			int notesIndex = cursor.getColumnIndex(Activities._NOTES);
			int lengthIndex = cursor.getColumnIndex(Activities._LENGTH);
			do{
				vad.initVideoMetadata(cursor.getString(nameIndex), cursor.getString(notesIndex), cursor.getInt(lengthIndex));
				vad.setUniqueId(cursor.getInt(idIndex));
			}while(cursor.moveToNext());
		}else{	
			/** Fetch Failed, Need to do something here in the future **/
		}
		return vad;
	}
}
