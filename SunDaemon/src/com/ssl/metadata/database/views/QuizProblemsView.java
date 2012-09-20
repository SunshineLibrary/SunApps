package com.ssl.metadata.database.views;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.ssl.metadata.database.DBHandler;
import com.ssl.metadata.database.TableView;
import com.ssl.metadata.database.tables.ProblemTable;
import com.ssl.metadata.database.tables.QuizComponentsTable;

import static com.ssl.metadata.provider.MetadataContract.*;

public class QuizProblemsView implements TableView {

    public static final String VIEW_NAME = "quizzes_problems";

    private DBHandler dbHandler;

    public QuizProblemsView(DBHandler dbHandler) {
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
        query += String.format("p.%s, q.%s, q.%s, q.%s, p.%s, p.%s, p.%s", Problems._ID,
                QuizComponents._QUIZ_ACTIVITY_ID, QuizComponents._PROBLEM_ID, QuizComponents._SEQUENCE,
                Problems._TYPE, Problems._ANSWER, Problems._BODY);
        query += String.format(" FROM %s q left join %s p ", QuizComponentsTable.TABLE_NAME, ProblemTable.TABLE_NAME);
        query += String.format("ON q.%s = p.%s ", QuizComponents._PROBLEM_ID, Problems._ID);
        query += String.format("WHERE p.%s > 0;", Problems._ID);
        return query;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        return dbHandler.getWritableDatabase().query(VIEW_NAME,
                projection, selection, selectionArgs, null, null, sortOrder);
    }
}
