package com.ssl.support.downloader.tasks;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.ssl.metadata.provider.MetadataContract;
import com.ssl.support.utils.JSONSerializable;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public abstract class DownloadTask implements JSONSerializable {

    public static final String DOWNLOAD_TYPE_KEY = "download_type";
    public static final String ID_KEY = "id";
    public static final int TYPE_SECTION = 1;
    public static final int TYPE_ACTIVITY = 2;
    public static final int TYPE_PROBLEM = 3;

    protected static final ContentValues NOT_DOWNLOADED = new ContentValues();
    protected static final ContentValues DOWNLOADING = new ContentValues();
    protected static final ContentValues DOWNLOADED = new ContentValues();
    protected static final String TAG = "DownloadTask";

    protected static final int SUCCESS = 1;
    protected static final int FAILURE = 0;

    protected Context mContext;
    protected int mResult;
    protected int mId;

    static {
        NOT_DOWNLOADED.put(MetadataContract.Downloadable._DOWNLOAD_STATUS, MetadataContract.Downloadable.STATUS_NOT_DOWNLOADED);
        DOWNLOADING.put(MetadataContract.Downloadable._DOWNLOAD_STATUS, MetadataContract.Downloadable.STATUS_DOWNLOADING);
        DOWNLOADED.put(MetadataContract.Downloadable._DOWNLOAD_STATUS, MetadataContract.Downloadable.STATUS_DOWNLOADED);
    }

    public DownloadTask(Context context, int id) {
        mContext = context;
        mId = id;
    }

    public void run() {
        int result = execute();
        setResult(execute());
    }

    public int getResult() {
        return mResult;
    }

    public JSONObject toJSON() {
        return toJSON(getType(), mId);
    }

    public static JSONObject toJSON(int type, int id) {
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(DOWNLOAD_TYPE_KEY, type);
            object.put(ID_KEY, id);
        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSONObject");
        }
        return object;
    }

    protected abstract int getType();

    protected abstract int execute();

    protected abstract Uri getUpdateUri();

    protected abstract Uri getNotifyUri();

    protected void updateProgress(int progress) {
        ContentResolver resolver = mContext.getContentResolver();
        resolver.update(getUpdateUri(), getProgressContentValues(progress), null, null);
        resolver.notifyChange(getNotifyUri(), null);
    }

    protected void setResult(int status) {
        Log.d(TAG, String.format("Task[type=%d] finished with status: %d", getType(), status));
        mResult = status;
        ContentResolver resolver = mContext.getContentResolver();
        resolver.update(getUpdateUri(), getStatusContentValues(status), null, null);
        resolver.notifyChange(getNotifyUri(), null);
    }

    private static ContentValues getProgressContentValues(int progress) {
        ContentValues values = new ContentValues();
        values.put(MetadataContract.Downloadable._DOWNLOAD_STATUS, MetadataContract.Downloadable.STATUS_DOWNLOADING);
        values.put(MetadataContract.Downloadable._DOWNLOAD_PROGRESS, progress);
        return values;
    }

    private static ContentValues getStatusContentValues(int status) {
        switch(status) {
            case SUCCESS:
                return DOWNLOADED;
            case FAILURE:
                return NOT_DOWNLOADED;
            default:
                return NOT_DOWNLOADED;
        }
    }
}
