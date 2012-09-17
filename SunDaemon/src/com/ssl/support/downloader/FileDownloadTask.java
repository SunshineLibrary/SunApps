package com.ssl.support.downloader;

import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.ssl.support.utils.IOUtils;
import com.ssl.support.utils.ListenableAsyncTask;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.*;

public class FileDownloadTask extends ListenableAsyncTask<Uri, Integer, Integer> {

    private HttpClient httpClient;
    Context context;
    private Uri remoteUri;

    Uri localUri;
    private long contentLength;

    public static final int SUCCESS = 0;
    public static final int FAILURE = 1;
    private static final int RETRY_COUNT = 3;

    public FileDownloadTask(Context context, Uri remoteUri, Uri localUri) {
        this.remoteUri = remoteUri;
        this.localUri = localUri;
        this.httpClient = new DefaultHttpClient();
        this.context = context;
    }

    @Override
    protected Integer doInBackground(Uri... uris) {
        int retryLimit = RETRY_COUNT;
        while (retryLimit-- > 0) {
            Log.d(getClass().getName(), "Requesting file: " + remoteUri);
            InputStream input = getInputStreamForUri(remoteUri);
            OutputStream output = getOutputStreamForUri(localUri);

            try {
                IOUtils.copy(input, output, new DownloadProgressUpdater(contentLength));
                return SUCCESS;
            } catch (IOException e) {
                Log.w(getClass().getName(), "Failed during download for uris." + remoteUri + "," + localUri);
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
            contentLength = Long.parseLong(response.getFirstHeader("Content-Length").getValue());
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

    public class DownloadProgressUpdater extends IOUtils.ProgressUpdater {
        private static final int PROGRESS_SAMPLE_RATE = 10;

        private long contentLength;
        private long lastProgress;

        public DownloadProgressUpdater(long contentLength) {
            this.contentLength = contentLength;
            this.lastProgress = 0 - PROGRESS_SAMPLE_RATE;
        }

        public void onProgressUpdate(long totalBytesProcessed) {
            if (totalBytesProcessed * 100 / contentLength >= lastProgress + PROGRESS_SAMPLE_RATE) {
                Integer[] progress = new Integer[]{(int) (totalBytesProcessed * 100 / contentLength)};
                FileDownloadTask.this.onProgressUpdate(progress);
                Log.d(getClass().getName(), String.format("Downloading[%d]: %s)", totalBytesProcessed * 100 / contentLength, remoteUri.toString()));
                lastProgress += PROGRESS_SAMPLE_RATE;
            }
        }
    }
}
