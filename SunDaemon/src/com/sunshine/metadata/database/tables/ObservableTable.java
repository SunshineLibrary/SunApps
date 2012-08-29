package com.sunshine.metadata.database.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.sunshine.metadata.database.Table;
import com.sunshine.metadata.database.observers.TableObserver;

import java.util.ArrayList;
import java.util.List;

public class ObservableTable implements Table {

    private Table mTable;
    private List<TableObserver> mObservers = new ArrayList<TableObserver>();

    public ObservableTable(Table table) {
        mTable = table;
    }

    public void addObserver(TableObserver observer) {
        mObservers.add(observer);
    }

    public void removeObserver(TableObserver observer) {
        mObservers.remove(observer);

    }

    @Override
    public String getTableName() {
        return mTable.getTableName();
    }

    @Override
    public String[] getColumns() {
        return mTable.getColumns();
    }

    @Override
    public void createTable(SQLiteDatabase db) {
        mTable.createTable(db);
    }

    @Override
    public void upgradeTable(SQLiteDatabase db, int oldVersion, int newVersion) {
        mTable.upgradeTable(db, oldVersion, newVersion);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return mTable.query(uri, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        preInsert(uri, values);
        Uri result = mTable.insert(uri, values);
        postInsert(uri, values, result);
        return result;
    }

    private void preInsert(Uri uri, ContentValues values) {
        if (mObservers.size() > 0) {
            for(TableObserver observer: mObservers) {
                observer.preInsert(uri, values);
            }
        }
    }

    private void postInsert(Uri uri, ContentValues values, Uri result) {
        if (mObservers.size() > 0) {
            for(TableObserver observer: mObservers) {
                observer.postInsert(this, uri, values, result);
            }
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        preDelete(uri, selection, selectionArgs);
        int result = mTable.delete(uri, selection, selectionArgs);
        postDelete(uri, selection, selectionArgs, result);
        return result;
    }

    private void preDelete(Uri uri, String selection, String[] selectionArgs) {
        if (mObservers.size() > 0) {
            for(TableObserver observer: mObservers) {
                observer.preDelete(uri, selection, selectionArgs);
            }
        }
    }

    private void postDelete(Uri uri, String selection, String[] selectionArgs, int result) {
        if (mObservers.size() > 0) {
            for(TableObserver observer: mObservers) {
                observer.postDelete(uri, selection, selectionArgs, result);
            }
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        preUpdate(uri, values, selection, selectionArgs);
        int result = mTable.update(uri, values, selection, selectionArgs);
        postUpdate(uri, values, selection, selectionArgs, result);
        return result;
    }

    @Override
    public SQLiteDatabase getDatabase() {
        return mTable.getDatabase();
    }

    private void preUpdate(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (mObservers.size() > 0) {
            for(TableObserver observer: mObservers) {
                observer.preUpdate(uri, values, selection, selectionArgs);
            }
        }
    }

    private void postUpdate(Uri uri, ContentValues values, String selection, String[] selectionArgs, int result) {
        if (mObservers.size() > 0) {
            for(TableObserver observer: mObservers) {
                observer.postUpdate(uri, values, selection, selectionArgs, result);
            }
        }
    }
}
