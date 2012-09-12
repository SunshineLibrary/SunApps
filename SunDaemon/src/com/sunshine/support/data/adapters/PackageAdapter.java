package com.sunshine.support.data.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.sunshine.support.R;
import com.sunshine.support.data.helpers.PackageHelper;
import com.sunshine.support.data.models.Package;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class PackageAdapter extends CursorAdapter {

    private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public PackageAdapter(Context context, Cursor c) {
        super(context, c, false);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newView = inflater.inflate(R.layout.apk_list_item, parent, false);
        bindView(newView, context, cursor);
        return newView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv_apk_name = (TextView) view.findViewById(R.id.tv_table_apk);
        TextView tv_version = (TextView) view.findViewById(R.id.tv_table_version);
        TextView tv_progress = (TextView) view.findViewById(R.id.tv_table_progress);
        TextView tv_status = (TextView) view.findViewById(R.id.tv_table_status);

        Package pkg = PackageHelper.newFromCursor(cursor);
        tv_apk_name.setText(pkg.getName());
        tv_version.setText(String.valueOf(pkg.getVersion()));
        tv_progress.setText("100");
        tv_status.setText("Pending");
    }
}
