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
			int titleCol = cur.getColumnIndex(BookCollections._NAME);
			int authorCol = cur.getColumnIndex(BookCollections._AUTHOR);
			int descriptionCol = cur.getColumnIndex(BookCollections._DESCRIPTION);
			
			while (cur.moveToNext()) {
				String tags = getBookCollectionTags(cur.getString(idCol));
				//
				Cursor bookcur = resolver.query(Books.CONTENT_URI, null, "collection_id = '" + cur.getString(idCol) +"'", null, null);
				int count = bookcur.getCount();
				resGridItems.add(new ResourceGridItem(cur.getString(idCol), cur.getString(titleCol), cur.getString(authorCol) ,tags , R.drawable.ic_launcher, 0, cur.getString(descriptionCol), count));	
			}
		} finally {
			
		}
		
		return (List)resGridItems;
	}
	
	public List<Object> getBooks(String[] projection, String selection){
		ArrayList<ResourceGridItem> resGridItems = new ArrayList<ResourceGridItem>();
		try {
			Cursor cur = resolver.query(Books.CONTENT_URI, projection, selection, null, null);		

			int idCol = cur.getColumnIndex(Books._ID);
			int titleCol = cur.getColumnIndex(Books._TITLE);
			int authorCol = cur.getColumnIndex(Books._AUTHOR);
			int descriptionCol = cur.getColumnIndex(Books._DESCRIPTION);
			int progressCol = cur.getColumnIndex(Books._PROGRESS);
			
			while (cur.moveToNext()) {
				String tags = getBookTags(cur.getString(idCol));
				//
				resGridItems.add(new ResourceGridItem(cur.getString(idCol), cur.getString(titleCol), cur.getString(authorCol) ,tags , R.drawable.ic_launcher, cur.getInt(progressCol), cur.getString(descriptionCol), 0));	
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
