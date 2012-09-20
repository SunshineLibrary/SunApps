package com.ssl.curriculum.math.download.manage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.ssl.curriculum.math.R;

/**
 * User: jtong
 * Date: 9/19/12
 */
public class DownloadedItemAdapter extends BaseAdapter {
    private Context context;
    private String[] itemNames;

    public DownloadedItemAdapter(Context context, String[] itemNames) {
        this.context = context;
        this.itemNames = itemNames;
    }

    @Override
    public int getCount() {
        return itemNames.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = inflater.inflate(R.layout.downloaded_item, null);


            TextView textView = (TextView) gridView
                    .findViewById(R.id.downloaded_item_label);
            textView.setText(itemNames[position]);

            ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.downloaded_item_image);

            String itemName = itemNames[position];

            imageView.setImageResource(R.drawable.ic_navi_section_thumbnail);

        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }
}
