package com.sunshine.metadata.database.views;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.sunshine.metadata.database.DBHandler;
import com.sunshine.metadata.database.TableView;
import com.sunshine.metadata.database.tables.BookCollectionTagTable;
import com.sunshine.metadata.database.tables.TagTable;
import com.sunshine.metadata.provider.MetadataContract.BookCollectionTags;
import com.sunshine.metadata.provider.MetadataContract.Tags;

public class BookCategoryView implements TableView{

	public static final String VIEW_NAME = "book_category";
	private DBHandler dbHandler;
	
	public BookCategoryView(DBHandler handler){
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
        
        query.append(String.format("t.%s as tag_id, t.%s as name, COUNT(DISTINCT bc.%s) as count", 
        		Tags._ID, Tags._NAME, BookCollectionTags._BOOK_COLLECTION_ID));
       
        query.append(String.format(" FROM %s t left join %s bc ",
        		TagTable.TABLE_NAME, BookCollectionTagTable.TABLE_NAME));
       
        query.append(String.format(" ON t.%s = bc.%s ",
        		Tags._ID, BookCollectionTags._TAG_ID ));
        
        query.append(String.format(" WHERE t.%s = '%s' ",
        		Tags._TYPE, Tags.TYPE.THEME));
        
        query.append(" GROUP BY tag_id;");
        
        return query.toString();
    }
    
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		return dbHandler.getWritableDatabase().query(VIEW_NAME,
                projection, selection, selectionArgs, null, null, sortOrder);
	}

}
