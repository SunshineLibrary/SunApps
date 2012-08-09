package com.sunshine.sunresourcecenter;

import java.util.ArrayList;
import java.util.List;

import com.sunshine.metadata.provider.MetadataContract.BookCollections;
import com.sunshine.metadata.provider.MetadataContract.BookLists;
import com.sunshine.metadata.provider.MetadataContract.Books;
import com.sunshine.sunresourcecenter.R;
import com.sunshine.sunresourcecenter.griditem.CategoryGridItem;
import com.sunshine.sunresourcecenter.griditem.ResourceGridItem;
import com.sunshine.sunresourcecenter.griditem.ResourceListGridItem;

import android.content.ContentResolver;
import android.database.Cursor;

public class ResourceContentResolver {
	
	private ContentResolver resolver;
		
	public ResourceContentResolver(ContentResolver resolver){
		
		this.resolver = resolver;		
	}
	
	public List<Object> getBookCollections(String[] projection, String selection){
		ArrayList<ResourceGridItem> resGridItems = new ArrayList<ResourceGridItem>();

		try {
			Cursor cur = resolver.query(BookCollections.CONTENT_URI, projection, selection, null, null);		

			int idCol = cur.getColumnIndex(BookCollections._ID);
			int titleCol = cur.getColumnIndex(BookCollections._TITLE);
			int authorCol = cur.getColumnIndex(BookCollections._AUTHOR);
			int descriptionCol = cur.getColumnIndex(BookCollections._INTRO);
			
			int imageId = R.drawable.ic_launcher;
			while (cur.moveToNext()) {
				String tags = getBookCollectionTags(cur.getString(idCol));
				//tooo slow, try put into view
				int count = resolver.query(Books.CONTENT_URI, null, Books._COLLECTION_ID + " = '" + cur.getString(idCol) +"'", null, null).getCount();
				resGridItems.add(new ResourceGridItem(cur.getString(idCol), cur.getString(titleCol), cur.getString(authorCol) ,tags , imageId, 0, cur.getString(descriptionCol), count));	
			}
		} finally {
			
		}
		
		return (List)resGridItems;
	}
	
	public String getSingleResIdOfCollection(String ColId) {
		String ResId = null;
		//case book:
		Cursor bookcur = resolver.query(Books.CONTENT_URI, null, Books._COLLECTION_ID + " = '" + ColId +"'", null, null);
		
		if(bookcur.getCount()>1) 
			return null;
		while(bookcur.moveToNext()){
			ResId =  bookcur.getString(bookcur.getColumnIndex(Books._ID));
		}
		//other cases:
		return ResId;
	}
	
	public List<Object> getBooks(String[] projection, String selection){
		ArrayList<ResourceGridItem> resGridItems = new ArrayList<ResourceGridItem>();
		try {
			Cursor cur = resolver.query(Books.CONTENT_URI, projection, selection, null, null);		

			int idCol = cur.getColumnIndex(Books._ID);
			int titleCol = cur.getColumnIndex(Books._TITLE);
			int authorCol = cur.getColumnIndex(Books._AUTHOR);
			int descriptionCol = cur.getColumnIndex(Books._INTRO);
			//int progressCol = cur.getColumnIndex(Books._PROGRESS);
			
			int imageId = R.drawable.ic_launcher;
			while (cur.moveToNext()) {
				String tags = getBookTags(cur.getString(idCol));
				
				//
				resGridItems.add(new ResourceGridItem(cur.getString(idCol), cur.getString(titleCol), cur.getString(authorCol) ,tags , imageId, 20, cur.getString(descriptionCol), 0));	
			}
		} finally {
			
		}
		
		return (List)resGridItems;
	}
	
	public Cursor getBookLists(String[] projection, String selection){
		ArrayList<ResourceListGridItem> listGridItems = new ArrayList<ResourceListGridItem>();
		return resolver.query(BookLists.CONTENT_URI, projection, selection, null, null);
	}
	
	public List<Object> getBookCategory(){
		ArrayList<CategoryGridItem> cateGridItems = new ArrayList<CategoryGridItem>();
		//from tags
		//count books
		return (List)cateGridItems;
	}
	
	private String getBookTags(String id){
		
		return "";
	}
	
	private String getBookCollectionTags(String id){
		//BookCollections.
		return "";
	}
	
	private String getBookListTags(String id){
		
		return "";
	}
}
