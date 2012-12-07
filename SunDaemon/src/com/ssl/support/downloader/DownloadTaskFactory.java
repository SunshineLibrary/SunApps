package com.ssl.support.downloader;

import android.content.Context;
import android.util.Log;
import com.ssl.support.downloader.tasks.*;
import com.ssl.support.utils.JSONSerializable;
import org.json.JSONException;
import org.json.JSONObject;

import static com.ssl.support.downloader.tasks.DownloadTask.*;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class DownloadTaskFactory implements JSONSerializable.Factory<DownloadTask> {

    private static final String TAG = "DownloadTaskFactory";

    private Context mContext;

    public DownloadTaskFactory(Context context) {
        mContext = context;
    }

    @Override
    public DownloadTask createNewFromJSON(JSONObject jsonObject) {
        try {
            int type = jsonObject.getInt(DOWNLOAD_TYPE_KEY);
            int id = jsonObject.getInt(ID_KEY);
            switch(type) {
                case TYPE_SECTION:
                    return new SectionDownloadTask(mContext, id);
                case TYPE_ACTIVITY:
                    return new ActivityDownloadTask(mContext, id);
                case TYPE_PROBLEM:
                    return new ProblemDownloadTask(mContext, id);
                case TYPE_BOOK:
                    return new BookDownloadTask(mContext, id);
                case TYPE_BOOK_THUMB:
                case TYPE_BOOK_COLLECTION_THUMB:
                    return new ThumbnailDownloadTask(mContext, type, id);

            }
        } catch (JSONException e) {
            Log.e(TAG, "Failed creating DownloadTask from json: " + jsonObject.toString(), e);
        }
        return null;
    }
}
