package com.sunshine.metadata.database.tables;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.sunshine.metadata.database.DBHandler;
import com.sunshine.metadata.database.TableView;

import static com.sunshine.metadata.provider.MetadataContract.Activities;
import static com.sunshine.metadata.provider.MetadataContract.SectionComponents;

public class SectionActivitiesView implements TableView {

    public static final String VIEW_NAME = "sections_activities";

    private DBHandler dbHandler;

    public SectionActivitiesView(DBHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    @Override
    public String getViewName() {
        return VIEW_NAME;
    }

    @Override
    public void createView(SQLiteDatabase db) {
        db.execSQL(createViewQuery());
    }

    private String createViewQuery() {
        String query = "CREATE VIEW " + VIEW_NAME + " AS SELECT ";
        query += String.format("s.%s, s.%s, s.%s, a.%s, a.%s", SectionComponents._SECTION_ID,
                SectionComponents._ACTIVITY_ID, SectionComponents._SEQUENCE, Activities._NAME, Activities._TYPE);
        query += String.format(" FROM %s s left join %s a ", SectionComponentsTable.TABLE_NAME, ActivityTable.TABLE_NAME);
        query += String.format("ON s.%s = a.%s;", SectionComponents._ACTIVITY_ID, Activities._ID);
        return query;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        return dbHandler.getWritableDatabase().query(VIEW_NAME,
                projection, selection, selectionArgs, null, null, sortOrder);
    }
}
