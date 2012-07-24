package com.sunshine.metadata.database.tables;

import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.provider.MetadataContract;

public abstract class MenuWithForeignKeyTable extends Table {
    public MenuWithForeignKeyTable(MetadataDBHandler handler) {
        super(handler);
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
