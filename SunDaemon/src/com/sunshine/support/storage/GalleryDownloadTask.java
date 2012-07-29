package com.sunshine.support.storage;

import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.*;
import java.util.zip.ZipInputStream;

public class GalleryDownloadTask extends FileDownloadTask {
    private int gallery_id;

    public GalleryDownloadTask(Context context, Uri remoteUri, Uri localUri) {
        super(context, remoteUri, localUri);
        this.gallery_id = Integer.parseInt(localUri.getLastPathSegment());
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        if (result == SUCCESS) {
            ZipInputStream input = getGalleryStream(localUri);

            int bufferSize = 4096;
            byte[] buffer = new byte[bufferSize];
            int count, offset;
            long total;


        }
    }

    private ZipInputStream getGalleryStream(Uri uri) {
        try {
            ParcelFileDescriptor fid = null;
            fid = context.getContentResolver().openFileDescriptor(uri, String.valueOf(SharedStorageProvider.RO_MODE));
            return new ZipInputStream(new BufferedInputStream(new FileInputStream(fid.getFileDescriptor())));
        } catch (FileNotFoundException e) {
            Log.e(getClass().getName(), "Unable to read gallery file: " + uri, e);
            return null;
        }
    }
}
