package com.ssl.support.api;

import android.content.Context;
import android.net.Uri;
import com.ssl.support.config.AccessToken;
import com.ssl.support.config.Configurations;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.net.URI;

public class ApiClient {

    private Context mContext;
    private Uri apiServerUri;
    private Uri uploadServerUri;
    private Uri logServerUri;
    private String accessToken;

    public ApiClient(Context context, Configurations configs) {
        mContext = context;

        String host = configs.getString(Configurations.API_SERVER_ADDRESS);
        apiServerUri = new Uri.Builder().scheme("http").encodedAuthority(host).build();

        host = configs.getString(Configurations.UPLOAD_SERVER_ADDRESS);
        uploadServerUri = new Uri.Builder().scheme("http").encodedAuthority(host).build();

        host = configs.getString(Configurations.LOG_SERVER_ADDRESS);
        logServerUri = new Uri.Builder().scheme("http").encodedAuthority(host).build();
    }

    public HttpClient newHttpClient() {
        return new DefaultHttpClient();
    }

    public String getSyncRequestUrl(String tableName, long lastUpdateTime) {
        return apiServerUri.buildUpon().appendPath("api").appendPath(getApiPath(tableName))
            .appendQueryParameter("timestamp", String.valueOf(lastUpdateTime / 1000))
            .appendQueryParameter("access_token", getAccessToken()).build()
            .toString();
    }

    private String getAccessToken() {
        if (accessToken == null) {
            accessToken = AccessToken.getAccessToken(mContext);
        }
        return accessToken;
    }

    private String getApiPath(String tableName) {
        return tableName + ".json";
    }

    public Uri getDownloadUri(String type, long id) {
        return apiServerUri.buildUpon().appendPath("download").appendPath(type).appendPath(String.valueOf(id))
            .appendQueryParameter("access_token", getAccessToken()).build();
    }

    public Uri getThumbnailUri(String type, int id) {
        return apiServerUri.buildUpon().appendPath("download").appendPath(type + "_thumb").appendPath(String.valueOf(id)).build();
    }

    public Uri getApkUpdateUri() {
        return apiServerUri.buildUpon().appendPath("apks").appendPath("get_updates")
            .appendQueryParameter("access_token", getAccessToken()).build();
    }

    public Uri getAllSchoolsUri() {
        return apiServerUri.buildUpon().appendPath("schools").appendEncodedPath("get_all.json").build();
    }

    public Uri getLoginUri() {
        return apiServerUri.buildUpon().appendPath("machines").appendPath("sign_in.json").build();
    }

    public Uri getUserRecordPostUri() {
        return apiServerUri.buildUpon().appendPath("user_records").appendPath("batch_update.json").build();
    }
}
