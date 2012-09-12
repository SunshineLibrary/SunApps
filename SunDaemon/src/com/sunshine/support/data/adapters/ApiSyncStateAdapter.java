package com.sunshine.support.data.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.sunshine.metadata.database.tables.APISyncStateTable;
import com.sunshine.support.R;
import com.sunshine.support.data.helpers.ApiSyncStateHelper;
import com.sunshine.support.data.models.ApiSyncState;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class ApiSyncStateAdapter extends CursorAdapter {

    private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ApiSyncStateAdapter(Context context, Cursor c) {
        super(context, c, false);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newView = inflater.inflate(R.layout.metadata_list_item, parent, false);
        bindView(newView, context, cursor);
        return newView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv_table_name = (TextView) view.findViewById(R.id.tv_table_name);
        TextView tv_last_sync = (TextView) view.findViewById(R.id.tv_table_last_sync);
        TextView tv_last_update = (TextView) view.findViewById(R.id.tv_table_last_update);
        TextView tv_last_sync_status = (TextView) view.findViewById(R.id.tv_table_last_sync_status);

        ApiSyncState state = ApiSyncStateHelper.newEntryFromCursor(cursor);
        tv_table_name.setText(state.tableName);
        tv_last_update.setText(format.format(new Date(state.lastUpdateTime)));
        tv_last_sync.setText(format.format(new Date(state.lastSyncTime)));

        switch (state.lastSyncStatus) {
            case APISyncStateTable.ApiSyncStates.SYNC_SUCCESS:
                tv_last_sync_status.setTextColor(Color.GREEN);
                tv_last_sync_status.setText("SUCCESS");
                break;
            case APISyncStateTable.ApiSyncStates.SYNC_ONGOING:
                tv_last_sync_status.setTextColor(Color.YELLOW);
                tv_last_sync_status.setText("SYNCING");
                break;
            case APISyncStateTable.ApiSyncStates.SYNC_FAILURE:
                tv_last_sync_status.setTextColor(Color.RED);
                tv_last_sync_status.setText("FAILURE");
                break;
        }
        return;
    }
}
