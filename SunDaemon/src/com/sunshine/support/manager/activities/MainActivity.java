package com.sunshine.support.manager.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.sunshine.metadata.database.tables.ActivityTable;
import com.sunshine.support.R;

import static com.sunshine.metadata.provider.MetadataContract.Activities;

public class MainActivity extends Activity implements View.OnClickListener {
    private PackageManager localPackageManager;

    /**
     * Called when the activities is first created.
     */

    private TextView tvViewMetadataUpdates, tvViewAppUpdates, tvViewSystemConfigs;
    private Button btnUpdateMetadata, btnUpdateApp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initUI();
        initComponents();
    }

    private void initUI() {
        tvViewMetadataUpdates = (TextView) findViewById(R.id.tv_view_metadata_updates);
        tvViewAppUpdates = (TextView) findViewById(R.id.tv_view_app_updates);
        tvViewSystemConfigs = (TextView) findViewById(R.id.tv_view_system_config);
        btnUpdateApp = (Button) findViewById(R.id.btn_update_app);
        btnUpdateMetadata = (Button) findViewById(R.id.btn_update_metadata);
    }

    private void initComponents() {
        tvViewMetadataUpdates.setOnClickListener(this);
        tvViewAppUpdates.setOnClickListener(this);
        tvViewSystemConfigs.setOnClickListener(this);
        btnUpdateApp.setOnClickListener(this);
        btnUpdateMetadata.setOnClickListener(this);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.v(getClass().getName(), "Starting API sync service.");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_view_metadata_updates:
                startActivity(new Intent(this, ViewMetadataUpdatesActivity.class));
                break;
            case R.id.tv_view_app_updates:
                startActivity(new Intent(this, ViewMetadataUpdatesActivity.class));
                break;
            case R.id.tv_view_system_config:
                startActivity(new Intent(this, ViewMetadataUpdatesActivity.class));
                break;
            case R.id.btn_update_app:
                startService(new Intent("com.sunshine.support.action.update"));
                break;
            case R.id.btn_update_metadata:
                startService(new Intent("com.sunshine.support.action.sync"));
                break;
            default:
                return;
        }
    }
}