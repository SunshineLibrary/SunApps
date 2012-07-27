package com.sunshine.support.mock;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import com.sunshine.metadata.provider.MetadataContract;
import com.sunshine.support.storage.FileStorage;
import com.sunshine.support.storage.FileStorageManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageTestData {
    private static final String IMAGE_DIR_PATH = "gallery/image";
    private static final String THUMBNAIL_DIR_PATH = "gallery/thumbnail";

    private FileStorage fileStorage;
    private Resources resources;
    private ContentResolver contentResolver;
    private File imageDir;
    private File thumbnailDir;
    private Context context;

    public ImageTestData(Context context) {
        this.resources = context.getResources();
        this.contentResolver = context.getContentResolver();
        this.context = context;
        fileStorage = FileStorageManager.getInstance().getWritableFileStorage();
        createDirs();
    }

    private void createDirs() {
        imageDir = fileStorage.mkdir(IMAGE_DIR_PATH);
        thumbnailDir = fileStorage.mkdir(THUMBNAIL_DIR_PATH);
    }

    public void prepareDataForGallery(String imageFileName, String thumbnailFileName, int imageResource, int thumbnailResource, String description) {
        try {
            writeResourceToFile(imageDir, imageFileName, imageResource);
            writeResourceToFile(thumbnailDir, thumbnailFileName, thumbnailResource);
            writeImageFileUriToDB(imageFileName, thumbnailFileName, description);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeImageFileUriToDB(String imageFileName, String thumbnailFileName, String description) {
        ContentValues values = new ContentValues();
        values.put(MetadataContract.GalleryImages._IMAGE_PATH, Uri.decode(MetadataContract.AUTHORITY_URI.buildUpon().appendPath(IMAGE_DIR_PATH).appendPath(imageFileName).build().toString()));
        values.put(MetadataContract.GalleryImages._THUMBNAIL_PATH, Uri.decode(MetadataContract.AUTHORITY_URI.buildUpon().appendPath(THUMBNAIL_DIR_PATH).appendPath(thumbnailFileName).build().toString()));
        values.put(MetadataContract.GalleryImages._DESCRIPTION, description);
        contentResolver.insert(MetadataContract.GalleryImages.CONTENT_URI, values);
    }

    private void writeResourceToFile(File imageDir, String imageFileName, int imageResource) throws IOException {
        File image = fileStorage.createFile(imageDir, imageFileName);
        FileOutputStream fos = new FileOutputStream(image);
        Bitmap bm = BitmapFactory.decodeResource(resources, imageResource);
        bm.compress(Bitmap.CompressFormat.JPEG, 80, fos);
        fos.close();
    }

    public boolean hasPreparedData() {
        SharedPreferences settings = context.getSharedPreferences("MockImageSettings", 0);
        return settings.getBoolean("isMockedImageDataPrepared", false);
    }

    public void setHasPreparedData(boolean preparedData) {
        SharedPreferences settings = context.getSharedPreferences("MockImageSettings", 0);
        settings.edit().putBoolean("isMockedImageDataPrepared", preparedData).commit();
    }
}
