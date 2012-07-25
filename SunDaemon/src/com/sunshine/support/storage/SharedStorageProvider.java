package com.sunshine.support.storage;

import android.content.UriMatcher;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.provider.MetadataContract;

import java.io.FileNotFoundException;

public class SharedStorageProvider {

    private MetadataDBHandler dbHandler;

    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int PACKAGE_ID = 1;

    static {
        sUriMatcher.addURI(MetadataContract.AUTHORITY, "packages/#", PACKAGE_ID);
    }

    public SharedStorageProvider(MetadataDBHandler dbHandler) {
        this.dbHandler = dbHandler;
    }


    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {

        return null;
    }
}
