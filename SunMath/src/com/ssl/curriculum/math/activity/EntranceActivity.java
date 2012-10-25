package com.ssl.curriculum.math.activity;

import java.util.ArrayList;
import java.util.HashMap;

import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.download.manage.DownloadManageActivity;
import com.ssl.metadata.provider.MetadataContract;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;

public class EntranceActivity extends Activity implements OnItemClickListener {
	
	public static ArrayList<String> subjectIds, subjectNames;
	
	private GridView gridView;
	private SimpleAdapter gridAdapter;
	private ArrayList<HashMap<String, Object>> gridItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();
		initComponents();
		loadData();
		intiListener();
	}
	
	private void initUI() {
       setContentView(R.layout.entrance_layout);
       gridView = (GridView) findViewById(R.id.entryGridView);
       
	}

	private void initComponents() {
		gridItems = new ArrayList<HashMap<String, Object>>();
		gridAdapter = new SimpleAdapter(this, gridItems, 
				R.layout.entrance_griditem_layout, 
				new String[] { "SubjectName" },
				new int[] { R.id.textViewSubjectName });
		gridView.setAdapter(gridAdapter);
		
	}
	
	private void loadData(){
		subjectIds = new ArrayList<String>();
		subjectNames = new ArrayList<String>();
		String[] cols = {MetadataContract.Subjects._ID, MetadataContract.Subjects._NAME};
		ContentResolver rs = this.getContentResolver();
		HashMap<String, Object> map;
		Cursor cr = rs.query(MetadataContract.Subjects.CONTENT_URI, cols, null, null, null);
		if(cr.moveToFirst()){
			int idCol = cr.getColumnIndex(MetadataContract.Subjects._ID);
			int nameCol = cr.getColumnIndex(MetadataContract.Subjects._NAME);
			do{
				String id = String.valueOf(cr.getInt(idCol));
				String name = cr.getString(nameCol);
				map= new HashMap<String, Object>();
				map.put("SubjectName", name);
				gridItems.add(map);
				
				subjectIds.add(id);
				subjectNames.add(name);
			}while(cr.moveToNext());
		}
		cr.close();
	}

	private void intiListener() {
		gridView.setOnItemClickListener(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		Intent intent = new Intent();
		intent.putExtra("subjectId", subjectIds.get(position));
		intent.putExtra("subjectName", subjectNames.get(position));
        intent.setClass(this, NavigationActivity.class);
        this.startActivity(intent);
	}
	
	
}
