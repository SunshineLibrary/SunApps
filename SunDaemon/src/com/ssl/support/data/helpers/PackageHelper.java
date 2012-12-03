package com.ssl.support.data.helpers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;
import com.ssl.support.data.models.Package;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import static com.ssl.metadata.provider.MetadataContract.Packages;

public class PackageHelper {

    public static List<Package> getAllPackages(Context context) {
        Cursor cursor = context.getContentResolver().query(Packages.CONTENT_URI, null, null, null, null);
        return getPackageListFromCursor(cursor, context);
    }

    public static List<Package> pickOutInstalledPackages(Context context, List<Package> packages) {
        List<Package> installed = new LinkedList<Package>();
        PackageManager pm = context.getPackageManager();
        for (Package pkg : packages) {
            try {
                PackageInfo info = pm.getPackageInfo(pkg.name, 0);
                if (info.versionCode >= pkg.version) {
                    pkg.version = info.versionCode;
                    installed.add(pkg);
                }
            } catch (PackageManager.NameNotFoundException e) {}
        }
        packages.removeAll(installed);
        return installed;
    }

    public static Package getPackageForFile(Context context, Uri data) {
        String path = truncatePath(data.getLastPathSegment());
        String[] values = path.split("_");
        if (values.length == 2) {
            Cursor cursor = context.getContentResolver().query(Packages.CONTENT_URI, null,
                    Packages._NAME + "= ? and " + Packages._VERSION + "= ?", values, null);
            if (cursor.moveToFirst()) {
                return newFromCursor(cursor);
            }
        }
        return null;
    }

    public static List<Package> getPackageListFromJSONArray(JSONArray jsonArr) {
        List<Package> packages = new Vector<Package>();
        try {
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject row = jsonArr.getJSONObject(i);
                Package pkg = new Package();
                pkg.id = row.getInt("id");
                pkg.version = row.getInt("version");
                pkg.name = row.getString("name");
                packages.add(pkg);
            }
        } catch (JSONException e) {
            Log.e("PackageHelper", "Error parsing package list: " + jsonArr.toString());
        }
        return packages;
    }

    public static void createOrUpdatePackage(Context context, Package pkg) {
        ContentResolver contentResolver = context.getContentResolver();
        if (isExistingPackage(contentResolver, pkg.id)) {
            contentResolver.update(Packages.CONTENT_URI, getUpdateRecordContentValues(pkg),
                    BaseColumns._ID + "=" + pkg.id, null);
        } else {
            contentResolver.insert(Packages.CONTENT_URI, getNewRecordContentValues(pkg));
        }
        contentResolver.notifyChange(Packages.CONTENT_URI, null);
    }

    private static boolean isExistingPackage(ContentResolver resolver, int pkg_id) {
        boolean isExisting = false;
        Cursor cursor = resolver.query(Packages.CONTENT_URI,
                null, BaseColumns._ID + "=" + pkg_id, null, null);
        if(cursor.moveToFirst()) {
            isExisting = true;
        }
        cursor.close();
        return isExisting;
    }

    public static void deletePackage(Context context, Package pkg) {
        context.getContentResolver().delete(Packages.CONTENT_URI, BaseColumns._ID + "=" + pkg.id, null);
    }

    public static void setInstallStatus(Context context, int id, int status) {
        setInstallStatusForMatching(context, BaseColumns._ID + "=" + id, status);
    }

    public static void setPendingToFailed(Context context) {
        setInstallStatusForMatching(context, Packages._INSTALL_STATUS + "=" + Packages.INSTALL_STATUS_PENDING,
                Packages.INSTALL_STATUS_FAILED);
    }

    private static void setInstallStatusForMatching(Context context, String where, int status) {
        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.update(Packages.CONTENT_URI, getContentValuesForStatus(status), where, null);
        contentResolver.notifyChange(Packages.CONTENT_URI, null);
    }

    public static Package newFromCursor(Cursor cursor) {
        Package pkg = new Package();
        pkg.id = cursor.getInt(cursor.getColumnIndex(Packages._ID));
        pkg.version = cursor.getInt(cursor.getColumnIndex(Packages._VERSION));
        pkg.name = cursor.getString(cursor.getColumnIndex(Packages._NAME));
        pkg.downloadProgress = cursor.getInt(cursor.getColumnIndex(Packages._DOWNLOAD_PROGRESS));
        pkg.downloadStatus = cursor.getInt(cursor.getColumnIndex(Packages._DOWNLOAD_STATUS));
        pkg.installStatus = cursor.getInt(cursor.getColumnIndex(Packages._INSTALL_STATUS));
        return pkg;
    }

    private static List<Package> getPackageListFromCursor(Cursor cursor, Context context) {
        List<Package> packages = new Vector<Package>();
        if (cursor.moveToFirst()) {
            do {
                packages.add(newFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return packages;
    }

    private static ContentValues getNewRecordContentValues(Package pkg) {
        ContentValues values = new ContentValues();
        values.put(Packages._ID, pkg.getId());
        values.put(Packages._VERSION, pkg.getVersion());
        values.put(Packages._NAME, pkg.getName());
        return values;
    }

    private static ContentValues getUpdateRecordContentValues(Package pkg) {
        ContentValues values = new ContentValues();
        values.put(Packages._VERSION, pkg.getVersion());
        values.put(Packages._NAME, pkg.getName());
        return values;
    }

    private static ContentValues getContentValuesForStatus(int status) {
        ContentValues values = new ContentValues();
        values.put(Packages._INSTALL_STATUS, status);
        if (status == Packages.INSTALL_STATUS_INSTALLED) {
            values.put(Packages._DOWNLOAD_PROGRESS, 100);
            values.put(Packages._DOWNLOAD_STATUS, Packages.STATUS_DOWNLOADED);
        }
        return values;
    }

    private static String truncatePath(String path) {
        int index = path.lastIndexOf(".apk");
        if (index >= 0) {
            return path.substring(0, index);
        }
        return path;
    }

}
