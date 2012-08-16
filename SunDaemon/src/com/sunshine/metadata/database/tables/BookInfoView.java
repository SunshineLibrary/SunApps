package com.sunshine.metadata.database.tables;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.sunshine.metadata.database.DBHandler;
import com.sunshine.metadata.database.TableView;
import com.sunshine.metadata.provider.MetadataContract.Activities;
import com.sunshine.metadata.provider.MetadataContract.Books;
import com.sunshine.metadata.provider.MetadataContract.SectionComponents;

public class BookInfoView implements TableView {
	
	public static final String VIEW_NAME = "books_info";
	private DBHandler dbHandler;
	
	public BookInfoView(DBHandler handler){
		this.dbHandler = handler;
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
    	
        StringBuffer query = new StringBuffer("CREATE VIEW " + VIEW_NAME + " AS SELECT ");
        query.append(String.format("b.%s, b.%s, b.%s, b.%s", Books._ID, Books._TITLE, Books._INTRO, Books._AUTHOR));
        
//        query += String.format("s.%s, s.%s, s.%s, a.%s, a.%s", );
//        query += String.format(" FROM %s s left join %s a ", SectionComponentsTable.TABLE_NAME, ActivityTable.TABLE_NAME);
//        query += String.format("ON s.%s = a.%s;", SectionComponents._ACTIVITY_ID, Activities._ID);
        return query.toString();
    }
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		return dbHandler.getWritableDatabase().query(VIEW_NAME,
                projection, selection, selectionArgs, null, null, sortOrder);
	}

}
