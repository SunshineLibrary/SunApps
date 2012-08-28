package com.sunshine.sunresourcecenter.contentresolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sunshine.metadata.provider.MetadataContract.BookCollectionInfo;
import com.sunshine.metadata.provider.MetadataContract.BookCollectionTags;
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
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.ParcelFileDescriptor;
import android.util.Log;

public class ResourceContentResolver {
	
	private ContentResolver resolver;
	private Resources res;
		
	public ResourceContentResolver(ContentResolver resolver, Resources res){
		
		this.resolver = resolver;		
		this.res = res;
	}
	
	public List<Object> getBookCollections(String[] projection, String selection){
		ArrayList<ResourceGridItem> resGridItems = new ArrayList<ResourceGridItem>();

		try {
			Cursor cur = resolver.query(BookCollectionInfo.CONTENT_URI, projection, selection, null, null);		
			
			int idCol = cur.getColumnIndex(BookCollectionInfo._BOOK_COLLECTION_ID);
			int titleCol = cur.getColumnIndex(BookCollectionInfo._TITLE);
			int authorCol = cur.getColumnIndex(BookCollectionInfo._AUTHOR);
			int descriptionCol = cur.getColumnIndex(BookCollectionInfo._INTRO);
			int tagCol = cur.getColumnIndex(BookCollectionInfo._TAGS);
			int countCol = cur.getColumnIndex(BookCollectionInfo._COUNT);
			
			Bitmap bm = null;
			while (cur.moveToNext()) {
				try {
					bm = getBitmap(cur.getString(idCol), resolver);
				} catch (IOException e) {
					//default image
					bm = BitmapFactory.decodeResource(res, R.drawable.ic_launcher);
				}
				resGridItems.add(new ResourceGridItem(cur.getString(idCol), cur.getString(titleCol), cur.getString(authorCol), cur.getString(tagCol), bm, 0, cur.getString(descriptionCol), cur.getInt(countCol)));	
				
			}
			
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
			ResId =  bookcur.getString(bookcur.getColumnIndex(BookInfo._BOOK_ID));
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

			Bitmap bm = null;
			while (cur.moveToNext()) {
				try {
					bm = getBitmap(cur.getString(idCol), resolver);
				} catch (IOException e) {
					//default image
					bm = BitmapFactory.decodeResource(res, R.drawable.ic_launcher);
				}
				resGridItems.add(new ResourceGridItem(cur.getString(idCol), cur.getString(titleCol), cur.getString(authorCol) ,cur.getString(tagCol) , bm, 20, cur.getString(descriptionCol), 0));	
			
			}
			cur.close();
			
		}  
		finally {
			
		}
		
		return (List)resGridItems;
	}
	
	public List<Object> getBookCategory(){
		ArrayList<CategoryGridItem> cateGridItems = new ArrayList<CategoryGridItem>();
		
		try
		{
			Cursor cur = resolver.query(BookCollectionTags.CONTENT_URI, null, "", null, null);
			
			
			while(cur.moveToNext()){
				
			}
		}finally{
			
		}
		return (List)cateGridItems;
	}
	
	public Cursor getBookLists(String[] projection, String selection){
		ArrayList<ResourceListGridItem> listGridItems = new ArrayList<ResourceListGridItem>();
		return resolver.query(BookLists.CONTENT_URI, projection, selection, null, null);
	}
	
	public static Bitmap getBitmap(String bookId, ContentResolver resolver) throws IOException {
		ItemBookCover cover = new ItemBookCover(bookId);
		ParcelFileDescriptor pfdInput = resolver.openFileDescriptor(cover.getThumbnailUri(), "r");
		if (pfdInput == null)
			return null;
		Bitmap bitmap = BitmapFactory.decodeFileDescriptor(pfdInput.getFileDescriptor(), null, null);
		pfdInput.close();
		return bitmap;
	}

}
