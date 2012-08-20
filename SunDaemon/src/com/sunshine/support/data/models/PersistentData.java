package com.sunshine.support.data.models;

import android.content.ContentValues;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public interface PersistentData {

    public boolean isDirty();

    public boolean isNew();

    public int getId();

    public ContentValues getUpdateContentValues();

    public ContentValues getCreateContentValues();

}
