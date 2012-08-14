package com.sunshine.support.installer.utils;

import org.json.JSONObject;

public interface JSONSerializable {

    public JSONObject toJSON();

    public interface Factory<T extends JSONSerializable> {
        public T createNewFromJSON(JSONObject jsonObject);
    }
}
