package com.sunshine.support.update;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import com.sunshine.metadata.database.DBHandler;
import com.sunshine.metadata.database.SystemDBHandlerFactory;
import com.sunshine.metadata.database.tables.PackageTable;
import com.sunshine.metadata.provider.MetadataContract;
import com.sunshine.support.api.ApiClient;
import com.sunshine.support.downloader.FileDownloadTask;
import com.sunshine.support.storage.FileStorage;
import com.sunshine.support.storage.FileStorageManager;
import com.sunshine.support.utils.Listener;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UpdateTask extends AsyncTask {

    private DBHandler dbHandler;
    private PackageTable pkgTable;
    private Context context;
    private FileStorage fileStorage;

    private static final Uri PKG_DIR = new Uri.Builder().scheme("file").appendPath("/sdcard").appendPath(".apks").build();

    public UpdateTask(Context context) {
        this.context = context;
        dbHandler = SystemDBHandlerFactory.newSystemDBHandler(context);
        pkgTable = (PackageTable) dbHandler.getTableManager(PackageTable.TABLE_NAME);
    }

    @Override
    protected Object doInBackground(Object... params) {
        List<Package> updates = getUpdates(getLocalPackages());
        Log.v(getClass().getName(), "Packages to be updated: " + updates);
        for (Package pkg: updates) {
            downloadAndInstallApk(pkg);
        }
        return null;
    }

    private List<Package> getLocalPackages() {
        Cursor cursor = pkgTable.query(null, PackageTable.ALL_COLUMNS, null, null, null);
        return PackageFactory.getPackageListFromCursor(cursor);
    }

    private List<Package> getUpdates(List<Package> localPackages) {
        JSONArray jsonArr = packageListToJSONArray(localPackages);
        jsonArr = getPendingPackagesFromServer(jsonArr);
        return PackageFactory.getPackageListFromJSONArray(jsonArr);
    }

    private JSONArray packageListToJSONArray(List<Package> localPackages) {
        JSONArray jsonArr = new JSONArray();
        try {
            for (Package pkg: localPackages) {
                JSONObject obj = new JSONObject();
                obj.put("id", pkg.getId());
                obj.put("name", pkg.getName());
                obj.put("version", pkg.getVersion());
                jsonArr.put(obj);
            }
        } catch (JSONException e) {
            Log.e(getClass().getName(), "Failure converting packages to JSON...");
        }
        return jsonArr;
    }

    private JSONArray getPendingPackagesFromServer(JSONArray jsonArr) {
        Log.v(getClass().getName(), "Sending local packages: " + jsonArr.toString());
        HttpClient httpClient = ApiClient.newHttpClient();
        StringWriter writer = new StringWriter();
        JSONArray result = new JSONArray();

        try {
            HttpPost post = getPostMessage(jsonArr);
            HttpResponse response = httpClient.execute(post);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                writer.write(line);
            }

            result = new JSONArray(writer.toString());
            Log.v(getClass().getName(), "Received package list: " + result);
        } catch (UnsupportedEncodingException e) {
            Log.e(getClass().getName(), "URL encoding failed for data: " + jsonArr.toString(), e);
        } catch (IOException e) {
            Log.e(getClass().getName(), "Failed to post packages: " + jsonArr.toString(), e);
        } catch (JSONException e) {
            Log.e(getClass().getName(), "Failed to parse result: " + writer.toString(), e);
        }
        return result;
    }

    private HttpPost getPostMessage(JSONArray jsonArr) throws UnsupportedEncodingException {
        HttpPost post = new HttpPost(ApiClient.getApkUpdateUri().toString());
        List<NameValuePair> pairs = new ArrayList<NameValuePair>(1);
        pairs.add(new BasicNameValuePair("packages", jsonArr.toString()));
        post.setEntity(new UrlEncodedFormEntity(pairs));
        return post;
    }

    private void downloadAndInstallApk(Package pkg) {
        Uri remoteUri = ApiClient.getDownloadUri("packages", pkg.getId());
        Uri localUri = getLocalFilePath(pkg);

        Log.v(getClass().getName(), "Download package: " + pkg);
        FileDownloadTask task = new FileDownloadTask(context, remoteUri, localUri);
        task.addListener(new InstallListener(pkgTable, pkg, localUri));
    }

    private Uri getLocalFilePath(Package pkg) {
        Uri filePath = PKG_DIR.buildUpon().appendPath(pkg.getName() + pkg.version + ".apk").build();
        createNewFileSafely(filePath);
        return filePath;
    }

    private void createNewFileSafely(Uri filePath) {
        FileStorage storage = getExternalStorage();
        File file = storage.getFile(filePath.getPath());
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            Log.e(getClass().getName(), "Failed to create file for download: " + filePath);
        }
    }

    private FileStorage getExternalStorage() {
        if (fileStorage == null) {
            fileStorage = FileStorageManager.getInstance().getWritableFileStorage();
            if (!fileStorage.getFile(PKG_DIR.getPath()).exists()) {
                fileStorage.mkdir(PKG_DIR.getPath());
            }
        }
        return fileStorage;
    }

    private class InstallListener extends Listener<Integer> {

        private PackageTable table;
        private Package pkg;
        private Uri filePath;

        public InstallListener(PackageTable table, Package pkg, Uri filePath) {
            this.table = table;
            this.pkg = pkg;
            this.filePath = filePath;
        }

        @Override
        public void onResult(Integer integer) {
            if (integer == FileDownloadTask.SUCCESS) {
                Log.v(getClass().getName(), "Sending install request for package: " + pkg);
                Intent intent = new Intent();
                intent.setAction("com.sunshine.support.installer.scheduleInstall");
                intent.setData(filePath);
                context.startService(intent);
                createInstallRecord();
            }
        }

        private void createInstallRecord() {
            ContentValues values = new ContentValues();
            values.put(MetadataContract.Packages._ID, pkg.getId());
            values.put(MetadataContract.Packages._VERSION, pkg.getVersion());
            values.put(MetadataContract.Packages._NAME, pkg.getName());
            table.insert(null, values);
        }
    }
}
