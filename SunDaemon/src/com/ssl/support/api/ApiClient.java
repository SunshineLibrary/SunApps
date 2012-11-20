package com.ssl.support.api;

import android.content.Context;
import android.net.Uri;
import com.ssl.support.config.AccessToken;
import com.ssl.support.config.Configurations;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

public class ApiClient {

    private Context mContext;
    private Configurations mConfigs;
    private String accessToken;

    public ApiClient(Context context, Configurations configs) {
        mContext = context;
        mConfigs = configs;
    }

    public HttpClient newHttpClient() {
        return new DefaultHttpClient();
    }

    public String getSyncRequestUrl(String tableName, long lastUpdateTime) {
        return getApiServerUri().buildUpon().appendPath("api").appendPath(getApiPath(tableName))
            .appendQueryParameter("timestamp", String.valueOf(lastUpdateTime / 1000))
            .appendQueryParameter("access_token", getAccessToken()).build()
            .toString();
    }

    private Uri getApiServerUri() {
        String host = mConfigs.getString(Configurations.API_SERVER_ADDRESS);
        return new Uri.Builder().scheme("http").encodedAuthority(host).build();
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
        return getApiServerUri().buildUpon().appendPath("download").appendPath(type).appendPath(String.valueOf(id))
            .appendQueryParameter("access_token", getAccessToken()).build();
    }

    public Uri getThumbnailUri(String type, int id) {
        return getApiServerUri().buildUpon().appendPath("download").appendPath(type + "_thumb").appendPath(String.valueOf(id)).build();
    }

    public Uri getApkUpdateUri() {
        return getApiServerUri().buildUpon().appendPath("apks").appendPath("get_updates")
            .appendQueryParameter("access_token", getAccessToken()).build();
    }

    public Uri getAllSchoolsUri() {
        return getApiServerUri().buildUpon().appendPath("schools").appendEncodedPath("get_all.json").build();
    }

    public Uri getLoginUri() {
        return getApiServerUri().buildUpon().appendPath("machines").appendPath("sign_in.json").build();
    }

    public Uri getUserRecordPostUri() {
        return getApiServerUri().buildUpon().appendPath("user_records").appendPath("batch_update.json").build();
    }
}
