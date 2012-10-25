package com.ssl.curriculum.math.activity;

import java.util.ArrayList;
import java.util.HashMap;

import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.download.manage.DownloadManageActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;

public class EntranceActivity extends Activity implements OnItemClickListener {
	
	public static final String[] SUBJECTS = {"数学", "英语", "测试科目", null, null, null};
	
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
		
		HashMap<String, Object> map;
		for(String name: SUBJECTS){
			map= new HashMap<String, Object>();
			map.put("SubjectName", name);
			gridItems.add(map);
		}
		
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
		intent.putExtra("subject", SUBJECTS[position]);
        intent.setClass(this, NavigationActivity.class);
        this.startActivity(intent);
	}
	
	
}
