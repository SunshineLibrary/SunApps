package com.sunshine.support.manager.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import com.sunshine.metadata.database.MetadataDBHandlerFactory;
import com.sunshine.support.R;
import com.sunshine.support.data.adapters.ApiSyncStateAdapter;
import com.sunshine.support.data.daos.ApiSyncStateDao;

public class ViewMetadataUpdatesActivity extends Activity{

    private ListView lv_metadata_list;
    private ApiSyncStateDao syncStateDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.metadata_updates);
        intUI();
        loadData();
    }

    private void intUI() {
        lv_metadata_list = (ListView) findViewById(R.id.lv_metadata_list);
    }

    private void loadData() {
        syncStateDao = new ApiSyncStateDao(MetadataDBHandlerFactory.newMetadataDBHandler(this));
        lv_metadata_list.setAdapter(new ApiSyncStateAdapter(this, syncStateDao.getAllSyncStates()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        syncStateDao.close();
    }
}