package com.ssl.curriculum.math.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
 
public class NaviActivity extends Activity {
	
private ArrayList<String> ListDescriptors = new ArrayList<String>();
	
	/** Creates a listView from custom dataObject **/
	private void createListFromData(ArrayList<String> data, ListView lv){
		lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data));
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    private void initUI() {
        setContentView(R.layout.navigation_layout);
        
        ListDescriptors.add("返回");
        ListDescriptors.add("一元二次方程");
        ListDescriptors.add("方程组");
        
        ListView leftNav = (ListView) findViewById(R.id.nav_left);
        leftNav.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	public void onItemClick(AdapterView<?> arg0, View arg1, int pos,long id) {
				System.out.println("POS: " + pos + " ID: " + id);
			}
		});
        createListFromData(ListDescriptors,leftNav);
    }

}
