package com.ssl.support.api.record;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.ssl.support.utils.JSONSerializable;
import org.json.JSONException;
import org.json.JSONObject;
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

    @Override
    public String toString() {
        return mAsJson.toString();
    }

    public static class Builder {
        public static final String RECORD_TYPE = "item_type";
        public static final String RECORD_ID = "item_id";
        public static final String RECORD_PARAMS = "params";

        public String mItemType;
        private Map<String, Object> mParams;
        private int mItemId;

        public Builder(String itemType, int itemId) {
            mItemType = itemType;
            mItemId = itemId;
            mParams = new HashMap<String, Object>();
        }

        public void putParam(String key, Object value) {
            mParams.put(key, value);
        }

        public UserRecordRequest build() {
            JSONObject newJson = new JSONObject();
            try {
                newJson.put(RECORD_ID, mItemId);
                newJson.put(RECORD_TYPE, mItemType);
                newJson.put(RECORD_PARAMS, new JSONObject(mParams));
            } catch (JSONException e) {
                Log.e("UserRecordParams", String.format("Error creating json: {%s, %s}", mItemType, mParams));
                return null;
            }
            return new UserRecordRequest(newJson);
        }
    }
}
