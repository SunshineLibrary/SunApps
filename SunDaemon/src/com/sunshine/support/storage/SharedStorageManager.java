package com.sunshine.support.storage;

import android.content.Context;
import android.content.UriMatcher;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.provider.Matcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class SharedStorageManager {

    private MetadataDBHandler dbHandler;

    public static final int RO_MODE = 0;
    public static final int WO_MODE = 1;

    private static UriMatcher sUriMatcher = Matcher.Factory.getMatcher();

    private FileStorage fileStorage;
    private Context context;


    public SharedStorageManager(Context context) {
        this.context = context;
        fileStorage = FileStorageManager.getInstance().getReadableFileStorage();
    }

    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case Matcher.ACTIVITIES_VIDEO:
                return "video/mp4";
            default:
                return null;
        }
    }

    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        Log.i(context.toString(), "openFile Uri:" + uri.toString());
        switch (sUriMatcher.match(uri)) {
            case Matcher.ACTIVITIES_VIDEO:
            case Matcher.ACTIVITIES_THUMBNAIL:
            case Matcher.GALLERY_IMAGES_ID:
            case Matcher.GALLERY_IMAGES_THUMBNAIL:
                return getFileDescriptor(uri, mode);
            default:
                return getFileDescriptor(uri, mode);
        }
    }

    private ParcelFileDescriptor getFileDescriptor(Uri uri, String mode) throws FileNotFoundException {
        System.out.println("-------------------uri = " + uri.toString());
        File directory = getFileDirectory(uri);
        ParcelFileDescriptor descriptor;
        File file = new File(directory, uri.getLastPathSegment());

        System.out.println("-------------file = " + file.getAbsolutePath());

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
            System.out.println("------------writing file:" + uri);
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
        System.out.println("------------reading file:" + file.getAbsolutePath());
        if (file.exists()) {
            System.out.println("------------returning descriptor.");
            return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        } else {
            System.out.println("------------file not found.");
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
        File directory = fileStorage.mkdir(directoryPath);
        if (directory.exists()) {
            return directory;
        } else {
            Log.e(getClass().getName(), "Could not create file directory: " + directoryPath);
            throw new FileNotFoundException();
        }
    }
}
