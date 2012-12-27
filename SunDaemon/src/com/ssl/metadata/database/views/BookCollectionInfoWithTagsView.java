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
import com.ssl.metadata.provider.MetadataContract.BookCollectionInfo;
import com.ssl.metadata.provider.MetadataContract.BookCollectionInfoWithTags;
import com.ssl.metadata.provider.MetadataContract.BookCollectionTags;
import com.ssl.metadata.provider.MetadataContract.BookCollections;
import com.ssl.metadata.provider.MetadataContract.Books;
import com.ssl.metadata.provider.MetadataContract.Tags;

public class BookCollectionInfoWithTagsView implements TableView {
	
	public static final String VIEW_NAME = "book_collections_info_with_tags";
	
	public static final String QUERY_TAG = "("+BookCollectionInfoWithTags._ID+" IN (SELECT "+BookCollectionInfoWithTags._ID+" FROM "+BookCollectionInfoWithTagsView.VIEW_NAME+" WHERE   ("+BookCollectionInfoWithTags._TAG_ID+" = ?)))";
	
	private DBHandler dbHandler;
	
	public BookCollectionInfoWithTagsView(DBHandler handler){
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
        //SELECT book_collections.*,     (select count(            _id) from   books  where books.book_collection_id=book_collections._id) as count,    
        //book_collections_tags.tag_id as tag_id,    
        //tags.name as tag_name   from book_collections     left join book_collections_tags on book_collections._id = book_collections_tags.book_collection_id    
        //left join tags on tags._id =  book_collections_tags.tag_id  where count>0
        query.append(String.format(
        		"%s.*, (select count(            %s) from   %s ",
        		 BookCollectionTable.TABLE_NAME, Books._ID, BookTable.TABLE_NAME));
        query.append(String.format(" where %s.%s=%s.%s) as %s",
        		 BookTable.TABLE_NAME, Books._COLLECTION_ID, BookCollectionTable.TABLE_NAME, BookCollections._ID, BookCollectionInfoWithTags._COUNT
        		));
        query.append(String.format(", %s.%s as %s", BookCollectionTagTable.TABLE_NAME, BookCollectionTags._TAG_ID, BookCollectionInfoWithTags._TAG_ID));
        query.append(String.format(", %s.%s as %s", TagTable.TABLE_NAME, Tags._NAME, BookCollectionInfoWithTags._TAG_NAME));
        query.append(String.format(" from %s left join %s",BookCollectionTable.TABLE_NAME, BookCollectionTagTable.TABLE_NAME));
        query.append(String.format(" on %s.%s = %s.%s", BookCollectionTable.TABLE_NAME, BookCollections._ID, BookCollectionTagTable.TABLE_NAME, BookCollectionTags._BOOK_COLLECTION_ID));
        query.append(String.format(" left join %s", TagTable.TABLE_NAME));
        query.append(String.format(" on %s.%s = %s.%s", TagTable.TABLE_NAME, Tags._ID, BookCollectionTagTable.TABLE_NAME, BookCollectionTags._TAG_ID));
        query.append(String.format(" where %s>0", BookCollectionInfoWithTags._COUNT));
        return query.toString();
    }
    
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		return dbHandler.getWritableDatabase().query(VIEW_NAME,
                projection, selection, selectionArgs, BookCollectionInfoWithTags._ID, null, sortOrder);
	}

}
