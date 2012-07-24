package com.ssl.curriculum.math.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.model.MetadataContract;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends Activity {

    private ViewFlipper viewFlipper;
    private ImageView leftBtn;
    private ImageView rightBtn;
    private ImageView naviBtn;
    private Animation animFlipInFromRight;
    private Animation animFlipInFromLeft;
    private Animation animFlipOutToRight;
    private Animation animFlipOutToLeft;
    private ImageView contentProviderImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initListeners();
        loadAnimation();
        loadData();
    }

    private void loadData() {
        ContentResolver contentResolver = this.getContentResolver();
        String[] columns = {MetadataContract.Gallery._ID, MetadataContract.Gallery._IMAGE_PATH, MetadataContract.Gallery._THUMBNAIL_PATH, MetadataContract.Gallery._DESCRIPTION};
        String imageContentUri = null;
        Cursor cursor = contentResolver.query(MetadataContract.Gallery.CONTENT_URI, columns, null, null, null);
        if (cursor.moveToFirst()) {
            int thumbnailImageIndex = cursor.getColumnIndex(MetadataContract.Gallery._THUMBNAIL_PATH);
            imageContentUri = cursor.getString(thumbnailImageIndex);
        }
        cursor.close();

        if (imageContentUri == null) return;
        System.out.println("-------------------------------imageContentUri = " + imageContentUri);
        try {
            ParcelFileDescriptor pfdInput = contentResolver.openFileDescriptor(Uri.parse(imageContentUri), "r");
            System.out.println("---------------------pfdInput = " + pfdInput);
            if(pfdInput == null) return;
            Bitmap bitmap = BitmapFactory.decodeFileDescriptor(pfdInput.getFileDescriptor(), null, null);
            contentProviderImageView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            System.out.println("-------------------e = " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initUI() {
        setContentView(R.layout.main_layout);
        viewFlipper = (ViewFlipper) this.findViewById(R.id.main_activity_view_flipper);
        this.leftBtn = (ImageView) this.findViewById(R.id.main_activity_left_btn);
        this.rightBtn = (ImageView) this.findViewById(R.id.main_activity_right_btn);
        this.naviBtn = (ImageView) this.findViewById(R.id.main_activity_navi_btn);
        contentProviderImageView = (ImageView) this.findViewById(R.id.content_provider_image_view);
    }

    private void initListeners() {
        this.leftBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                viewFlipper.setInAnimation(animFlipInFromRight);
                viewFlipper.setOutAnimation(animFlipOutToLeft);
                viewFlipper.showPrevious();
            }
        });
        this.rightBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                viewFlipper.setInAnimation(animFlipInFromLeft);
                viewFlipper.setOutAnimation(animFlipOutToRight);
                viewFlipper.showNext();
            }
        });
        this.naviBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NaviActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadAnimation() {
        animFlipInFromLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.flip_in_from_left);
        animFlipInFromRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.flip_in_from_right);
        animFlipOutToRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.flip_out_to_right);
        animFlipOutToLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.flip_out_to_left);
    }
}