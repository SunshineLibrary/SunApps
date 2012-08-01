package com.sunshine.support;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.sunshine.metadata.database.tables.ActivityTable;

import static com.sunshine.metadata.provider.MetadataContract.Activities;
import static com.sunshine.metadata.provider.MetadataContract.Downloadable;

public class SupportServiceActivity extends Activity {
    private PackageManager localPackageManager;

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
