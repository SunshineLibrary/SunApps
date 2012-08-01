package com.ssl.curriculum.math.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.ssl.curriculum.math.data.GalleryContentData;

import java.io.FileNotFoundException;
import java.io.IOException;

public class GalleryGridAdapter extends BaseAdapter {
    private Context context;
    private GalleryContentData galleryContentData;

    public GalleryGridAdapter(Context context) {
        this.context = context;
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

    private void updateImageView(ImageView imageView, int position) throws FileNotFoundException {
        ParcelFileDescriptor pfdInput = context.getContentResolver().openFileDescriptor(Uri.parse(galleryContentData.getGalleryImage(position).getThumbnailUri()), "r");
        if (pfdInput == null) return;
        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(pfdInput.getFileDescriptor(), null, null);
        imageView.setImageBitmap(bitmap);
    }

    private ImageView createNewImageView(int position) throws IOException {
        ImageView imageView = new ImageView(context);
        ParcelFileDescriptor pfdInput = context.getContentResolver().openFileDescriptor(Uri.parse(galleryContentData.getGalleryImage(position).getThumbnailUri()), "r");
        if (pfdInput == null) return imageView;
        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(pfdInput.getFileDescriptor(), null, null);
        imageView.setImageBitmap(bitmap);
        pfdInput.close();
        return imageView;
    }

    public void setGalleryData(GalleryContentData galleryContentData) {
        this.galleryContentData = galleryContentData;
    }

    public void setSelectedPosition(int position) {
        galleryContentData.setSelectedPosition(position);
    }
}
