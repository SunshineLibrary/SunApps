package com.ssl.support.install.service;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ssl.support.install.utils.JSONSerializable;

public class UninstallRequest implements JSONSerializable {

    private String apkPkg;

    public UninstallRequest() {}

	public UninstallRequest(String apkpkg) {
		this.apkPkg = apkpkg;
	}

    public String getApkPkg() {
        return apkPkg;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put("apkPkg", apkPkg);
        } catch (JSONException e) {
            Log.e(getClass().getName(), "Error creating JSONObject");
        }
        return object;
    }

    public static class Factory implements JSONSerializable.Factory<UninstallRequest> {

        @Override
        public UninstallRequest createNewFromJSON(JSONObject jsonObject) {
            try {
            	UninstallRequest request = new UninstallRequest();
                request.apkPkg = jsonObject.getString("apkPkg");
                return request;
            } catch (JSONException e) {
                Log.e(getClass().getName(), "Error creating UninstallRequest from JSONObject: " + jsonObject.toString());
            }
            return null;
        }
    }
}
