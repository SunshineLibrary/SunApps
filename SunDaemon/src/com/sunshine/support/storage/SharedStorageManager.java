package com.sunshine.support.storage;

import android.content.Context;
import android.content.UriMatcher;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.sunshine.metadata.provider.Matcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class SharedStorageManager {

    public static final int RO_MODE = 0;
    public static final int WO_MODE = 1;

    private static UriMatcher sUriMatcher = Matcher.Factory.getMatcher();

    private FileStorage fileStorage;
    private Context context;


    public SharedStorageManager(Context context) {
        this.context = context;
    }

    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case Matcher.ACTIVITIES_VIDEO:
                return "video/mp4";
            case Matcher.ACTIVITIES_TEXT:
                return "text";
            default:
                return null;
        }
    }

    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        Log.i(context.toString(), "openFile Uri:" + uri.toString());
        switch (sUriMatcher.match(uri)) {
            case Matcher.ACTIVITIES_VIDEO:
            case Matcher.ACTIVITIES_TEXT:
            case Matcher.ACTIVITIES_THUMBNAIL:
            case Matcher.GALLERY_IMAGES_ID:
            case Matcher.GALLERY_IMAGES_THUMBNAIL:
            case Matcher.BOOKS_ID:
            case Matcher.BOOKS_THUMBNAIL:
            case Matcher.BOOK_COLLECTIONS_THUMBNAIL:
                return getFileDescriptor(uri, mode);
            default:
                throw new FileNotFoundException();
        }
    }

    private ParcelFileDescriptor getFileDescriptor(Uri uri, String mode) throws FileNotFoundException {
        File directory = getFileDirectory(uri);
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
        if (mode.equalsIgnoreCase("r")) {
            return RO_MODE;
        } else if (mode.equalsIgnoreCase("w")) {
            return WO_MODE;
        } else {
            return -1;
        }
    }

    private File getFileDirectory(Uri uri) throws FileNotFoundException {
        String path = uri.getPath();
        String directoryPath = path.substring(0, path.lastIndexOf("/"));
        File directory = getFileStorage().mkdir(directoryPath);
        if (directory.exists()) {
            return directory;
        } else {
            Log.e(getClass().getName(), "Could not create file directory: " + directoryPath);
            throw new FileNotFoundException();
        }
    }
    
    private FileStorage getFileStorage() {
    	if (fileStorage == null) {
    		fileStorage = FileStorageManager.getInstance().getWritableFileStorage();
    	} 
    	return fileStorage;
    }
}
