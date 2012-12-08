package com.ssl.metadata.storage;

import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.ssl.metadata.database.tables.FileTable;
import com.ssl.metadata.provider.Matcher;
import com.ssl.metadata.provider.MetadataContract;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class SharedStorageManager {

    public static final int RO_MODE = 0;
    public static final int WO_MODE = 1;

    private static UriMatcher sUriMatcher = Matcher.Factory.getMatcher();

    private FileStorage mFileStorage;
    private FileTable mFileTable;
    private Context mContext;


    public SharedStorageManager(Context context, FileTable fileTable) {
        mContext = context;
        mFileTable = fileTable;
    }

    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
        	case Matcher.ACTIVITIES_AUDIO:
    		return "audio/mp3";
            case Matcher.ACTIVITIES_VIDEO:
                return "video/mp4";
            case Matcher.ACTIVITIES_TEXT:
                return "text";
            default:
                return null;
        }
    }

    public synchronized ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        Log.i(mContext.toString(), "openFile Uri:" + uri.toString());
        switch (sUriMatcher.match(uri)) {
        	case Matcher.ACTIVITIES_AUDIO:
            case Matcher.ACTIVITIES_VIDEO:
            case Matcher.ACTIVITIES_TEXT:
            case Matcher.ACTIVITIES_PDF:
            case Matcher.ACTIVITIES_HTML:
            case Matcher.ACTIVITIES_THUMBNAIL:
            case Matcher.SECTIONS_THUMBNAIL:
            case Matcher.GALLERY_IMAGES_ID:
            case Matcher.GALLERY_IMAGES_THUMBNAIL:
            case Matcher.BOOKS_THUMBNAIL:
            case Matcher.BOOK_COLLECTIONS_THUMBNAIL:
                return getFileDescriptor(uri, mode);
            case Matcher.BOOKS_ID:
                return getExtendedFileDescriptor(uri, mode);
            default:
                throw new FileNotFoundException();
        }
    }

    private ParcelFileDescriptor getExtendedFileDescriptor(Uri uri, String mode) throws FileNotFoundException {
        if (getMode(mode) == RO_MODE) {
            Cursor cursor = mFileTable.query(uri, null,
                    MetadataContract.Files._URI_PATH + "='?'", new String[] {uri.getPath()}, null);
            try {
                if (cursor.moveToFirst()) {
                    String filePath = cursor.getString(cursor.getColumnIndex(MetadataContract.Files._FILE_PATH));
                    cursor.close();
                    return getReadOnlyDescriptor(new File(filePath));
                }
            } catch (Exception e) {} finally {
                cursor.close();
            }
        }
        return getFileDescriptor(uri, mode);
    }

    private ParcelFileDescriptor getFileDescriptor(Uri uri, String mode) throws FileNotFoundException {
        File file = getFile(uri);
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

    private File getFile(Uri uri) throws FileNotFoundException {
        File directory = getFileDirectory(uri.getPath());
        return new File(directory, uri.getLastPathSegment());
    }

    private File getFileDirectory(String path) throws FileNotFoundException {
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
    	if (mFileStorage == null) {
    		mFileStorage = FileStorageManager.getInstance().getWritableFileStorage();
    	} 
    	return mFileStorage;
    }

    public String moveFile(String uriPath, String fileName) {
        File oldFile = getFileStorage().getFile(uriPath);
        File newFile = new File(getFileStorage().mkdir("files"), fileName);
        oldFile.renameTo(newFile);
        Log.i(getClass().getName(), String.format("Moving file from %s to %s.",
                oldFile.getAbsolutePath(), newFile.getAbsolutePath()));
        return newFile.getAbsolutePath();
    }
}

