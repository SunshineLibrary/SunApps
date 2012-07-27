package com.sunshine.support.storage;

import android.content.Context;
import android.content.UriMatcher;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.provider.MetadataContract;

import java.io.File;
import java.io.FileNotFoundException;
import android.util.Log;
import com.sunshine.metadata.provider.MetadataProvider;

public class SharedStorageProvider {

    private MetadataDBHandler dbHandler;

    private static final int RO_MODE = 0;
    private static final int WO_MODE = 1;

    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int GALLERY_IMAGE = 0;
    private static final int GALLERY_THUMBNAIL = 1;
    private static final int ACTIVITIES_THUMBNAIL = 2;
    private static final int ACTIVITIES_FILE = 3;
    private FileStorage fileStorage;
    private Context context;

    private static final String GALLERY_IMAGE_MIME_TYPE = MetadataProvider.ITEM_MIME_TYPE
            + ".image";

    private static final String GALLERY_THUMBNAIL_MIME_TYPE = MetadataProvider.ITEM_MIME_TYPE
            + ".thumbnail";


    static {
        sUriMatcher.addURI(MetadataContract.AUTHORITY, "gallery/image/*", GALLERY_IMAGE);
        sUriMatcher.addURI(MetadataContract.AUTHORITY, "gallery/thumbnail/*", GALLERY_THUMBNAIL);
        sUriMatcher.addURI(MetadataContract.AUTHORITY, "activities/file/#", ACTIVITIES_FILE);
        sUriMatcher.addURI(MetadataContract.AUTHORITY, "activities/thumbnail/#", ACTIVITIES_THUMBNAIL);
    }

    public SharedStorageProvider(Context context) {
        this.context = context;
        fileStorage = FileStorageManager.getInstance().getReadableFileStorage();
    }

    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case GALLERY_IMAGE:
                return GALLERY_IMAGE_MIME_TYPE;
            case GALLERY_THUMBNAIL:
                return GALLERY_THUMBNAIL_MIME_TYPE;
            default:
                return null;
        }
    }

    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        Log.i(context.toString(), "openFile Uri:" + uri.toString());
        switch (sUriMatcher.match(uri)) {
            case GALLERY_IMAGE:
            case GALLERY_THUMBNAIL:
                return readImageFile(uri);
            default:
                throw new FileNotFoundException();
        }
    }


    private int getMode(String mode) {
        if (mode.equals(ParcelFileDescriptor.MODE_READ_ONLY)) {
            return RO_MODE;
        } else if (mode.equals(ParcelFileDescriptor.MODE_WRITE_ONLY)) {
            return WO_MODE;
        } else {
            return -1;
        }
    }

    private ParcelFileDescriptor readImageFile(Uri uri) {
        String path = uri.getPath();
        Log.i(context.toString(), "uri path=" + uri.getPath());
        String imagePath = trimPreviousSlash(path);
        File imageFile = fileStorage.readFile(imagePath);
        if (imageFile.exists()) {
            ParcelFileDescriptor open = null;
            try {
                open = ParcelFileDescriptor.open(imageFile, ParcelFileDescriptor.MODE_READ_ONLY);
            } catch (Exception e) {
                Log.i(context.toString(),"open parce file error.");
            }
            return open;
        }
        Log.i(context.toString(), "can not file path=" + path);
        return null;
    }

    private String trimPreviousSlash(String path) {
        String thumbnailPath = null;
        if (path.startsWith(File.separator)) {
            thumbnailPath = path.substring(path.indexOf(File.separator));
        }
        return thumbnailPath;
    }
}
