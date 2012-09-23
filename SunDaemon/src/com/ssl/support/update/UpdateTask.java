package com.ssl.support.update;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import com.ssl.api.R;
import com.ssl.metadata.provider.MetadataContract;
import com.ssl.metadata.storage.ExternalFileStorage;
import com.ssl.support.api.ApiClient;
import com.ssl.support.api.ApiClientFactory;
import com.ssl.support.data.helpers.PackageHelper;
import com.ssl.support.data.models.Package;
import com.ssl.support.downloader.FileDownloadTask;
import com.ssl.support.downloader.MonitoredFileDownloadTask;
import com.ssl.support.services.UpdateService;
import com.ssl.metadata.storage.FileStorage;
import com.ssl.metadata.storage.FileStorageManager;
import com.ssl.support.utils.Listener;
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

    private static final String DAEMON_PACKAGE_NAME = "com.ssl.support.daemon";

    private static Package daemonPkg;
    private static Uri daemonFilePath;

    private UpdateService context;
    private FileStorage fileStorage;
    private ApiClient apiClient;
    private int numInstalls;

    private static final String PKG_DIR_NAME = "apks";
    private static final Uri PKG_DIR = Uri.parse(
            "file://" + Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/" + ExternalFileStorage.BASE_PATH).buildUpon().appendPath(PKG_DIR_NAME).build();

    public UpdateTask(UpdateService context) {
        this.context = context;
        apiClient = ApiClientFactory.newApiClient(context);
    }

    @Override
    protected Object doInBackground(Object... params) {
        List<Package> updates = getUpdates(getLocalPackages());
        Log.v(getClass().getName(), "Packages to be updated: " + updates);
        numInstalls = updates.size();
        for (Package pkg: updates) {
            downloadAndInstallApk(pkg);
        }
        return null;
    }

    private List<Package> getLocalPackages() {
        return PackageHelper.getLocalPackages(this.context);
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
            Log.e(getClass().getName(), "Failure converting packages to JSON...");
        }
        return jsonArr;
    }

    private JSONArray getPendingPackagesFromServer(JSONArray jsonArr) {
        Log.v(getClass().getName(), "Sending local packages: " + jsonArr.toString());
        HttpClient httpClient = apiClient.newHttpClient();
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
        HttpPost post = new HttpPost(apiClient.getApkUpdateUri().toString());
        List<NameValuePair> pairs = new ArrayList<NameValuePair>(1);
        pairs.add(new BasicNameValuePair("packages", jsonArr.toString()));
        post.setEntity(new UrlEncodedFormEntity(pairs));
        return post;
    }

    private void downloadAndInstallApk(Package pkg) {
        Uri remoteUri = apiClient.getDownloadUri("apks", pkg.getId());
        Uri localUri = getLocalFilePath(pkg);

        PackageHelper.createNewPackage(context, pkg);
        FileDownloadTask task = new MonitoredFileDownloadTask(context, remoteUri, localUri,
                pkg.getUpdateUri(), MetadataContract.Packages.CONTENT_URI);
        task.addListener(new InstallListener(pkg, localUri));
        Log.v(getClass().getName(), "Start downloading package: " + pkg);
        task.execute();
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
            Log.e(getClass().getName(), "Failed to create file for download: " + filePath);
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
                if (pkg.getName().equals(DAEMON_PACKAGE_NAME)) {
                    daemonPkg = pkg;
                    daemonFilePath = filePath;
                } else {
                    sendInstallRequest(pkg, filePath);
                }
            }

            numInstalls--;
            if (numInstalls == 0 && daemonPkg != null) {
                sendInstallRequest(daemonPkg, daemonFilePath);
            }
        }

        private void sendInstallRequest(Package pkg, Uri filePath) {
            Log.v(getClass().getName(), "Sending install request for package: " + pkg);
            Intent intent = new Intent();
            intent.setAction("com.ssl.support.action.scheduleInstall");
            intent.setData(filePath);
            context.startService(intent);
            PackageHelper.setInstallStatus(context, pkg.id, MetadataContract.Packages.INSTALL_STATUS_PENDING);
        }
    }
}