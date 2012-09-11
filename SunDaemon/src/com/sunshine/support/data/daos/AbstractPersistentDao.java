package com.sunshine.support.data.daos;

import android.content.ContentValues;
import com.sunshine.metadata.database.DBHandler;
import com.sunshine.support.data.models.PersistentData;

import java.util.Collection;

/**
 * A Persistent Dao should be able to fetch objects from database and persist dirty data afterwards.
 *
 * @author Bowen Sun
 * @version 1.0
 */
public abstract class AbstractPersistentDao<T extends PersistentData> {

    protected DBHandler dbHandler;

    protected abstract void fetch();

    protected abstract Collection<T> getAllFetched();

    protected abstract void update(T data, ContentValues values);

    protected abstract void insert(T data, ContentValues values);


    public void persist(T data) {
        if (data.isNew()) {
            insert(data, data.getCreateContentValues());
            data.clearNew();
        } else if (data.isDirty()) {
            update(data, data.getUpdateContentValues());
            data.clearDirty();
        }
    }

    public void close() {
        dbHandler.close();
    }
}
