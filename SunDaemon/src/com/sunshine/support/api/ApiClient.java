package com.sunshine.support.api;

import java.util.HashMap;
import java.util.Map;

import android.content.IntentFilter;
import android.net.Uri;
import com.sunshine.metadata.database.tables.*;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

public class ApiClient {

    private static ThreadSafeClientConnManager connManager;
    private static final Uri ROOT_URI;
    private static final String HOST = "10.0.2.2:3000";

    private static final Map<String, String> apiMap;

    static {
        ROOT_URI = new Uri.Builder().scheme("http").encodedAuthority(HOST).build();
        apiMap = new HashMap<String, String>();
        apiMap.put(GalleryImageTable.TABLE_NAME, "images.json");
    }

    public static synchronized ThreadSafeClientConnManager getConnManager() {
        if (connManager == null) {
            SchemeRegistry registry = new DefaultHttpClient().getConnectionManager().getSchemeRegistry();
            HttpParams params = new DefaultHttpClient().getParams();
            connManager = new ThreadSafeClientConnManager(params, registry);
        }
        return connManager;
    }

    public static synchronized HttpClient newHttpClient() {
        return new DefaultHttpClient(getConnManager(), null);
    }

    public static String getSyncRequestUrl(String tableName, long lastUpdateTime) {
        return ROOT_URI.buildUpon().appendPath(getApiPath(tableName))
                .appendQueryParameter("timestamp", String.valueOf(lastUpdateTime)).build()
                .toString();
    }

    private static String getApiPath(String tableName) {
        String name = apiMap.get(tableName);
        return (name != null) ? name : tableName + ".json";
    }

    public static Uri getRootUri() {
        return ROOT_URI;
    }

    public static Uri getDownloadUri(String type, long id) {
        return ROOT_URI.buildUpon().appendPath("download")
                .appendQueryParameter("type", type).appendQueryParameter("id", String.valueOf(id)).build();
    }

    public static Uri getThumbnailUri(String type, int id) {
        return ROOT_URI.buildUpon().appendPath("thumbnail")
                .appendQueryParameter("type", type).appendQueryParameter("id", String.valueOf(id)).build();
    }
}
