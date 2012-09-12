package com.sunshine.metadata.database.views;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.sunshine.metadata.database.DBHandler;
import com.sunshine.metadata.database.TableView;
import com.sunshine.metadata.database.tables.ActivityTable;
import com.sunshine.metadata.database.tables.SectionComponentsTable;

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
        query += String.format("a.%s, s.%s, s.%s, s.%s, a.%s, a.%s, a.%s, a.%s", Activities._ID,
                SectionComponents._SECTION_ID, SectionComponents._SEQUENCE, SectionComponents._ACTIVITY_ID,
                Activities._NAME, Activities._TYPE, Activities._DOWNLOAD_PROGRESS, Activities._DOWNLOAD_STATUS);
        query += String.format(" FROM %s s left join %s a ", SectionComponentsTable.TABLE_NAME, ActivityTable.TABLE_NAME);
        query += String.format("ON s.%s = a.%s ", SectionComponents._ACTIVITY_ID, Activities._ID);
        query += String.format("ORDER BY s.%s;", SectionComponents._SEQUENCE);
        return query;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        return dbHandler.getWritableDatabase().query(VIEW_NAME,
                projection, selection, selectionArgs, null, null, sortOrder);
    }
}
