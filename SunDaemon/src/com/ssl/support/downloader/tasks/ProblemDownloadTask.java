package com.ssl.support.downloader.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import com.ssl.metadata.provider.MetadataContract;
import com.ssl.support.api.ApiClient;
import com.ssl.support.api.ApiClientFactory;
import com.ssl.support.utils.JSONSerializable;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

public class ProblemDownloadTask extends DownloadTask implements JSONSerializable {

    private static String[] COLUMNS = {MetadataContract.Problems._ID};

    private static Uri URI = MetadataContract.Problems.CONTENT_URI;

    private ApiClient apiClient;
    private Uri problemUri;

    public ProblemDownloadTask(Context context, int id) {
        super(context, id);
        apiClient = ApiClientFactory.newApiClient(context);
        problemUri = MetadataContract.Books.getBookUri(mId);
    }

    @Override
    protected int getType() {
        FileDownloadWithContextTask task = new FileDownloadWithContextTask(
                mContext, apiClient.getDownloadUri("books", mId), problemUri);
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
            if (!uri.getPath().equals(problemUri.getPath())) {
                // We followed a redirect, so there must be a audio or image file available
                renameFile(uri.getLastPathSegment());
            }
        }
        return TYPE_PROBLEM;
    }

    private void renameFile(String fileName) {
        ContentValues values = new ContentValues();
        values.put(MetadataContract.Files._URI_PATH, problemUri.getPath());
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
    protected int execute() {
        int status = SUCCESS;
        return status;
    }

    @Override
    protected Uri getUpdateUri() {
        return null;
    }

    @Override
    protected Uri getNotifyUri() {
        return null;
    }
}
