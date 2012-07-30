package com.sunshine.support.storage;

import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.provider.MetadataContract;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.util.Log;
import com.sunshine.support.api.ApiClient;

import static com.sunshine.metadata.provider.MetadataContract.Activities;

public class SharedStorageManager {

    private MetadataDBHandler dbHandler;

    public static final int RO_MODE = 0;
    public static final int WO_MODE = 1;

    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int ACTIVITIES_THUMBNAIL = 0;
    private static final int ACTIVITIES_VIDEO = 1;
    private static final int ACTIVITIES_GALLERY= 2;
    private static final int GALLERY_IMAGES_ID = 3;
    private FileStorage fileStorage;
    private Context context;

    static {
        sUriMatcher.addURI(MetadataContract.AUTHORITY, "activities/video/#", ACTIVITIES_VIDEO);
        sUriMatcher.addURI(MetadataContract.AUTHORITY, "activities/thumbnail/#", ACTIVITIES_THUMBNAIL);
        sUriMatcher.addURI(MetadataContract.AUTHORITY, "activities/gallery/images/#", GALLERY_IMAGES_ID);
    }

    public SharedStorageManager(Context context) {
        this.context = context;
        fileStorage = FileStorageManager.getInstance().getReadableFileStorage();
    }

    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case ACTIVITIES_VIDEO:
                return "video/mp4";
            default:
                return null;
        }
    }

    public void downloadActivity(Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor.moveToFirst()) {

            int id = Integer.parseInt(uri.getLastPathSegment());
            int type = cursor.getInt(cursor.getColumnIndex(Activities._TYPE));
            switch (type) {
                case Activities.TYPE_VIDEO:
                    new FileDownloadTask(context,
                            ApiClient.getDownloadUri("video_activity", id),
                            Activities.getActivityVideoUri(id)).execute();
                    break;
                default:
            }
        }
        cursor.close();
    }

    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        Log.i(context.toString(), "openFile Uri:" + uri.toString());
        switch (sUriMatcher.match(uri)) {
            case ACTIVITIES_VIDEO:
            case ACTIVITIES_GALLERY:
            case ACTIVITIES_THUMBNAIL:
                return getActivityDescriptor(uri, mode);
            default:
                throw new FileNotFoundException();
        }
    }

    private ParcelFileDescriptor getActivityDescriptor(Uri uri, String mode) throws FileNotFoundException {
        File directory = getActivityFileDirectory(uri);
        ParcelFileDescriptor descriptor;
        File file = new File(directory, uri.getLastPathSegment());

        switch(getMode(mode)) {
            case RO_MODE:
                return getReadOnlyDescriptor(file);
            case WO_MODE:
                return getWriteOnlyDescriptor(uri, file);
            default:
                Log.d(getClass().getName(), "Not a valid mode.");
                return null;
        }
    }

    private ParcelFileDescriptor getWriteOnlyDescriptor(Uri uri, File file) {
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_WRITE_ONLY);
        } catch (IOException e) {
            Log.e(getClass().getName(), "Failed to create file for " + uri, e);
            return null;
        }
    }

    private ParcelFileDescriptor getReadOnlyDescriptor(File file) throws FileNotFoundException {
        if (file.exists()) {
            return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        } else {
            throw new FileNotFoundException();
        }
    }

    private int getMode(String mode) {
        if (mode.equals(String.valueOf(RO_MODE))) {
            return RO_MODE;
        } else if (mode.equals(String.valueOf(WO_MODE))) {
            return WO_MODE;
        } else {
            return -1;
        }
    }

    private File getActivityFileDirectory(Uri uri) throws FileNotFoundException {
        String path = uri.getPath();
        String directoryPath = path.substring(0, path.lastIndexOf("/"));
        File directory = fileStorage.mkdir(directoryPath);
        if (directory.exists()) {
            return directory;
        } else {
            Log.e(getClass().getName(), "Could not create activity directory: " + directoryPath);
            throw new FileNotFoundException();
        }
    }
}
