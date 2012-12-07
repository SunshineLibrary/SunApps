package com.ssl.support.downloader.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import com.ssl.metadata.provider.MetadataContract;
import com.ssl.support.api.ApiClient;
import com.ssl.support.api.ApiClientFactory;
import com.ssl.support.utils.JSONSerializable;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import static com.ssl.metadata.provider.MetadataContract.Activities;

public class BookDownloadTask extends DownloadTask implements JSONSerializable {

    private static int INVALID_TYPE = -999;

    private ApiClient apiClient;
    private Uri bookUri;

    public BookDownloadTask(Context context, int id) {
        super(context, id);
        apiClient = ApiClientFactory.newApiClient(context);
        bookUri = MetadataContract.Books.getBookUri(mId);
    }

    @Override
    protected int execute() {
        FileDownloadWithContextTask task = new FileDownloadWithContextTask(
                mContext, apiClient.getDownloadUri("books", mId), bookUri);
        task.setDownloadProgressListener(new FileDownloadTask.DownloadProgressListener() {
            @Override
            public void onProgressUpdate(int progress) {
                updateProgress(progress);
            }
        });

        int status = getDownloadStatus(task.execute());
        if (status == SUCCESS) {
            HttpContext httpContext = task.getHttpContext();
            HttpUriRequest request = (HttpUriRequest) httpContext.getAttribute(ExecutionContext.HTTP_REQUEST);

            Uri uri = Uri.parse(request.getURI().toString());
            updateFileName(uri.getLastPathSegment());
        }
        return status;
    }

    private void updateFileName(String fileName) {
        ContentValues values = new ContentValues();
        values.put(MetadataContract.Files._URI_PATH, bookUri.getPath());
        values.put(MetadataContract.Files._FILE_PATH, fileName);
        mContext.getContentResolver().insert(MetadataContract.Files.CONTENT_URI, values);
    }

    private int getDownloadStatus(int status) {
        if (status == FileDownloadTask.SUCCESS) {
            return SUCCESS;
        }
        return FAILURE;
    }

    @Override
    protected Uri getUpdateUri() {
        return bookUri;
    }

    @Override
    protected Uri getNotifyUri() {
        return bookUri;
    }

    @Override
    protected int getType() {
        return DownloadTask.TYPE_BOOK;
    }
}
