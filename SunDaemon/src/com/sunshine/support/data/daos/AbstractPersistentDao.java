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

    protected abstract void update(int id, ContentValues values);

    protected abstract void insert(ContentValues values);

    public void persist() {
        for (T data: getAllFetched()) {
            persistItem(data);
        }
    }

    protected void persistItem(T data) {
       if (data.isNew()) {
           insert(data.getCreateContentValues());
       } else if (data.isDirty()) {
           update(data.getId(), data.getUpdateContentValues());
       }
    }

    public void close() {
        dbHandler.close();
    }
}
