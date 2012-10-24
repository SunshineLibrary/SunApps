package com.ssl.support.downloader;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.ssl.metadata.provider.MetadataContract;
import com.ssl.support.utils.JSONSerializable;
import org.json.JSONException;
import org.json.JSONObject;

public class MonitoredFileDownloadTask extends FileDownloadTask implements JSONSerializable {

    private static final ContentValues NOT_DOWNLOADED = new ContentValues();
    private static final ContentValues DOWNLOADING = new ContentValues();
    private static final ContentValues DOWNLOADED = new ContentValues();

    private static final String TAG = "MonitoredFileDownloadTask";
    private static final String LOCAL_URI = "localUri";
    private static final String REMOTE_URI = "remoteUri";
    private static final String UPDATE_URI = "updateUri";
    private static final String NOTIFY_URI = "notifyUri";

    private Uri updateUri;
    private Uri notifyUri;

    static {
        NOT_DOWNLOADED.put(MetadataContract.Downloadable._DOWNLOAD_STATUS, MetadataContract.Downloadable.STATUS_NOT_DOWNLOADED);
        DOWNLOADING.put(MetadataContract.Downloadable._DOWNLOAD_STATUS, MetadataContract.Downloadable.STATUS_DOWNLOADING);
        DOWNLOADED.put(MetadataContract.Downloadable._DOWNLOAD_STATUS, MetadataContract.Downloadable.STATUS_DOWNLOADED);
    }

    public MonitoredFileDownloadTask(Context context, Uri remoteUri, Uri localUri, Uri updateUri) {
        super(context, remoteUri, localUri);
        this.updateUri = updateUri;
    }

    public MonitoredFileDownloadTask(Context context, Uri remoteUri, Uri localUri, Uri updateUri, Uri notifyUri) {
        super(context, remoteUri, localUri);
        this.updateUri = updateUri;
        this.notifyUri = notifyUri;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (remoteUri == null || localUri == null) {
            onPostExecute(SUCCESS);
            cancel(true);
        } else {
            context.getContentResolver().update(updateUri, DOWNLOADING, null, null);
            context.getContentResolver().notifyChange(updateUri, null);
        }
    }

    @Override
    protected void onPostExecute(Integer status) {
        super.onPostExecute(status);
        if (status == SUCCESS) {
            context.getContentResolver().update(updateUri, DOWNLOADED, null, null);
        } else {
            context.getContentResolver().update(updateUri, NOT_DOWNLOADED, null, null);
        }
        if (notifyUri != null) {
            context.getContentResolver().notifyChange(notifyUri, null);
        } else {
            context.getContentResolver().notifyChange(updateUri, null);
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        context.getContentResolver().update(updateUri, downloadProgress(values[0]), null, null);
        if (notifyUri != null) {
            context.getContentResolver().notifyChange(notifyUri, null);
        } else {
            context.getContentResolver().notifyChange(updateUri, null);
        }
    }

    private ContentValues downloadProgress(int progress) {
        ContentValues values = new ContentValues();
        values.put(MetadataContract.Downloadable._DOWNLOAD_PROGRESS, progress);
        return values;
    }

    @Override
    public JSONObject toJSON() {
        return toJSON(remoteUri, localUri, updateUri, notifyUri);
    }

    public static JSONObject toJSON(Uri remoteUri, Uri localUri, Uri updateUri, Uri notifyUri) {
        JSONObject object = null;
        try {
            object = new JSONObject();
            putUri(object, LOCAL_URI, localUri);
            putUri(object, REMOTE_URI, remoteUri);
            putUri(object, UPDATE_URI, updateUri);
            putUri(object, NOTIFY_URI, notifyUri);
        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSONObject");
        }
        return object;
    }

    private static void putUri(JSONObject object, String key, Uri uri) throws JSONException {
        if (uri != null) {
            object.put(key, uri);
        } else {
            object.put(key, "");
        }
    }

    private static Uri parseUri(JSONObject object, String key) throws JSONException {
        String uriString = object.getString(key);
        if (uriString.equals("")) {
            return null;
        } else {
            return Uri.parse(uriString);
        }
    }

    public static class Factory implements JSONSerializable.Factory<MonitoredFileDownloadTask> {

        private Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @Override
        public MonitoredFileDownloadTask createNewFromJSON(JSONObject jsonObject) {
            try {
                Uri localUri= parseUri(jsonObject, LOCAL_URI);
                Uri remoteUri = parseUri(jsonObject, REMOTE_URI);
                Uri updateUri = parseUri(jsonObject, UPDATE_URI);
                Uri notifyUri = parseUri(jsonObject, NOTIFY_URI);
            	return new MonitoredFileDownloadTask(context, remoteUri, localUri, updateUri, notifyUri);
            } catch (JSONException e) {
                Log.e(getClass().getName(), "Error creating UninstallRequest from JSONObject: " + jsonObject.toString());
            }
            return null;
        }
    }
}
