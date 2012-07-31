package com.sunshine.support;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.Button;
import com.sunshine.metadata.database.tables.ActivityTable;
import com.sunshine.metadata.provider.MetadataContract;
import com.sunshine.support.api.ApiClient;
import com.sunshine.support.mock.ImageTestData;
import com.sunshine.support.storage.FileDownloadTask;
import com.sunshine.support.sync.APISyncReceiver;
import com.sunshine.support.webclient.WebClient;

import java.io.FileNotFoundException;
import java.text.Format;

import static com.sunshine.metadata.provider.MetadataContract.*;

public class SupportServiceActivity extends Activity {
    private PackageManager localPackageManager;
    private WebClient serverClient;
    private ImageTestData imageTestData;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(Activities._DOWNLOAD_STATUS, Downloadable.STATUS.QUEUED.ordinal());
                getContentResolver().update(Activities.CONTENT_URI, values, null, null);
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        startService(new Intent("com.sunshine.support.action.sync"));
    }

    private void prepareData(int id, int type) {
        ContentValues values = new ContentValues();
        values.put(Activities._ID, id);
        values.put(Activities._TYPE, type);
        Cursor cursor;
        if (!(cursor = getContentResolver().query(Activities.getActivityUri(id),
                ActivityTable.ALL_COLUMNS, null, null, null)).moveToFirst()) {
            getContentResolver().insert(Activities.CONTENT_URI, values);
        }
        cursor.close();
    }
}
