package com.sunshine.metadata.database.tables;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.sunshine.metadata.database.DBHandler;
import com.sunshine.metadata.database.TableView;
import com.sunshine.metadata.provider.MetadataContract.Activities;
import com.sunshine.metadata.provider.MetadataContract.Authors;
import com.sunshine.metadata.provider.MetadataContract.BookTags;
import com.sunshine.metadata.provider.MetadataContract.Books;
import com.sunshine.metadata.provider.MetadataContract.SectionComponents;
import com.sunshine.metadata.provider.MetadataContract.Tags;

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
        
        query.append(String.format("b.%s as book_id, b.%s as title, b.%s as intro, b.%s as author, b.%s as publisher, b.%s as publication_year," +
        		"b.%s as book_collection_id, b.%s as download_status, a.%s as author_intro, GROUP_CONCAT(t.%s, '/') as tags ", 
        		Books._ID, Books._TITLE, Books._INTRO, Books._AUTHOR, Books._PUBLISHER, 
        		Books._PUBLICATION_YEAR, Books._COLLECTION_ID, Books._DOWNLOAD_STATUS, 
        		Authors._INTRO, Tags._NAME));
       
        query.append(String.format(" FROM %s b left join %s a left join %s bt left join %s t",
        		BookTable.TABLE_NAME, AuthorTable.TABLE_NAME, BookTagTable.TABLE_NAME, TagTable.TABLE_NAME));
       
        query.append(String.format(" ON b.%s = a.%s and b.%s = bt.%s and bt.%s = t.%s ",
        		Books._AUTHOR_ID, Authors._ID, Books._ID, BookTags._BOOK_ID, BookTags._TAG_ID, Tags._ID));
        
        query.append(String.format(" WHERE t.%s = '%s' ",
        		Tags._TYPE, Tags.TYPE.THEME));
        
        query.append(" GROUP BY book_id;");
        
        return query.toString();
    }
    
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		return dbHandler.getWritableDatabase().query(VIEW_NAME,
                projection, selection, selectionArgs, null, null, sortOrder);
	}

}
