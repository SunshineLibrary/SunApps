package com.sunshine.metadata.database.tables;

import com.sunshine.metadata.database.AbstractTable;
import com.sunshine.metadata.database.MetadataDBHandler;

public abstract class MenuWithForeignKeyTable extends AbstractTable {
    public MenuWithForeignKeyTable(MetadataDBHandler handler,
                                   String tableName,
                                   String[][] columnDefinitions,
                                   String[] columns) {
        super(handler, tableName, columnDefinitions, columns);
    }

    @Override
    protected String getCreateQuery() {
        String query = "CREATE TABLE " + getTableName() + "(";
        for (String[] pair : getColumnDefinitions()) {
            query += pair[0] + " " + pair[1] + " , ";
        }
        query += "FOREIGN KEY (" + getForeignKeyColumn() + ") REFERENCES " + getParentTableName() + "(" + getForeignKey() + "));";
        return query;
    }

    protected abstract String getForeignKey();

    protected abstract String getParentTableName();
    
    protected abstract String getForeignKeyColumn();
}
