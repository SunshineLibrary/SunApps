package com.sunshine.support.update;

import android.database.Cursor;
import android.util.Log;
import com.sunshine.metadata.provider.MetadataContract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Vector;

public class PackageFactory {
    public static List<Package> getPackageListFromCursor(Cursor cursor) {
        List<Package> packages = new Vector<Package>();
        if (cursor.moveToFirst()) {
            do {
                Package pkg = new Package();
                pkg.id = cursor.getInt(cursor.getColumnIndex(MetadataContract.Packages._ID));
                pkg.version = cursor.getInt(cursor.getColumnIndex(MetadataContract.Packages._VERSION));
                pkg.name = cursor.getString(cursor.getColumnIndex(MetadataContract.Packages._NAME));
                packages.add(pkg);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return packages;
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
            Log.e("PackageFactory", "Error parsing package list: " + jsonArr.toString());
        }
        return packages;
    }
}
