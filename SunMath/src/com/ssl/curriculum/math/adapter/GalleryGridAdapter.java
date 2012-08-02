package com.ssl.curriculum.math.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.ssl.curriculum.math.data.GalleryContentData;

import java.io.IOException;

public class GalleryGridAdapter extends BaseAdapter {
    private Context context;
    private GalleryContentData galleryContentData;

    public GalleryGridAdapter(Context context) {
        this.context = context;
        galleryContentData = new GalleryContentData();
    }

    public int getCount() {
        return galleryContentData.getCount();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup viewGroup) {
        ImageView imageView = null;
        try {
            if (view == null) {
                imageView = createNewImageView(position);
            } else {
                imageView = (ImageView) view;
                updateImageView(imageView, position);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageView;
    }

    private void updateImageView(ImageView imageView, int position) throws IOException {
        Bitmap bitmap = getBitmap(position);
        if(bitmap == null) return;
        imageView.setImageBitmap(bitmap);
    }

    private ImageView createNewImageView(int position) throws IOException {
        Bitmap bitmap = getBitmap(position);
        ImageView imageView = new ImageView(context);
        if(bitmap == null) return imageView;
        imageView.setImageBitmap(bitmap);
        return imageView;
    }

    private Bitmap getBitmap(int position) throws IOException {
        ParcelFileDescriptor pfdInput = context.getContentResolver().openFileDescriptor(galleryContentData.getGalleryItem(position).getThumbnailUri(), "r");
        if(pfdInput == null) return null;
        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(pfdInput.getFileDescriptor(), null, null);
        pfdInput.close();
        return bitmap;
    }

    public void setGalleryData(GalleryContentData galleryContentData) {
        this.galleryContentData = galleryContentData;
        notifyDataSetChanged();
    }

    public void setSelectedPosition(int position) {
        galleryContentData.setSelectedPosition(position);
    }
}
