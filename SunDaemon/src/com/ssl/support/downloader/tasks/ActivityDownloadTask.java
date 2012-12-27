package com.ssl.support.downloader.tasks;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import com.ssl.metadata.provider.MetadataContract;
import com.ssl.support.api.ApiClient;
import com.ssl.support.api.ApiClientFactory;
import com.ssl.support.utils.JSONSerializable;

import java.util.ArrayList;
import java.util.List;

import static com.ssl.metadata.provider.MetadataContract.*;

public class ActivityDownloadTask extends DownloadTask implements JSONSerializable {

    private static final String[] COLUMNS = {Activities._DOWNLOAD_STATUS, Activities._TYPE};
    private static final String[] PROBLEM_COLUMNS = {QuizComponents._PROBLEM_ID};

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
        int status = SUCCESS;
        Cursor cursor = mContext.getContentResolver().query(QuizComponents.PROBLEMS_URI, PROBLEM_COLUMNS,
                QuizComponents._QUIZ_ACTIVITY_ID + "=" + mId, null, null);
        List<Integer> ids = getProblemIds(cursor);

        int size = ids.size();
        for (int i = 0; i < size; i ++) {
            int id = ids.get(i);
            ProblemDownloadTask task = new ProblemDownloadTask(mContext, id);
            task.run();
            if (FAILURE == task.getResult()){
                status = FAILURE;
                break;
            }
            updateProgress((i+1) * 100 / size);
        }

        return status;

    }

    private List<Integer> getProblemIds(Cursor cursor){
        List<Integer> idList = new ArrayList<Integer>();
        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(QuizComponents._PROBLEM_ID));
                idList.add(id);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return idList;
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
