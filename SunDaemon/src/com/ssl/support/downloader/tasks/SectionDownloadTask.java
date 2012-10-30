package com.ssl.support.downloader.tasks;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import com.ssl.metadata.provider.MetadataContract;
import com.ssl.support.utils.JSONSerializable;
import org.json.JSONException;
import org.json.JSONObject;

import static com.ssl.metadata.provider.MetadataContract.Activities;

public class SectionDownloadTask extends DownloadTask{

    private static String[] COLUMNS = {Activities._ID, Activities._TYPE, Activities._DOWNLOAD_STATUS};

    private int sectionId;

    public SectionDownloadTask(Context context, int sectionId) {
        super(context, sectionId);
        this.sectionId = sectionId;
    }

    @Override
    protected int getType() {
        return TYPE_SECTION;
    }

    protected int execute() {
        int status = SUCCESS;
        Cursor cursor = mContext.getContentResolver().query(
                MetadataContract.Sections.getSectionActivitiesUri(sectionId), COLUMNS, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(Activities._ID));
                int type = cursor.getInt(cursor.getColumnIndex(Activities._TYPE));
                int activityStatus = cursor.getInt(cursor.getColumnIndex(Activities._DOWNLOAD_STATUS));
                ActivityDownloadTask task =
                        new ActivityDownloadTask(mContext, id, type, activityStatus, getNotifyUri());
                task.run();
                if (FAILURE == task.getResult()){
                    status = FAILURE;
                    break;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return status;
    }

    @Override
    protected Uri getUpdateUri() {
        return MetadataContract.Sections.getSectionUri(sectionId);
    }

    @Override
    protected Uri getNotifyUri() {
        return MetadataContract.Sections.getSectionUri(sectionId);
    }
}
