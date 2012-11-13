package com.ssl.support.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.ssl.support.api.ApiClient;
import com.ssl.support.api.ApiClientFactory;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class JSONUtils {

    private static final String TAG = "JSONUtils";

    public static JSONObject fetchJSONObjectFromUri(ApiClient apiClient, HttpUriRequest request) {
        String result = fetchJSONStringFromUri(apiClient, request);
        try {
            return new JSONObject(result);
        } catch (JSONException e) {
            Log.e(TAG, String.format("Error parsing JSON String: %s", result), e);
        }
        return new JSONObject();
    }

    public static JSONArray fetchJSONArrayFromUri(ApiClient apiClient, HttpUriRequest request) {
        String result = fetchJSONStringFromUri(apiClient, request);
        try {
            return new JSONArray(result);
        } catch (JSONException e) {
            Log.e(TAG, String.format("Error parsing JSON String: %s", result), e);
        }
        return new JSONArray();
    }

    private static String fetchJSONStringFromUri(ApiClient apiClient, HttpUriRequest request) {
        Log.v(TAG, String.format("Starting Request: %s [%s]", request.getURI(), request.getMethod()));
        StringWriter writer = new StringWriter();
        String result = "No Response";

        try {
            HttpResponse response = getHttpClient(apiClient).execute(request);
            InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());

            IOUtils.copyCharacterStream(reader, writer);

            result = writer.toString();
        } catch (IOException e) {
            Log.e(TAG, String.format("Failed Request: %s [%s]", request.getURI(), request.getMethod()), e);
        }
        return result;
    }

    private static HttpClient getHttpClient(ApiClient apiClient) {
        if (apiClient == null) {
                return null;
        }
        return apiClient.newHttpClient();
    }
}
