package com.ssl.support.api.record;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.ssl.support.utils.JSONSerializable;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Bowen Sun
 * @version 1.0
 *
 */
public class UserRecordRequest implements Parcelable, JSONSerializable {

    private static final String TAG = "UserRecordRequest";

    private JSONObject mAsJson;

    public UserRecordRequest(JSONObject asJson) {
        mAsJson = asJson;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAsJson.toString());
    }

    public static final Creator<UserRecordRequest> CREATOR = new Creator<UserRecordRequest>() {
        @Override
        public UserRecordRequest createFromParcel(Parcel source) {
            String jsonString = source.readString();
            try {
                return new UserRecordRequest(new JSONObject(jsonString));
            } catch (JSONException e) {
                Log.e(TAG, "Failed to parse: " + jsonString, e);
                return null;
            }
        }

        @Override
        public UserRecordRequest[] newArray(int size) {
            return new UserRecordRequest[size];
        }
    };

    @Override
    public JSONObject toJSON() {
        return mAsJson;
    }

    public static class Builder {
        public static final String RECORD_TYPE = "record_type";
        public static final String RECORD_PARAMS = "record_params";

        public String mRecordType;
        private Map<String, String> mRecordParams;

        public Builder(String recordType) {
            mRecordType = recordType;
            mRecordParams = new HashMap<String, String>();
        }

        public void putParam(String key, String value) {
            mRecordParams.put(key, value);
        }

        public UserRecordRequest build() {
            JSONObject newJson = new JSONObject();
            try {
                newJson.put(RECORD_TYPE, mRecordType);
                newJson.put(RECORD_PARAMS, mRecordParams);
            } catch (JSONException e) {
                Log.e("UserRecordParams", String.format("Error creating json: {%s, %s}", mRecordType, mRecordParams));
                return null;
            }
            return new UserRecordRequest(newJson);
        }
    }
}
