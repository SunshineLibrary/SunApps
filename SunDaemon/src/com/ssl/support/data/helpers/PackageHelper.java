package com.ssl.support.data.helpers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.ssl.metadata.provider.MetadataContract;
import com.ssl.support.data.models.Package;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Vector;

import static com.ssl.metadata.provider.MetadataContract.Packages;

public class PackageHelper {
    public static List<Package> getLocalPackages(Context context) {
        Cursor cursor = context.getContentResolver().query(Packages.CONTENT_URI, null, null, null, null);
        return getPackageListFromCursor(cursor);
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

    public static void createNewPackage(Context context, Package pkg) {
        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.insert(Packages.CONTENT_URI, getNewRecordContentValues(pkg));
        context.getContentResolver().notifyChange(Packages.CONTENT_URI, null);
    }

    public static Package newFromCursor(Cursor cursor) {
        Package pkg = new Package();
        pkg.id = cursor.getInt(cursor.getColumnIndex(Packages._ID));
        pkg.version = cursor.getInt(cursor.getColumnIndex(Packages._VERSION));
        pkg.name = cursor.getString(cursor.getColumnIndex(Packages._NAME));
        return pkg;
    }

    private static List<Package> getPackageListFromCursor(Cursor cursor) {
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
        values.put(MetadataContract.Packages._ID, pkg.getId());
        values.put(MetadataContract.Packages._VERSION, pkg.getVersion());
        values.put(MetadataContract.Packages._NAME, pkg.getName());
        return values;
    }

}
