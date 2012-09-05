package com.sunshine.support.data.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.sunshine.metadata.database.tables.APISyncStateTable;
import com.sunshine.support.R;
import com.sunshine.support.data.models.ApiSyncState;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class ApiSyncStateAdapter extends BaseAdapter {


    private List<ApiSyncState> states;
    private Context context;

    private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ApiSyncStateAdapter(Context context, List<ApiSyncState> states) {
        this.context = context;
        this.states = states;
    }

    public void update(ApiSyncState state) {
        int index = states.indexOf(state);
        if (index >= 0) {
            states.set(index, state);
        } else {
            states.add(state);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return states.size();
    }

    @Override
    public Object getItem(int position) {
        return states.get(position);
    }

    @Override
    public long getItemId(int position) {
        return states.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getNewView(position, parent);
    }

    private View getNewView(int position, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newView = inflater.inflate(R.layout.metadata_list_item, parent, false);
        TextView tv_table_name = (TextView) newView.findViewById(R.id.tv_table_name);
        TextView tv_last_sync = (TextView) newView.findViewById(R.id.tv_table_last_sync);
        TextView tv_last_update = (TextView) newView.findViewById(R.id.tv_table_last_update);
        TextView tv_last_sync_status = (TextView) newView.findViewById(R.id.tv_table_last_sync_status);

        ApiSyncState state = states.get(position);
        tv_table_name.setText(state.getTableName());
        tv_last_update.setText(format.format(new Date(state.getLastUpdateTime())));
        tv_last_sync.setText(format.format(new Date(state.getLastSyncTime())));

        switch (state.getLastSyncStatus()) {
            case APISyncStateTable.ApiSyncStates.SYNC_SUCCESS:
                tv_last_sync_status.setTextColor(Color.GREEN);
                tv_last_sync_status.setText("同步成功");
                break;
            case APISyncStateTable.ApiSyncStates.SYNC_ONGOING:
                tv_last_sync_status.setTextColor(Color.YELLOW);
                tv_last_sync_status.setText("正在同步");
                break;
            case APISyncStateTable.ApiSyncStates.SYNC_FAILURE:
                tv_last_sync_status.setTextColor(Color.RED);
                tv_last_sync_status.setText("同步失败");
                break;
        }

        return newView;
    }
}
