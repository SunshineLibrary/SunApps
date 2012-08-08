package com.sunshine.support.downloader;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.sunshine.support.storage.SharedStorageManager;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileDownloadTask extends AsyncTask<Uri, Integer, Integer> {

    private HttpClient httpClient;
    Context context;
    private Uri remoteUri;
    Uri localUri;
    private long contentLength;
    private long lastProgress;
    private boolean extractFiles;
    private MessageDigest digest;

    public static final int SUCCESS = 0;
    public static final int FAILURE = 1;
    private static final int PROGRESS_SAMPLE_RATE = 20;
    private static final int RETRY_COUNT = 3;

    public FileDownloadTask(Context context, Uri remoteUri, Uri localUri) {
        this.remoteUri = remoteUri;
        this.localUri = localUri;
        this.httpClient = new DefaultHttpClient();
        this.context = context;
        this.lastProgress = 0 - PROGRESS_SAMPLE_RATE;
    }

    @Override
    protected Integer doInBackground(Uri... uris) {
        int count, offset;
        int bufferSize = 1024;
        long total;
        byte[] buffer = new byte[1024];

        int retryLimit = RETRY_COUNT;
        while (retryLimit-- > 0) {
            Log.d(getClass().getName(), "Requesting file: " + remoteUri);
            InputStream input = getInputStreamForUri(remoteUri);
            OutputStream output = getOutputStreamForUri(localUri);

            try {
                if (input == null || output == null) {
                    throw new IOException();
                }

                total = 0;
                count = offset = 0;
                while ((count = input.read(buffer, offset, bufferSize - offset)) > 0) {
                    output.write(buffer, offset, count);
                    offset = (offset + count) % bufferSize;
                    total += count;
                    if (total * 100 / contentLength >= lastProgress + PROGRESS_SAMPLE_RATE) {
                        Log.d(getClass().getName(), String.format("Downloading[%d]: %s)", total * 100 / contentLength, remoteUri.toString()));
                        lastProgress += PROGRESS_SAMPLE_RATE;
                    }
                }
                input.close();
                output.close();
                return SUCCESS;
            } catch (IOException e) {
                Log.w(getClass().getName(), "Failed during download for uris." + remoteUri + "," + localUri);
                try {
                    if (input != null) input.close();
                    if (output != null) output.close();
                } catch (IOException e1) {}

                continue;
            }
        }
        return FAILURE;
    }

    private InputStream getInputStreamForUri(Uri uri) {
        HttpGet get = new HttpGet(uri.toString());
        HttpResponse response;
        try {
            response = httpClient.execute(get);
            this.contentLength = Long.parseLong(response.getFirstHeader("Content-Length").getValue());
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                Log.w(getClass().getName(), "Bad HTTP Response: SC " + response.getStatusLine().getStatusCode());
                get.abort();
                return null;
            } else {
                return response.getEntity().getContent();
            }
        } catch (IOException e) {
            get.abort();
            Log.e(getClass().getName(), "Failed to download file for " + uri, e);
            return null;
        }
    }

    private OutputStream getOutputStreamForUri(Uri uri) {
        try {
            Log.d(getClass().getName(), "Accessing local uri: " + uri);
            ParcelFileDescriptor fid = context.getContentResolver().openFileDescriptor(uri, "w");
            if (fid != null)
                return new FileOutputStream(fid.getFileDescriptor());
        } catch (FileNotFoundException e) {
            Log.e(getClass().getName(), "Failed to open file for writing");
        }
        return null;
    }

    private MessageDigest getDigest() {
        try {
            return MessageDigest.getInstance("md5");
        } catch (NoSuchAlgorithmException e) {
            Log.e(getClass().getName(), "Could not find md5 digest algorithm.");
            return null;
        }
    }
}
