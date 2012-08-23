package com.sunshine.support.manager.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import com.sunshine.metadata.database.DBHandler;
import com.sunshine.metadata.database.MetadataDBHandlerFactory;
import com.sunshine.support.R;
import com.sunshine.support.application.MessageApplication;
import com.sunshine.support.application.UIMessage;
import com.sunshine.support.application.UIMessageListener;
import com.sunshine.support.data.adapters.ApiSyncStateAdapter;
import com.sunshine.support.data.daos.ApiSyncStateDao;
import com.sunshine.support.data.models.ApiSyncState;

import java.util.List;

public class ViewMetadataUpdatesActivity extends Activity{

    private ListView lv_metadata_list;
    private UIMessageListener listener;

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
        listener = new MetadataListUpdateListener();
        DBHandler dbHandler = MetadataDBHandlerFactory.newMetadataDBHandler(ViewMetadataUpdatesActivity.this);
        ApiSyncStateDao syncStateDao = new ApiSyncStateDao(ViewMetadataUpdatesActivity.this, dbHandler);
        List<ApiSyncState> states = syncStateDao.getAllSyncStates();
        lv_metadata_list.setAdapter(new ApiSyncStateAdapter(ViewMetadataUpdatesActivity.this, states));
        ((MessageApplication) ViewMetadataUpdatesActivity.this.getApplication())
                .registerUIMessageListener(ApiSyncStateDao.ON_NEW_STATE, listener);
        ((MessageApplication) ViewMetadataUpdatesActivity.this.getApplication())
                .registerUIMessageListener(ApiSyncStateDao.ON_STATE_CHANGE, listener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class MetadataListUpdateListener implements UIMessageListener {

        @Override
        public void onUIMessageReceived(UIMessage msg) {
            ((ApiSyncStateAdapter) lv_metadata_list.getAdapter()).update((ApiSyncState) msg.getPayload());
        }
    }
}