package com.sunshine.support.storage;

import android.os.Environment;

public class FileStorageManager {
    private static FileStorageManager storageManager;

    private boolean mExternalStorageAvailable = false;
    private boolean mExternalStorageWritable = false;
    private ExternalFileStorage externalFileStorage;

    private FileStorageManager() {
        detectExternalStorage();
    }

    private ExternalFileStorage getExternalStorage() {
        if (externalFileStorage == null) {
            externalFileStorage = new ExternalFileStorage();
        }
        return externalFileStorage;
    }

    public static FileStorageManager getInstance() {
        if (storageManager == null) {
            storageManager = new FileStorageManager();
        }
        return storageManager;
    }

    public FileStorage getReadableFileStorage() {
        if (!mExternalStorageAvailable) return null;
        return getExternalStorage();
    }

    public FileStorage getWritableFileStorage() {
        if(!mExternalStorageWritable) return null;
        return getExternalStorage();
    }

    private void detectExternalStorage() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mExternalStorageAvailable = mExternalStorageWritable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            mExternalStorageAvailable = true;
            mExternalStorageWritable = false;
        } else {
            mExternalStorageAvailable = mExternalStorageWritable = false;
        }
    }
}
