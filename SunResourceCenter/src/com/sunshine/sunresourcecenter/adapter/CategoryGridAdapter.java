package com.sunshine.sunresourcecenter.adapter;

import java.util.ArrayList;
import java.util.List;

import com.sunshine.sunresourcecenter.R;
import com.sunshine.sunresourcecenter.R.id;
import com.sunshine.sunresourcecenter.R.layout;
import com.sunshine.sunresourcecenter.griditem.CategoryGridItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CategoryGridAdapter extends BaseAdapter{
	
	private LayoutInflater inflater;
	private ArrayList<CategoryGridItem> gridItemList;

	public CategoryGridAdapter(List<Object> cateList, Context context){
		super();
		this.gridItemList = (ArrayList) cateList;
		this.inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		if (null != gridItemList) {
			return gridItemList.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return gridItemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CateViewHolder viewHolder;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.category_grid_item, null);
			viewHolder = new CateViewHolder();
			viewHolder.name = (TextView)convertView.findViewById(R.id.cate_name);
			viewHolder.count = (TextView)convertView.findViewById(R.id.cate_count);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (CateViewHolder) convertView.getTag();
		}
		viewHolder.name.setText(((CategoryGridItem)gridItemList.get(position)).getName());
		viewHolder.count.setText(String.valueOf( ((CategoryGridItem)gridItemList.get(position)).getCount() ));
		
		return convertView;
	}

}

class CateViewHolder {
	public TextView name;
	public TextView count;
}
