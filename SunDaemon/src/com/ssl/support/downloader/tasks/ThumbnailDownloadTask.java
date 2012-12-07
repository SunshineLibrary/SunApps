package com.ssl.support.downloader.tasks;

import android.content.Context;
import android.net.Uri;
import com.ssl.metadata.provider.MetadataContract;
import com.ssl.support.api.ApiClient;
import com.ssl.support.api.ApiClientFactory;
import com.ssl.support.utils.JSONSerializable;

public class ThumbnailDownloadTask extends DownloadTask implements JSONSerializable {

    private static String[] COLUMNS = {MetadataContract.Problems._ID};

    private static Uri URI = MetadataContract.Problems.CONTENT_URI;
    private ApiClient apiClient;
    private int mType;

    public ThumbnailDownloadTask(Context context, int type, int id) {
        super(context, id);
        apiClient = ApiClientFactory.newApiClient(context);
        mType = type;
    }

    @Override
    protected int getType() {
        return TYPE_PROBLEM;
    }

    @Override
    protected int execute() {
        switch (mType) {
            case TYPE_BOOK_THUMB:
                return fetchThumbnail("books",
                        MetadataContract.Books.getBookThumbnailUri(mId));
            case TYPE_BOOK_COLLECTION_THUMB:
                return fetchThumbnail("book_collections",
                        MetadataContract.BookCollections.getBookCollectionThumbnailUri(mId));
        }
        return FAILURE;
    }

    private int fetchThumbnail(String type, Uri localUri) {
        FileDownloadTask task = new FileDownloadTask(mContext, apiClient.getThumbnailUri(type, mId), localUri);
        return getDownloadStatus(task.execute());
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
