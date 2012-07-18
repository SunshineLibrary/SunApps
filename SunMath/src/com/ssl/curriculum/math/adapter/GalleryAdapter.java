package com.ssl.curriculum.math.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import com.ssl.curriculum.math.R;

public class GalleryAdapter extends BaseAdapter {
    private int drawableArray[] = new int[]{R.drawable.ic_main_activity_show_next_disable, R.drawable.ic_main_activity_show_next_enable, R.drawable.ic_main_activity_show_previous_disable};
    private Context context;

    public GalleryAdapter(Context context) {
        this.context = context;
    }

    public int getCount() {
        return Integer.MAX_VALUE;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup viewGroup) {
        ImageView imageView = (ImageView) view;
        if(imageView != null) return imageView;
        imageView = createNewImageView(position);
        return imageView;

    }

    private ImageView createNewImageView(int position) {
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(drawableArray[position % drawableArray.length]);
        imageView.setLayoutParams(new Gallery.LayoutParams(Gallery.LayoutParams.FILL_PARENT, Gallery.LayoutParams.FILL_PARENT));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }
}
