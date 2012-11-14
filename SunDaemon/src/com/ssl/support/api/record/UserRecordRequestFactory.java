package com.ssl.support.api.record;

import com.ssl.support.utils.JSONSerializable;
import org.json.JSONObject;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class UserRecordRequestFactory implements JSONSerializable.Factory<UserRecordRequest> {

    @Override
    public UserRecordRequest createNewFromJSON(JSONObject jsonObject) {
        return new UserRecordRequest(jsonObject);
    }
}
