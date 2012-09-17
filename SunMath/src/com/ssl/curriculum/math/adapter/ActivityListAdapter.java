package com.ssl.curriculum.math.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.ssl.curriculum.math.R;

import static com.ssl.metadata.provider.MetadataContract.Activities;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class ActivityListAdapter extends CursorAdapter {

    public ActivityListAdapter(Context context, Cursor c) {
        super(context, c, false);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newView = inflater.inflate(R.layout.navi_activity_horizontal_item, parent, false);
        bindView(newView, context, cursor);
        return newView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView iv_image = (ImageView) view.findViewById(R.id.image);
        TextView tv_title = (TextView) view.findViewById(R.id.title);
        TextView tv_status = (TextView) view.findViewById(R.id.status);
        View v_mask = view.findViewById(R.id.mask);

        long id = cursor.getLong(cursor.getColumnIndex(Activities._ID));
        int status = cursor.getInt(cursor.getColumnIndex(Activities._DOWNLOAD_STATUS));
        int progress = cursor.getInt(cursor.getColumnIndex(Activities._DOWNLOAD_PROGRESS));
        int type = cursor.getInt(cursor.getColumnIndex(Activities._TYPE));
        String title = cursor.getString(cursor.getColumnIndex(Activities._NAME));

        tv_title.setText(title);
        switch (type) {
            case Activities.TYPE_VIDEO:
                iv_image.setImageResource(R.drawable.ic_main_thumbnail_video);
                break;
            case Activities.TYPE_TEXT:
                iv_image.setImageResource(R.drawable.ic_main_thumbnail_photo_album);
                break;
            case Activities.TYPE_QUIZ:
                iv_image.setImageResource(R.drawable.ic_main_thumbnail_quiz);
                break;
        }

        switch (status) {
            case Activities.STATUS_NOT_DOWNLOADED:
                tv_status.setText("未下载");
                break;
            case Activities.STATUS_QUEUED:
                tv_status.setText("等待下载");
                break;
            case Activities.STATUS_DOWNLOADING:
                tv_status.setText("正在下载:" + progress + "%");
                break;
            case Activities.STATUS_DOWNLOADED:
                tv_status.setVisibility(View.INVISIBLE);
                v_mask.setVisibility(View.INVISIBLE);
                break;
        }
    }
}
