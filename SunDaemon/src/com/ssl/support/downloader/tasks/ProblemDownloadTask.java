package com.ssl.support.downloader.tasks;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import com.ssl.metadata.provider.MetadataContract;
import com.ssl.support.utils.JSONSerializable;
import org.json.JSONException;
import org.json.JSONObject;

public class ProblemDownloadTask extends DownloadTask implements JSONSerializable {

    private static String[] COLUMNS = {MetadataContract.Problems._ID};

    private static Uri URI = MetadataContract.Problems.CONTENT_URI;

    public ProblemDownloadTask(Context context, int id) {
        super(context, id);
    }

    @Override
    protected int getType() {
        return TYPE_PROBLEM;
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
