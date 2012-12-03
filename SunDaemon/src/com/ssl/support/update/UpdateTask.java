package com.ssl.support.update;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import com.ssl.metadata.provider.MetadataContract;
import com.ssl.metadata.storage.ExternalFileStorage;
import com.ssl.metadata.storage.FileStorage;
import com.ssl.metadata.storage.FileStorageManager;
import com.ssl.support.api.ApiClient;
import com.ssl.support.api.ApiClientFactory;
import com.ssl.support.data.helpers.PackageHelper;
import com.ssl.support.data.models.Package;
import com.ssl.support.downloader.tasks.AsyncFileDownloadTask;
import com.ssl.support.downloader.tasks.FileDownloadTask;
import com.ssl.support.downloader.tasks.MonitoredFileDownloadTask;
import com.ssl.support.services.UpdateService;
import com.ssl.support.utils.JSONUtils;
import com.ssl.support.utils.Listener;
import com.ssl.support.utils.LockManager;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UpdateTask extends AsyncTask {

    private static final String DAEMON_PACKAGE_NAME = "com.ssl.support.daemon";
    private static final String PKG_DIR_NAME = "apks";
    private static final String TAG = "UpdateTask";
    private static final Uri PKG_DIR = Uri.parse(
            "file://" + Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/" + ExternalFileStorage.BASE_PATH).buildUpon().appendPath(PKG_DIR_NAME).build();


    private UpdateService mContext;
    private ApiClient apiClient;
    private FileStorage fileStorage;
    private LinkedList<AsyncFileDownloadTask> tasks;
    private LockManager lockManager;
    private LockManager.Token lockToken;

    public UpdateTask(UpdateService context) {
        mContext = context;
        apiClient = ApiClientFactory.newApiClient(context);
        tasks = new LinkedList<AsyncFileDownloadTask>();
        lockManager = LockManager.getInstance(context);
    }

    @Override
    protected Object doInBackground(Object... params) {
        List<Package> updates = getUpdates(getLocalPackages());
        Log.v(TAG, "Packages to be updated: " + updates);

        for (Package pkg: updates) {
            if (pkg.getName().equals(DAEMON_PACKAGE_NAME)) {
                tasks.addLast(getDownloadTask(pkg));
            } else {
                tasks.addFirst(getDownloadTask(pkg));
            }
        }

        if (!tasks.isEmpty()) {
            lockToken = lockManager.acquireWifiLock(lockToken);
            tasks.getFirst().execute();
        }

        return null;
    }

    private List<Package> getLocalPackages() {
        return PackageHelper.getLocalPackages(this.mContext);
    }

    private List<Package> getUpdates(List<Package> localPackages) {
        JSONArray jsonArr = packageListToJSONArray(localPackages);
        jsonArr = getPendingPackagesFromServer(jsonArr);
        return PackageHelper.getPackageListFromJSONArray(jsonArr);
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
            Log.e(TAG, "Failure converting packages to JSON...");
        }
        return jsonArr;
    }

    private JSONArray getPendingPackagesFromServer(JSONArray jsonArr) {
        try {
            Log.v(TAG, "Sending Local Packages: " + jsonArr);
            HttpPost post = getPostMessage(jsonArr);
            JSONArray packages = JSONUtils.fetchJSONArrayFromUri(apiClient, post);
            Log.v(TAG, "Received Packages: " + packages);
            return packages;
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "URL encoding failed for data: " + jsonArr.toString(), e);
        }
        return new JSONArray();
    }

    private HttpPost getPostMessage(JSONArray jsonArr) throws UnsupportedEncodingException {
        HttpPost post = new HttpPost(apiClient.getApkUpdateUri().toString());
        List<NameValuePair> pairs = new ArrayList<NameValuePair>(1);
        pairs.add(new BasicNameValuePair("packages", jsonArr.toString()));
        post.setEntity(new UrlEncodedFormEntity(pairs));
        return post;
    }

    private AsyncFileDownloadTask getDownloadTask(Package pkg) {
        Uri remoteUri = apiClient.getDownloadUri("apks", pkg.getId());
        Uri localUri = getLocalFilePath(pkg);

        PackageHelper.createNewPackage(mContext, pkg);
        AsyncFileDownloadTask task = new MonitoredFileDownloadTask(mContext, remoteUri, localUri,
                pkg.getUpdateUri(), MetadataContract.Packages.CONTENT_URI);
        task.addListener(new InstallListener(pkg, localUri));
        return task;
    }

    private Uri getLocalFilePath(Package pkg) {
        Uri filePath = PKG_DIR.buildUpon().appendPath(pkg.getName() + "_" + pkg.version + ".apk").build();
        createNewFileSafely(filePath);
        return filePath;
    }

    private void createNewFileSafely(Uri filePath) {
        FileStorage storage = getExternalStorage();
        File file = new File(storage.getFile(PKG_DIR_NAME), filePath.getLastPathSegment());
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            Log.e(TAG, "Failed to create file for download: " + filePath);
        }
    }

    private FileStorage getExternalStorage() {
        if (fileStorage == null) {
            fileStorage = FileStorageManager.getInstance().getWritableFileStorage();
            if (!fileStorage.getFile(PKG_DIR_NAME).exists()) {
                fileStorage.mkdir(PKG_DIR_NAME);
            }
        }
        return fileStorage;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }

    private class InstallListener extends Listener<Integer> {

        private Package pkg;
        private Uri filePath;

        public InstallListener(Package pkg, Uri filePath) {
            this.pkg = pkg;
            this.filePath = filePath;
        }

        @Override
        public void onResult(Integer integer) {
            if (integer == FileDownloadTask.SUCCESS) {
                sendInstallRequest(pkg, filePath);
            }

            tasks.removeFirst();
            if (tasks.isEmpty()) {
                lockManager.releaseLock(lockToken);
            } else {
                tasks.getFirst().execute();
            }
        }

        private void sendInstallRequest(Package pkg, Uri filePath) {
            Log.v(TAG, "Sending install request for package: " + pkg);
            Intent intent = new Intent();
            intent.setAction("com.ssl.support.action.scheduleInstall");
            intent.setData(filePath);
            mContext.startService(intent);
            PackageHelper.setInstallStatus(mContext, pkg.id, MetadataContract.Packages.INSTALL_STATUS_PENDING);
        }
    }
}
