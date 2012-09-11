package com.sunshine.support.activities;

import android.app.Activity;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.ListView;
import com.sunshine.metadata.database.tables.APISyncStateTable;
import com.sunshine.support.R;
import com.sunshine.support.data.adapters.ApiSyncStateAdapter;

public class ViewMetadataUpdatesActivity extends Activity{

    private ListView lv_metadata_list;
    private ApiSyncStateAdapter mAdapter;
    private CursorLoader mLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.metadata_updates);

        intUI();
        loadData();

        getContentResolver().registerContentObserver(APISyncStateTable.ApiSyncStates.CONTENT_URI, false ,
                new MContentObserver(new Handler()));
    }

    private void intUI() {
        mAdapter = new ApiSyncStateAdapter(this, null);
        lv_metadata_list = (ListView) findViewById(R.id.lv_metadata_list);
        lv_metadata_list.setAdapter(mAdapter);
    }

    private void loadData() {
        mLoader = new CursorLoader(this,APISyncStateTable.ApiSyncStates.CONTENT_URI, null, null, null, null);

        mLoader.registerListener(0, new Loader.OnLoadCompleteListener<Cursor>() {
            public void onLoadComplete(Loader<Cursor> cursorLoader, Cursor cursor) {
                mAdapter.changeCursor(cursor);
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