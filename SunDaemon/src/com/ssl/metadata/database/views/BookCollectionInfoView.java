package com.ssl.metadata.database.views;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.ssl.metadata.database.DBHandler;
import com.ssl.metadata.database.TableView;
import com.ssl.metadata.database.tables.BookTable;
import com.ssl.metadata.database.tables.TagTable;
import com.ssl.metadata.database.tables.BookCollectionTable;
import com.ssl.metadata.database.tables.BookCollectionTagTable;
import com.ssl.metadata.provider.MetadataContract.BookCollectionTags;
import com.ssl.metadata.provider.MetadataContract.BookCollections;
import com.ssl.metadata.provider.MetadataContract.Books;
import com.ssl.metadata.provider.MetadataContract.Tags;

public class BookCollectionInfoView implements TableView {
	
	public static final String VIEW_NAME = "book_collections_info";
	private DBHandler dbHandler;
	
	public BookCollectionInfoView(DBHandler handler){
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
        
        query.append(String.format("bc.%s as book_collection_id, bc.%s as title, bc.%s as intro, bc.%s as author, bc.%s as publisher, " +
        		"bc.%s as download_status, GROUP_CONCAT(DISTINCT t.%s) as tags, COUNT(DISTINCT b.%s) as count", 
        		BookCollections._ID, BookCollections._TITLE, BookCollections._INTRO, BookCollections._AUTHOR, BookCollections._PUBLISHER, 
        		BookCollections._DOWNLOAD_STATUS, Tags._NAME, Books._ID));
       
        query.append(String.format(" FROM %s bc left join %s b left join %s bt left join %s t",
        		BookCollectionTable.TABLE_NAME, BookTable.TABLE_NAME, BookCollectionTagTable.TABLE_NAME, TagTable.TABLE_NAME));
       
        query.append(String.format(" ON bc.%s = b.%s and bc.%s = bt.%s and bt.%s = t.%s ",
        		BookCollections._ID, Books._COLLECTION_ID, BookCollections._ID, BookCollectionTags._BOOK_COLLECTION_ID, BookCollectionTags._TAG_ID, Tags._ID));
        
        query.append(String.format(" WHERE t.%s = '%s' ",
        		Tags._TYPE, Tags.TYPE.THEME));
        
        query.append(" GROUP BY book_collection_id;");
        
        return query.toString();
    }
    
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		return dbHandler.getWritableDatabase().query(VIEW_NAME,
                projection, selection, selectionArgs, null, null, sortOrder);
	}

}
