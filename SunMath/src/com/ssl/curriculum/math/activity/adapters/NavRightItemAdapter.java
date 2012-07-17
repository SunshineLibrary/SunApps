package com.ssl.curriculum.math.activity.adapters;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

public class NavRightItemAdapter extends ArrayAdapter<NavItem>{

	public NavRightItemAdapter(Context context, int resource,int textViewResourceId, List<NavItem> objects) {
		super(context, resource, textViewResourceId, objects);
	}
 
}
