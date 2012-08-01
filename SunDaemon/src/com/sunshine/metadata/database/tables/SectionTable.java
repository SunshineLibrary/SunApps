package com.sunshine.metadata.database.tables;

import com.sunshine.metadata.database.MetadataDBHandler;
import com.sunshine.metadata.provider.MetadataContract;

import static com.sunshine.metadata.provider.MetadataContract.Lessons;
import static com.sunshine.metadata.provider.MetadataContract.Sections;

public class SectionTable extends MenuWithForeignKeyTable {
    public static final String TABLE_NAME = "sections";

    private static final String[] ALL_COLUMNS = {
            Sections._ID,
            Sections._NAME,
            Sections._DESCRIPTION,
            Sections._PARENT_ID
    };

    private static final String[][] COLUMN_DEFINITIONS = {
            {Sections._ID, "INTEGER PRIMARY KEY"},
            {Sections._NAME, "TEXT"},
            {Sections._DESCRIPTION, "TEXT"},
            {Sections._PARENT_ID, "INTEGER NOT NULL"}
    };

    public SectionTable(MetadataDBHandler handler) {
        super(handler);
    }

    @Override
    protected String getForeignKey() {
        return Lessons._ID;
    }

    @Override
    protected String getParentTableName() {
        return LessonTable.TABLE_NAME;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String[][] getColumnDefinitions() {
        return COLUMN_DEFINITIONS;
    }

    @Override
    public String[] getColumns() {
        return ALL_COLUMNS;
    }

	@Override
	protected String getForeignKeyColumn() {
		return MetadataContract.Sections._PARENT_ID;
	}
}
