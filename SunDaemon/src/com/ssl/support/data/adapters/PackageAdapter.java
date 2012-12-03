package com.ssl.support.data.adapters;

import android.content.Context;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.ssl.support.daemon.R;
import com.ssl.support.data.helpers.PackageHelper;
import com.ssl.support.data.models.Package;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static com.ssl.metadata.provider.MetadataContract.Packages;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class PackageAdapter extends CursorAdapter {

    private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private PackageManager mPackageManager;

    public PackageAdapter(Context context, Cursor c) {
        super(context, c, false);
        mPackageManager = context.getPackageManager();
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

        try {
            ApplicationInfo info = mPackageManager.getApplicationInfo(pkg.name, PackageManager.GET_META_DATA);
            pkg.displayName = info.loadLabel(mPackageManager).toString();
        } catch (PackageManager.NameNotFoundException e) {
            pkg.displayName = pkg.name;
        }

        tv_apk_name.setText(pkg.getDisplayName());
        tv_version.setText(String.valueOf(pkg.getVersion()));
        tv_progress.setText(String.valueOf(pkg.downloadProgress));

        switch(pkg.installStatus) {
            case Packages.INSTALL_STATUS_DOWNLOADING:
                tv_status.setTextColor(Color.YELLOW);
                tv_status.setText("下载中");
                break;
            case Packages.INSTALL_STATUS_PENDING:
                tv_status.setTextColor(Color.YELLOW);
                tv_status.setText("待安装");
                break;
            case Packages.INSTALL_STATUS_INSTALLED:
                tv_status.setTextColor(Color.GREEN);
                tv_status.setText("已安装");
                break;
            case Packages.INSTALL_STATUS_FAILED:
                tv_status.setTextColor(Color.RED);
                tv_status.setText("安装失败");
                break;
        }
    }
}
