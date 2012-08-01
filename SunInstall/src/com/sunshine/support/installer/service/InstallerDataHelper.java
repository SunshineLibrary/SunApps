package com.sunshine.support.installer.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.Closeable;
import java.util.ArrayList;

public class InstallerDataHelper extends SQLiteOpenHelper implements Closeable {
	
	
	
	SQLiteDatabase db;
	
	InstallerDataHelper(Context context) {
		super(context, "installer", null, 1);
		db = getWritableDatabase();
		
	}

	public Iterable<InstallRecord> getScheduledInstalls(){
		ArrayList<InstallRecord> map = new ArrayList<InstallRecord>();
		Cursor c = null;
		try{
			c = db.query("installqueue", new String[]{"name","version","apk"}, null, null, null, null, null);
			while(c.moveToNext()){
				InstallRecord r = new InstallRecord(c.getString(c.getColumnIndexOrThrow("name")), c.getString(c.getColumnIndexOrThrow("version")),c.getString(c.getColumnIndexOrThrow("apk")));
				map.add(r);
			}
			
		}finally{
			if(c!=null){
				c.close();
			}
		}
		return map;
	}
	
	public void setInstallSchedule(String name, String version, String apk){
		boolean exist;
		Cursor c = null;
		try{
			c = db.query("installqueue", new String[]{"name"}, "name=?", new String[]{name}, null, null, null);
			exist = c.moveToFirst();			
		}finally{
			if(c!=null){
				c.close();
			}
		}
		ContentValues values=new ContentValues();
		values.put("name", name);
		values.put("version", version);
		values.put("apk", apk);
		db.beginTransaction();
		try{
			if(exist){
				int count = db.update("installqueue", values, "name=?", new String[]{name});	
				if(count!=1){
					Log.e("InstallerData", "error on update records");
				}
			}else{
				long id = db.insertOrThrow("installqueue", null, values);
				Log.i("InstallerData", "insert new record "+id);
			}
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
		
		
	}
	
	public void removeInstallSchedule(String name){
		db.beginTransaction();
		try{
			db.delete("installqueue", "name=?", new String[]{name});
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE installqueue (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, version TEXT, apk TEXT NULL);"); 
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}
	
	
	
	@Override
	public synchronized void close() {
		if(db!=null){
			if(db.inTransaction()){
				db.endTransaction();
			}
			db.close();
		}
	}
	
}
