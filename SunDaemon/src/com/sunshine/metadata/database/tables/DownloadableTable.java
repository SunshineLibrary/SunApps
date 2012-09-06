package com.sunshine.metadata.database.tables;

import com.sunshine.metadata.database.DBHandler;
import com.sunshine.metadata.provider.MetadataContract.Downloadable;

public abstract class DownloadableTable extends AbstractTable {
    /**
     * TableManager requires a handle on the DatabaseHandler in order to access
     * the database.
     *
     * @param handler
     */

    private static final String[][] DOWNLOADABLE_COLUMNS = {
            {Downloadable._DOWNLOAD_PROGRESS, "INTEGER"},
            {Downloadable._DOWNLOAD_STATUS, "INTEGER"},
            {Downloadable._DOWNLOAD_TIME, "DATETIME"}
    };
    
    

    public DownloadableTable(DBHandler handler,
                             String tableName,
                             String[][] columnDefinitions,
                             String[] columns) {
        super(handler, tableName, columnDefinitions, columns);
    }

    @Override
    protected String getCreateQuery() {
        String query = "CREATE TABLE " + this.getTableName() + "(";
        for (String[] pair : getColumnDefinitions()) {
            query += pair[0] + " " + pair[1] + " , ";
        }
        for (String[] column: DOWNLOADABLE_COLUMNS) {
            query += column[0] + " " + column[1] + " , ";
        }
        query = query.substring(0, query.lastIndexOf(" , ")) + ");";
        return query;
    }
}
