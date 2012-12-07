package com.ssl.support.downloader.tasks;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import java.io.InputStream;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class FileDownloadWithContextTask extends FileDownloadTask{

    private HttpContext mHttpContext;

    public FileDownloadWithContextTask(Context context, Uri remoteUri, Uri localUri) {
        super(context, remoteUri, localUri);
    }

    @Override
    protected InputStream getInputStreamForUri(Uri uri) {
        HttpGet get = new HttpGet(uri.toString());
        mHttpContext = new BasicHttpContext();
        HttpResponse response;
        try {
            response = httpClient.execute(get, mHttpContext);
            contentLength = 10000;

            Header header;
            if ((header = response.getFirstHeader("Content-Length")) != null){
                contentLength = Long.parseLong(header.getValue());
            }

            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                Log.w(getClass().getName(), "Bad HTTP Response: SC " + response.getStatusLine().getStatusCode());
                get.abort();
                return null;
            } else {
                return response.getEntity().getContent();
            }
        } catch (Exception e) {
            get.abort();
            Log.e(getClass().getName(), "Failed to download file for " + uri, e);
            return null;
        }
    }

    public HttpContext getHttpContext() {
        return mHttpContext;
    }
}
