package com.sunshine.support.storage;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import com.sunshine.support.api.ApiClient;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.File;
import java.io.FileNotFoundException;

public class FileDownloadTask extends AsyncTask<Uri, Integer, String> {

    private HttpClient httpClient;
    private Context context;
    private Uri remoteUri;
    private Uri localUri;

    public FileDownloadTask(Context context, Uri remoteUri, Uri localUri) {
        this.remoteUri = remoteUri;
        this.httpClient = ApiClient.newHttpClient();
        this.context = context;
    }

    @Override
    protected String doInBackground(Uri... uris) {
        HttpGet get = new HttpGet(remoteUri.toString());
        try {
            ParcelFileDescriptor output = context.getContentResolver().openFileDescriptor(localUri, "w");

        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return "path";
    }
}
