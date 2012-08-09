package com.sunshine.metadata.database.tables;

import android.provider.BaseColumns;
import com.sunshine.metadata.database.MetadataDBHandler;

public class QuizComponentsTable extends AbstractTable{

    private static final class QuizComponents {
        public static final String _ID = BaseColumns._ID;
        public static final String _QUIZ_ACTIVITY_ID = "quiz_activity_id";
        public static final String _PROBLEM_ID= "problem_id";
    }

    public static final String TABLE_NAME = "quiz_components";

    public static final String[] ALL_COLUMNS = {
            QuizComponents._ID,
            QuizComponents._PROBLEM_ID,
            QuizComponents._QUIZ_ACTIVITY_ID,
    };

    public static final String[][] COLUMN_DEFINITIONS = {
            {QuizComponents._ID, "INTEGER PRIMARY KEY"},
            {QuizComponents._PROBLEM_ID, "INTEGER"},
            {QuizComponents._QUIZ_ACTIVITY_ID, "INTEGER"},
    };

    public QuizComponentsTable(MetadataDBHandler db) {
        super(db, TABLE_NAME, COLUMN_DEFINITIONS, ALL_COLUMNS);
    }
}
