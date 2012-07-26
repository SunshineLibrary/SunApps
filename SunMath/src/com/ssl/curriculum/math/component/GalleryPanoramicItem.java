package com.ssl.curriculum.math.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.ssl.curriculum.math.R;

public class GalleryPanoramicItem extends RelativeLayout {
    private ImageView itemImageView;

    public GalleryPanoramicItem(Context context) {
        super(context);
        initUI();
    }

    private void initUI() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.gallery_panoramic_item, this, false);
        this.addView(viewGroup);
        itemImageView = (ImageView) findViewById(R.id.gallery_panoramic_item_image);
    }

    public void setItemImageBitmap(Bitmap bitmap) {
        itemImageView.setImageBitmap(bitmap);
    }
}
