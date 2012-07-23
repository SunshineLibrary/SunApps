package com.ssl.curriculum.math.page;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import com.ssl.curriculum.math.R;

public class GalleryPage extends LinearLayout {
    private String[] info = new String[] {MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.DATA};
    private GridView gridview;

    public GalleryPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
        initData();
        initListener();
    }

    private void initUI() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.gallery_page, this, false);
        this.addView(viewGroup);
        gridview = (GridView) findViewById(R.id.image_viewer_grid_view);
    }

    private void initData() {
        ContentResolver contentResolver = this.getContext().getContentResolver();
        Cursor imageCursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, info, null, null, null);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getContext(), R.layout.gallery_page_item, imageCursor, new String[]{MediaStore.Images.Thumbnails.DATA}, new int[]{R.id.ItemImage});
        gridview.setAdapter(adapter);
    }

    private void initListener() {
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }


}
