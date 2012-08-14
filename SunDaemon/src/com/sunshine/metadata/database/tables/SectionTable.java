package com.sunshine.metadata.database.tables;

import com.sunshine.metadata.database.DBHandler;
import com.sunshine.metadata.provider.MetadataContract;

import static com.sunshine.metadata.provider.MetadataContract.Lessons;
import static com.sunshine.metadata.provider.MetadataContract.Sections;

public class SectionTable extends MenuWithForeignKeyTable {
    public static final String TABLE_NAME = "sections";

    private static final String[] ALL_COLUMNS = {
            Sections._ID,
            Sections._NAME,
            Sections._PARENT_ID,
            Sections._DESCRIPTION,
    };

    private static final String[][] COLUMN_DEFINITIONS = {
            {Sections._ID, "INTEGER PRIMARY KEY"},
            {Sections._NAME, "TEXT"},
            {Sections._PARENT_ID, "INTEGER NOT NULL"},
            {Sections._DESCRIPTION, "TEXT"},
    };

    public SectionTable(DBHandler handler) {
        super(handler, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
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
	protected String getForeignKeyColumn() {
		return MetadataContract.Sections._PARENT_ID;
	}
}
