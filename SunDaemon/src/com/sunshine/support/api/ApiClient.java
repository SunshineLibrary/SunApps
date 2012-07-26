package com.sunshine.support.api;

import java.util.HashMap;
import java.util.Map;

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
    private static final String HOST = "ssl-mock.herokuapp.com";


	private static final Map<String, String> apiMap;

    static {
        ROOT_URI = new Uri.Builder().scheme("http").authority(HOST).build();
        apiMap = new HashMap<String, String>();
        apiMap.put(CourseTable.TABLE_NAME, "courses.json");
        apiMap.put(ChapterTable.TABLE_NAME, "chapters.json");
        apiMap.put(LessonTable.TABLE_NAME, "lessons.json");
        apiMap.put(SectionTable.TABLE_NAME, "sections.json");
        apiMap.put(ActivityTable.TABLE_NAME, "activities.json");
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
        return apiMap.get(tableName);
    }
}
