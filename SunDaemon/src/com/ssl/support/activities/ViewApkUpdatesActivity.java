package com.ssl.support.activities;

import android.app.Activity;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.ListView;
import com.ssl.metadata.provider.MetadataContract;
import com.ssl.support.data.adapters.PackageAdapter;
import com.ssl.support.daemon.R;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class ViewApkUpdatesActivity extends Activity {

    private ListView lv_apk_list;
    private PackageAdapter mAdapter;
    private CursorLoader mLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apk_updates);
        initUI();
        loadData();

        getContentResolver().registerContentObserver(MetadataContract.Packages.CONTENT_URI, false,
                new MContentObserver(new Handler()));
    }

    private void initUI() {
        mAdapter = new PackageAdapter(this, null);
        lv_apk_list = (ListView) findViewById(R.id.lv_apk_list);
        lv_apk_list.setAdapter(mAdapter);
    }

    private void loadData() {
        mLoader = new CursorLoader(this, MetadataContract.Packages.CONTENT_URI, null, null, null, null);
        mLoader.registerListener(0, new Loader.OnLoadCompleteListener<Cursor>() {
            @Override
            public void onLoadComplete(Loader<Cursor> cursorLoader, Cursor data) {
                mAdapter.changeCursor(data);
            }
        });
        mLoader.startLoading();
    }

    private class MContentObserver extends ContentObserver {

        public MContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            loadData();
        }
    }
}
