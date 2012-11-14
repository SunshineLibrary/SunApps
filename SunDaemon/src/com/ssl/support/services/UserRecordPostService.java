package com.ssl.support.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import com.ssl.support.api.ApiClient;
import com.ssl.support.api.ApiClientFactory;
import com.ssl.support.api.record.UserRecordRequest;
import com.ssl.support.api.record.UserRecordRequestFactory;
import com.ssl.support.api.record.UserRecordRequestQueue;
import com.ssl.support.config.AccessToken;
import com.ssl.support.utils.JSONUtils;
import com.ssl.support.utils.ListenableAsyncTask;
import com.ssl.support.utils.Listener;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bowen Sun
 * @version 1.0
 */

public class UserRecordPostService extends Service {

    public static final String USER_RECORD_REQUEST_KEY = "user_record";

    private static final String TAG = "UserRecordPostService";
    private static final int BATCH_SIZE = 20;

    private UserRecordRequestQueue requestQueue;
    private PowerManager.WakeLock wakeLock;
    private boolean inProgress;
    private Listener<Boolean> startNextBatchListener;
    private ApiClient apiClient;
    private String mAccessToken;

    @Override
    public void onCreate() {
        super.onCreate();
        requestQueue = new UserRecordRequestQueue(this, new UserRecordRequestFactory());
        apiClient = ApiClientFactory.newApiClient(this);
        inProgress = false;

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());

        mAccessToken = AccessToken.getAccessToken(this);
        startNextBatchListener = new Listener<Boolean>() {
            @Override
            public void onResult(Boolean aBoolean) {
                requestQueue.popBatch(BATCH_SIZE);
                inProgress = false;
                startNextBatch();
            }
        };
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("DownloadService", intent.toString());
        if (intent.hasExtra(USER_RECORD_REQUEST_KEY)) {
            UserRecordRequest request = (UserRecordRequest) intent.getParcelableExtra(USER_RECORD_REQUEST_KEY);
            requestQueue.add(request);
            acquireLock();
        }
        startNextBatch();
        return START_STICKY;
    }

    private void startNextBatch() {
        if (!inProgress) {
            List<UserRecordRequest> requests = requestQueue.peekBatch(BATCH_SIZE);
            if (!requests.isEmpty()) {
                UserRecordPostTask task = new UserRecordPostTask(requests);
                task.addListener(startNextBatchListener);
                inProgress = true;
                task.execute();
            } else {
                stopSelf();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseLock();
        Log.d("DownloadService", "Stopping");
    }

    private void acquireLock() {
        if (!wakeLock.isHeld()) {
            wakeLock.acquire();
        }
    }

    private void releaseLock() {
        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
    }

    private class UserRecordPostTask extends ListenableAsyncTask<Object, Object, Boolean> {

        private List<UserRecordRequest> mRequests;

        public UserRecordPostTask(List<UserRecordRequest> requests) {
            mRequests = requests;
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            HttpPost post = new HttpPost(apiClient.getUserRecordPostUri().toString());
            ArrayList<BasicNameValuePair> postParams= new ArrayList<BasicNameValuePair>();
            postParams.add(new BasicNameValuePair("records", new JSONArray(mRequests).toString()));
            postParams.add(new BasicNameValuePair("access_token", mAccessToken));
            try {
                post.setEntity(new UrlEncodedFormEntity(postParams, "utf8"));
            } catch (UnsupportedEncodingException e) { /* Pray this won't happen */}
            JSONObject response = JSONUtils.fetchJSONObjectFromUri(apiClient, post);
            try {
                if ("200".equals(response.getString("status"))) {
                    return true;
                } else {
                    Log.d(TAG, "Error Message: " + response.getString("message"));
                }
            } catch (JSONException e) {
                Log.e(TAG,  "Error parsing response: " + response, e);
            }
            return false;
        }
    }
}
