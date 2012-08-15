package com.sunshine.support.installer.service;

import android.net.Uri;
import android.util.Log;
import com.sunshine.support.installer.utils.JSONSerializable;
import org.json.JSONException;
import org.json.JSONObject;

public class InstallRequest implements JSONSerializable {

    private Uri apkPath;

    public InstallRequest() {}

	public InstallRequest(String apkPath) {
		this.apkPath = Uri.parse(apkPath);
	}

    public Uri getApkPath() {
        return apkPath;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put("apkPath", apkPath);
        } catch (JSONException e) {
            Log.e(getClass().getName(), "Error creating JSONObject");
        }
        return object;
    }

    public static class Factory implements JSONSerializable.Factory<InstallRequest> {

        @Override
        public InstallRequest createNewFromJSON(JSONObject jsonObject) {
            try {
                InstallRequest request = new InstallRequest();
                request.apkPath = Uri.parse(jsonObject.getString("apkPath"));
                return request;
            } catch (JSONException e) {
                Log.e(getClass().getName(), "Error creating InstallRequest from JSONObject: " + jsonObject.toString());
            }
            return null;
        }
    }
}
