package com.ssl.support.activities;

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
import com.ssl.support.services.UpdateService;
import com.ssl.metadata.database.tables.ActivityTable;
import com.ssl.support.daemon.R;
import com.ssl.support.services.APISyncService;

import static com.ssl.metadata.provider.MetadataContract.Activities;

public class MainActivity extends Activity implements View.OnClickListener {
    private PackageManager localPackageManager;

    private static final int SIGN_IN_REQUEST = 100;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_REQUEST && resultCode != RESULT_OK) {
            finish();
        }
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
        startActivityForResult(new Intent(this, SignInActivity.class), SIGN_IN_REQUEST);
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
                startActivity(new Intent(this, ViewApkUpdatesActivity.class));
                break;
            case R.id.tv_view_system_config:
                startActivity(new Intent(this, SystemConfigsActivity.class));
                break;
            case R.id.btn_update_app:
                startService(new Intent(this, UpdateService.class));
                break;
            case R.id.btn_update_metadata:
                startService(new Intent(this, APISyncService.class));
                break;
            default:
                return;
        }
    }
}