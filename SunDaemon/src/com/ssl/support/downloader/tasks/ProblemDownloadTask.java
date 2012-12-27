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

    private ApiClient apiClient;
    private Uri problemUri;
    private Uri downloadUri;

    public ProblemDownloadTask(Context context, int id) {
        super(context, id);
        apiClient = ApiClientFactory.newApiClient(context);
        problemUri = MetadataContract.Problems.getProblemUri(mId);
        downloadUri = apiClient.getDownloadUri("problems", mId);
    }

    @Override
    protected int execute() {
        FileDownloadWithContextTask task = new FileDownloadWithContextTask( mContext, downloadUri, problemUri);

        int status = getDownloadStatus(task.execute());
        if (status == SUCCESS) {
            HttpContext httpContext = task.getHttpContext();
            HttpUriRequest request = (HttpUriRequest) httpContext.getAttribute(ExecutionContext.HTTP_REQUEST);
            Uri uri = Uri.parse(request.getURI().toString());
            if (!uri.getPath().equals(downloadUri.getPath())) {
                // We followed a redirect, so there must be a audio or image file available
                renameFile(uri.getLastPathSegment());
            }
        }
        return status;
    }

    @Override
    protected int getType() {
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
    protected Uri getUpdateUri() {
        return null;
    }

    @Override
    protected Uri getNotifyUri() {
        return null;
    }
}
