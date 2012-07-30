package com.sunshine.metadata.database.observers;

import android.content.ContentValues;
import android.net.Uri;

public abstract class TableObserver {

    public void preInsert(Uri uri, ContentValues values) {}

    public void postInsert(Uri uri, ContentValues values, Uri result) {}

    public void preDelete(Uri uri, String selection, String[] selectionArgs) {}

    public void postDelete(Uri uri, String selection, String[] selectionArgs, int result) {}

    public void preUpdate(Uri uri, ContentValues values, String selection, String[] selectionArgs) {}

    public void postUpdate(Uri uri, ContentValues values, String selection, String[] selectionArgs, int result) {}

}
