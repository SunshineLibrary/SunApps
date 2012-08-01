package com.sunshine.sunresourcecenter;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ResourceListGridAdapter extends BaseAdapter{

	private LayoutInflater inflater;
	private ArrayList<ResourceListGridItem> gridItemList;

	public ResourceListGridAdapter(List<Object> reslistList,  Context context) {
		super();
		this.gridItemList = (ArrayList) reslistList;
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
		ListViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.reslist_grid_item, null);
			viewHolder = new ListViewHolder();
			viewHolder.title = (TextView) convertView.findViewById(R.id.list_title);
			viewHolder.intro = (TextView) convertView.findViewById(R.id.list_intro);
			viewHolder.builder = (TextView) convertView.findViewById(R.id.list_builder);
			viewHolder.tags = (TextView) convertView.findViewById(R.id.list_tags);
			viewHolder.count = (TextView) convertView.findViewById(R.id.list_count);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ListViewHolder) convertView.getTag();
		}
		viewHolder.title.setText(((ResourceListGridItem) gridItemList.get(position)).getTitle());
		viewHolder.builder.setText(((ResourceListGridItem) gridItemList.get(position)).getBuilder());
		viewHolder.tags.setText(((ResourceListGridItem) gridItemList.get(position)).getTags());
		viewHolder.intro.setText(((ResourceListGridItem) gridItemList.get(position)).getIntro());
		viewHolder.count.setText(String.valueOf(((ResourceListGridItem) gridItemList.get(position)).getCount()));
		
		// convertView.setMinimumHeight(260);
		return convertView;
	}
}

class ListViewHolder {
	public TextView title;
	public TextView builder;
	public TextView tags;
	public TextView intro;
	public TextView count;
}
