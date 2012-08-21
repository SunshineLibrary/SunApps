package com.sunshine.sunresourcecenter.contentresolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sunshine.metadata.provider.MetadataContract.BookCollections;
import com.sunshine.metadata.provider.MetadataContract.BookInfo;
import com.sunshine.metadata.provider.MetadataContract.BookLists;
import com.sunshine.metadata.provider.MetadataContract.Books;
import com.sunshine.sunresourcecenter.R;
import com.sunshine.sunresourcecenter.R.drawable;
import com.sunshine.sunresourcecenter.model.CategoryGridItem;
import com.sunshine.sunresourcecenter.model.ItemBookCover;
import com.sunshine.sunresourcecenter.model.ResourceGridItem;
import com.sunshine.sunresourcecenter.model.ResourceListGridItem;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.ParcelFileDescriptor;

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
			
			//Bitmap bm = getBitmap(cur.getString(idCol));
			Cursor bookcur = null;
			while (cur.moveToNext()) {
				String tags = "";
				//tooo slow, try put into view
				bookcur = resolver.query(Books.CONTENT_URI, null, Books._COLLECTION_ID + " = '" + cur.getString(idCol) +"'", null, null);
				int count = bookcur.getCount();
				
				resGridItems.add(new ResourceGridItem(cur.getString(idCol), cur.getString(titleCol), cur.getString(authorCol) ,tags , null, 0, cur.getString(descriptionCol), count));	
				
			}
			if(bookcur != null)
				bookcur.close();
			if(cur != null)
				cur.close();
		} 
//		catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
		finally {
			
		}
		
		return (List)resGridItems;
	}
	
	public String getSingleResIdOfCollection(String ColId) {
		String ResId = null;
		//case book:
		Cursor bookcur = resolver.query(BookInfo.CONTENT_URI, null, BookInfo._COLLECTION_ID + " = '" + ColId +"'", null, null);
		
		if(bookcur.getCount()>1) {
			bookcur.close();
			return null;
		}
		while(bookcur.moveToNext()){
			ResId =  bookcur.getString(bookcur.getColumnIndex(BookInfo._ID));
		}
		
		//other cases:
		
		bookcur.close();
		return ResId;
	}
	
	public List<Object> getBooks(String[] projection, String selection){
		ArrayList<ResourceGridItem> resGridItems = new ArrayList<ResourceGridItem>();
		try {
			Cursor cur = resolver.query(BookInfo.CONTENT_URI, projection, selection, null, null);		

			int idCol = cur.getColumnIndex(BookInfo._BOOK_ID);
			int titleCol = cur.getColumnIndex(BookInfo._TITLE);
			int authorCol = cur.getColumnIndex(BookInfo._AUTHOR);
			int descriptionCol = cur.getColumnIndex(BookInfo._INTRO);
			int tagCol = cur.getColumnIndex(BookInfo._TAGS);
			//int progressCol = cur.getColumnIndex(Books._PROGRESS);
			
			//Bitmap bm = getBitmap(cur.getString(idCol));
			while (cur.moveToNext()) {
				//
				resGridItems.add(new ResourceGridItem(cur.getString(idCol), cur.getString(titleCol), cur.getString(authorCol) ,cur.getString(tagCol) , null, 20, cur.getString(descriptionCol), 0));	
			
			}
			cur.close();
			
		} 
//		catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
		finally {
			
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
	
	private Bitmap getBitmap(String bookId) throws IOException {
		ItemBookCover cover = new ItemBookCover(bookId);
		ParcelFileDescriptor pfdInput = resolver.openFileDescriptor(cover.getThumbnailUri(), "r");
		if (pfdInput == null)
			return null;
		Bitmap bitmap = BitmapFactory.decodeFileDescriptor(pfdInput.getFileDescriptor(), null, null);
		pfdInput.close();
		return bitmap;
	}

}
