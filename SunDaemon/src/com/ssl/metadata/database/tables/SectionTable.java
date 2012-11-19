package com.ssl.metadata.database.tables;

import android.database.sqlite.SQLiteDatabase;
import com.ssl.metadata.database.DBHandler;
import com.ssl.metadata.provider.MetadataContract;

import static com.ssl.metadata.provider.MetadataContract.Lessons;
import static com.ssl.metadata.provider.MetadataContract.Sections;

public class SectionTable extends MenuWithForeignKeyTable {
    public static final String TABLE_NAME = "sections";

    private static final String[] ALL_COLUMNS = {
            Sections._ID,
            Sections._NAME,
            Sections._PARENT_ID,
            Sections._DESCRIPTION,
            Sections._DOWNLOAD_STATUS,
            Sections._STATUS
    };

    private static final String[][] COLUMN_DEFINITIONS = {
            {Sections._ID, "INTEGER PRIMARY KEY"},
            {Sections._NAME, "TEXT"},
            {Sections._PARENT_ID, "INTEGER NOT NULL"},
            {Sections._DESCRIPTION, "TEXT"},
            {Sections._DOWNLOAD_STATUS, "INTEGER"},
            {Sections._STATUS, "TEXT"},
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

    @Override
    public void upgradeTableInSteps(SQLiteDatabase db, int oldVersion, int newVersion) {
        addColumn(db, oldVersion, 114, Sections._STATUS, "TEXT");
    }
}
