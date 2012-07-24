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
import com.ssl.curriculum.math.model.GalleryItem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends BaseAdapter {
    private Context context;
    private List<GalleryItem> galleryItemList;

    public GalleryAdapter(Context context) {
        this.context = context;
        galleryItemList = new ArrayList<GalleryItem>();
    }

    public int getCount() {
        return galleryItemList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup viewGroup) {
        ImageView imageView = (ImageView) view;
        try {
            if (imageView != null) updateImageView(imageView, position);
            else imageView = createNewImageView(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageView;
    }

    private void updateImageView(ImageView imageView, int position) throws FileNotFoundException {
        ParcelFileDescriptor pfdInput = context.getContentResolver().openFileDescriptor(Uri.parse(galleryItemList.get(position).getThumbnailUri()), "r");
        if (pfdInput == null) return;
        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(pfdInput.getFileDescriptor(), null, null);
        imageView.setImageBitmap(bitmap);
    }

    private ImageView createNewImageView(int position) throws IOException {
        ImageView imageView = new ImageView(context);
        ParcelFileDescriptor pfdInput = context.getContentResolver().openFileDescriptor(Uri.parse(galleryItemList.get(position).getThumbnailUri()), "r");
        if (pfdInput == null) return imageView;
        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(pfdInput.getFileDescriptor(), null, null);
        imageView.setImageBitmap(bitmap);
        pfdInput.close();
        return imageView;
    }

    public void setGalleryData(List<GalleryItem> galleryItemList) {
        this.galleryItemList = galleryItemList;
        notifyDataSetChanged();
    }
}
