package com.sunshine.support.storage;

import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.provider.MetadataContract;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.util.Log;

public class SharedStorageProvider {

    private MetadataDBHandler dbHandler;

    public static final int RO_MODE = 0;
    public static final int WO_MODE = 1;

    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int ACTIVITIES_THUMBNAIL = 0;
    private static final int ACTIVITIES_VIDEO = 1;
    private static final int ACTIVITIES_GALLERY= 2;
    private FileStorage fileStorage;
    private Context context;

    static {
        sUriMatcher.addURI(MetadataContract.AUTHORITY, "activities/video/#", ACTIVITIES_VIDEO);
        sUriMatcher.addURI(MetadataContract.AUTHORITY, "activities/gallery/#", ACTIVITIES_GALLERY);
        sUriMatcher.addURI(MetadataContract.AUTHORITY, "activities/thumbnail/#", ACTIVITIES_THUMBNAIL);
    }

    public SharedStorageProvider(Context context) {
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
