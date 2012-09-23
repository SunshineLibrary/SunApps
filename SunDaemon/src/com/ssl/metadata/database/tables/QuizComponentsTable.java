package com.ssl.metadata.database.tables;

import android.provider.BaseColumns;
import com.ssl.metadata.database.DBHandler;
import com.ssl.metadata.provider.MetadataContract;

import static com.ssl.metadata.provider.MetadataContract.QuizComponents;

public class QuizComponentsTable extends AbstractTable{

    public static final String TABLE_NAME = "quiz_components";

    public static final String[] ALL_COLUMNS = {
            QuizComponents._ID,
            QuizComponents._PROBLEM_ID,
            QuizComponents._QUIZ_ACTIVITY_ID,
            QuizComponents._SEQUENCE
    };

    public static final String[][] COLUMN_DEFINITIONS = {
            {QuizComponents._ID, "INTEGER PRIMARY KEY"},
            {QuizComponents._PROBLEM_ID, "INTEGER"},
            {QuizComponents._QUIZ_ACTIVITY_ID, "INTEGER"},
            {QuizComponents._SEQUENCE, "INTEGER"},
    };

    public QuizComponentsTable(DBHandler db) {
        super(db, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
    }
}
