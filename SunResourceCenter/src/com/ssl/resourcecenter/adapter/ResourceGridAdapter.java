package com.ssl.resourcecenter.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ssl.resourcecenter.model.ResourceGridItem;
import com.sunshine.sunresourcecenter.R;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ResourceGridAdapter extends BaseAdapter {
	
	private LayoutInflater inflater;
	private List<ResourceGridItem> gridItemList;
	private boolean showProgress = false;
	private ContentResolver contentResolver;
	private HashMap<Integer,Bitmap> dataCache;


	public ResourceGridAdapter(List<Object> resList, boolean showProgress, Context context) {
		super();
		this.gridItemList = (ArrayList) resList;
		this.inflater = LayoutInflater.from(context);
		this.showProgress = showProgress;
		this.contentResolver = context.getContentResolver();
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
		ResViewHolder viewHolder;
		int progress = 0;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.res_grid_item, null);
			viewHolder = new ResViewHolder();
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
			viewHolder.description = (TextView) convertView.findViewById(R.id.description);
			viewHolder.author = (TextView) convertView.findViewById(R.id.author);
			viewHolder.tags = (TextView) convertView.findViewById(R.id.tags);
			viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.reading_progress);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ResViewHolder) convertView.getTag();
		}
		viewHolder.title.setText(((ResourceGridItem) gridItemList.get(position)).getTitle());
		viewHolder.author.setText(((ResourceGridItem) gridItemList.get(position)).getAuthor());
		viewHolder.tags.setText(((ResourceGridItem) gridItemList.get(position)).getTags());
		viewHolder.description.setText(((ResourceGridItem) gridItemList.get(position)).getDescription());
		
		viewHolder.image.setImageBitmap(((ResourceGridItem) gridItemList.get(position)).getImageBitmap());
		
		progress = ((ResourceGridItem) gridItemList.get(position)).getProgress();
		viewHolder.progressBar.setProgress(progress);
		if(showProgress){
			convertView.findViewById(R.id.tags).setVisibility(View.INVISIBLE);
			((TextView)convertView.findViewById(R.id.progress_count)).setText(progress + "%");
		}else{
			convertView.findViewById(R.id.reading_progress).setVisibility(View.INVISIBLE);
		}
		
		// convertView.setMinimumHeight(260);
		return convertView;
	}
}

class ResViewHolder {
	public ImageView image;
	public TextView title;
	public TextView author;
	public TextView tags;
	public TextView description;
	public ProgressBar progressBar;
}