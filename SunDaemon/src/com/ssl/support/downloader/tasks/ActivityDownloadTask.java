package com.ssl.support.downloader.tasks;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import com.ssl.metadata.provider.MetadataContract;
import com.ssl.support.api.ApiClient;
import com.ssl.support.api.ApiClientFactory;
import com.ssl.support.utils.JSONSerializable;

import static com.ssl.metadata.provider.MetadataContract.Activities;

public class ActivityDownloadTask extends DownloadTask implements JSONSerializable {

    private static String[] COLUMNS = {Activities._DOWNLOAD_STATUS, Activities._TYPE};

    private static int INVALID_TYPE = -999;

    private int activityType;
    private int activityStatus;
    private Uri activityUri, notifyUri;
    private ApiClient apiClient;


    public ActivityDownloadTask(Context context, int id) {
        super(context, id);
        activityType = INVALID_TYPE;
        activityStatus = Activities.STATUS_NOT_DOWNLOADED;
        apiClient = ApiClientFactory.newApiClient(context);
        activityUri = Activities.getActivityUri(mId);
    }

    public ActivityDownloadTask(Context context, int id, int type, int status, Uri notifyUri) {
        super(context, id);
        activityType = type;
        activityStatus = status;
        apiClient = ApiClientFactory.newApiClient(context);
        activityUri = Activities.getActivityUri(mId);
        this.notifyUri = notifyUri;
    }

    @Override
    protected int execute() {
        if (activityType == INVALID_TYPE) {
            loadActivityInfo();
        }
        if (activityStatus == MetadataContract.Downloadable.STATUS_DOWNLOADED) {
            return SUCCESS;
        }

        switch (activityType) {
        	case Activities.TYPE_AUDIO:
    		return downloadToLocalUri(Activities.getActivityAudioUri(mId));
            case Activities.TYPE_HTML:
                return downloadToLocalUri(Activities.getActivityHtmlUri(mId));
            case Activities.TYPE_PDF:
                return downloadToLocalUri(Activities.getActivityPdfUri(mId));
            case Activities.TYPE_VIDEO:
                return downloadToLocalUri(Activities.getActivityVideoUri(mId));
            case Activities.TYPE_QUIZ:
                return downloadQuiz();
            case Activities.TYPE_TEXT:
                return downloadToLocalUri(Activities.getActivityTextUri(mId));
            default:
                return FAILURE;
        }
    }

    private int downloadToLocalUri(Uri localUri) {
        FileDownloadTask task = new FileDownloadTask(mContext, apiClient.getDownloadUri("activities", mId), localUri);
        task.setDownloadProgressListener(new FileDownloadTask.DownloadProgressListener() {
            @Override
            public void onProgressUpdate(int progress) {
                updateProgress(progress);
            }
        });
        return getDownloadStatus(task.execute());
    }

    private int downloadQuiz() {
        return SUCCESS;
    }

    private int getDownloadStatus(int status) {
        if (status == FileDownloadTask.SUCCESS) {
            return SUCCESS;
        }
        return FAILURE;
    }

    @Override
    protected Uri getUpdateUri() {
        return activityUri;
    }

    @Override
    protected Uri getNotifyUri() {
        if (notifyUri != null) {
            return notifyUri;
        }
        return activityUri;
    }

    @Override
    protected int getType() {
        return TYPE_ACTIVITY;
    }

    private void loadActivityInfo() {
        Cursor cursor = mContext.getContentResolver().query(activityUri, COLUMNS, null, null, null);
        if (cursor.moveToFirst()) {
            activityType = cursor.getInt(cursor.getColumnIndex(Activities._TYPE));
            activityStatus = cursor.getInt(cursor.getColumnIndex(Activities._DOWNLOAD_STATUS));
        } else {
            Log.e(TAG, "Activity not found: " + mId);
        }
        cursor.close();
    }
}
