package com.sunshine.support.api;

import android.net.Uri;
import com.sunshine.metadata.database.tables.GalleryImageTable;
import com.sunshine.support.config.Configurations;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.HashMap;
import java.util.Map;

public class ApiClient {

    private Uri apiServerUri;
    private Uri uploadServerUri;
    private Uri logServerUri;

    public ApiClient(Configurations configs) {
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
                .appendQueryParameter("timestamp", String.valueOf(lastUpdateTime)).build()
                .toString();
    }

    private String getApiPath(String tableName) {
        return tableName + ".json";
    }

    public Uri getDownloadUri(String type, long id) {
        return apiServerUri.buildUpon().appendPath("download").appendPath(type).appendPath(String.valueOf(id)).build();
    }

    public Uri getThumbnailUri(String type, int id) {
        return apiServerUri.buildUpon().appendPath("download").appendPath(type + "_thumb").appendPath(String.valueOf(id)).build();
    }

    public Uri getApkUpdateUri() {
        return apiServerUri.buildUpon().appendPath("apks").appendPath("get_updates").build();
    }
}