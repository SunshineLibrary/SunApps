package com.sunshine.metadata.database.tables;

import android.database.sqlite.SQLiteDatabase;
import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.provider.MetadataContract.Downloadable;

public abstract class DownloadableTable extends Table {
    /**
     * TableManager requires a handle on the DatabaseHandler in order to access
     * the database.
     *
     * @param handler
     */

    private static final String[] DOWNLOADABLE_COLUMNS = {
            Downloadable._DOWNLOAD_PROGRESS,
            Downloadable._DOWNLOAD_STATUS,
            Downloadable._THUMBNAIL
    };

    public DownloadableTable(MetadataDBHandler handler) {
        super(handler);
    }

    @Override
    protected String getCreateQuery() {
        String query = "CREATE TABLE " + this.getTableName() + "(";
        for (String[] pair : getColumnDefinitions()) {
            query += pair[0] + " " + pair[1] + " , ";
        }
        for (String column: DOWNLOADABLE_COLUMNS) {
            query += column + " INTEGER , ";
        }
        query = query.substring(0, query.lastIndexOf(" , ")) + ");";
        return query;
    }
}
