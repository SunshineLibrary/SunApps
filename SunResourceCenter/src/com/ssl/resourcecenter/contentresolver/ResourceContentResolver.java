package com.ssl.resourcecenter.contentresolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ssl.metadata.provider.MetadataContract;
import com.ssl.metadata.provider.MetadataContract.BookCategories;
import com.ssl.metadata.provider.MetadataContract.BookCollectionInfo;
import com.ssl.metadata.provider.MetadataContract.BookCollectionInfoWithTags;
import com.ssl.metadata.provider.MetadataContract.BookCollections;
import com.ssl.metadata.provider.MetadataContract.BookLists;
import com.ssl.metadata.provider.MetadataContract.Books;
import com.ssl.resourcecenter.model.ItemBookCollectionCover;
import com.ssl.resourcecenter.model.ItemCover;
import com.ssl.resourcecenter.model.ResourceGridItem;
import com.ssl.resourcecenter.model.ResourceListGridItem;
import com.ssl.resourcecenter.R;
import com.ssl.resourcecenter.model.CategoryGridItem;
import com.ssl.resourcecenter.model.ItemBookCover;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.ParcelFileDescriptor;

public class ResourceContentResolver {

	private ContentResolver resolver;
	private Resources res;

	public ResourceContentResolver(ContentResolver resolver, Resources res) {

		this.resolver = resolver;
		this.res = res;
	}

	public List<Object> getBookCollections(String[] projection, String selection) {

		return getBookCollections(projection, selection, Integer.MAX_VALUE, 0);
	}

	public List<Object> getBookCollections(String[] projection,
			String selection, int itemPerPage, int offset) {
		ArrayList<Object> resGridItems = new ArrayList<Object>();

		try {
			// real code
			Cursor cur = null;
			try {
				cur = resolver.query(
						MetadataContract.BookCollectionInfo.CONTENT_URI,
						projection, selection, null, null);

				int idCol = cur.getColumnIndex(BookCollectionInfo._ID);
				int titleCol = cur.getColumnIndex(BookCollectionInfo._TITLE);
				int authorCol = cur.getColumnIndex(BookCollectionInfo._AUTHOR);
				int descriptionCol = cur
						.getColumnIndex(BookCollectionInfo._INTRO);
				int tagCol = cur.getColumnIndex(BookCollectionInfo._TAGS);
				int countCol = cur.getColumnIndex(BookCollectionInfo._COUNT);

				Bitmap bm = null;

				cur.move(offset);
				int i = 0;
				while (cur.moveToNext() && i < itemPerPage) {
					try {
						bm = getBookCollectionCoverBitmap(cur.getString(idCol),
								resolver);
					} catch (IOException e) {
						// default image
						bm = BitmapFactory.decodeResource(res,
								R.drawable.default_cover);
					}
					// select count()
					int count = cur.getInt(countCol);

					resGridItems.add(new ResourceGridItem(cur.getString(idCol),
							cur.getString(titleCol), cur.getString(authorCol),
							cur.getString(tagCol), bm, 0, cur.getString(descriptionCol), count));

					i++;
				}
			} finally {
				if (cur != null)
					cur.close();
			}
		}

		finally {

		}

		return resGridItems;
	}

	public List<Object> getBookCollectionsWithTags(String[] projection,
			String tagid, int itemPerPage, int offset) {
		ArrayList<Object> resGridItems = new ArrayList<Object>();

		projection = new String[]{
					BookCollectionInfoWithTags._ID,
					BookCollectionInfoWithTags._AUTHOR,
					BookCollectionInfoWithTags._TITLE,
					BookCollectionInfoWithTags._INTRO,
					BookCollectionInfoWithTags._COUNT,
					BookCollectionInfoWithTags._TAGS_PROJECTION};
		
		try {
			// real code
			Cursor cur = null;
			try {
				cur = resolver.query(
						MetadataContract.BookCollectionInfoWithTags
								.getBookCollectionWithTags(tagid), projection,
						null, null, null);

				int idCol = cur.getColumnIndex(BookCollectionInfoWithTags._ID);
				int titleCol = cur
						.getColumnIndex(BookCollectionInfoWithTags._TITLE);
				int authorCol = cur
						.getColumnIndex(BookCollectionInfoWithTags._AUTHOR);
				int descriptionCol = cur
						.getColumnIndex(BookCollectionInfoWithTags._INTRO);
				int countCol = cur
						.getColumnIndex(BookCollectionInfoWithTags._COUNT);
				int tagCol=cur.getColumnIndex(BookCollectionInfoWithTags._TAGS);
				Bitmap bm = null;

				cur.move(offset);
				int i = 0;
				while (cur.moveToNext() && i < itemPerPage) {
					try {
						bm = getBookCollectionCoverBitmap(cur.getString(idCol),
								resolver);
					} catch (IOException e) {
						// default image
						bm = BitmapFactory.decodeResource(res,
								R.drawable.default_cover);
					}
					// select count()
					int count = cur.getInt(countCol);

					resGridItems.add(new ResourceGridItem(cur.getString(idCol),
							cur.getString(titleCol), cur.getString(authorCol),
							cur.getString(tagCol), bm, 0, cur.getString(descriptionCol), count));

					i++;
				}
			} finally {
				if (cur != null)
					cur.close();
			}
		}

		finally {

		}

		return resGridItems;
	}

	public String getSingleResIdOfCollection(String ColId) {
		String ResId = null;
		// case book:
		// Cursor bookcur =
		// resolver.query(MetadataContract.BookInfo.CONTENT_URI, null,
		// BookInfo._COLLECTION_ID + " = '" + ColId +"'", null, null);
		Cursor bookcur = resolver.query(MetadataContract.Books.CONTENT_URI,
				null, Books._COLLECTION_ID + " = '" + ColId + "'", null, null);

		// if(bookcur.getCount()>1) {
		// bookcur.close();
		// return null;
		// }
		while (bookcur.moveToNext()) {
			// ResId =
			// bookcur.getString(bookcur.getColumnIndex(BookInfo._BOOK_ID));
			ResId = bookcur.getString(bookcur.getColumnIndex(Books._ID));
			break;
		}

		// other cases:

		bookcur.close();
		return ResId;
	}

	public List<Object> getBooks(String[] projection, String selection,
			int itemPerPage, int offset) {
		ArrayList<Object> resGridItems = new ArrayList<Object>();
		Cursor cur = null;
		try {
			// real code
			// Cursor cur =
			// resolver.query(MetadataContract.BookInfo.CONTENT_URI, projection,
			// selection, null, null);
			//
			// int idCol = cur.getColumnIndex(BookInfo._BOOK_ID);
			// int titleCol = cur.getColumnIndex(BookInfo._TITLE);
			// int authorCol = cur.getColumnIndex(BookInfo._AUTHOR);
			// int descriptionCol = cur.getColumnIndex(BookInfo._INTRO);
			// int tagCol = cur.getColumnIndex(BookInfo._TAGS);
			// int progressCol = cur.getColumnIndex(BookInfo._PROGRESS);

			// not real code!!
			cur = resolver.query(MetadataContract.Books.CONTENT_URI,
					projection, selection, null, null);

			int idCol = cur.getColumnIndex(Books._ID);
			int titleCol = cur.getColumnIndex(Books._TITLE);
			int authorCol = cur.getColumnIndex(Books._AUTHOR);
			int descriptionCol = cur.getColumnIndex(Books._INTRO);
			int progressCol = cur.getColumnIndex(Books._PROGRESS);

			Bitmap bm = null;
			cur.move(offset);
			int i = 0;
			while (cur.moveToNext() && i < itemPerPage) {
				try {
					bm = getBookCoverBitmap(cur.getString(idCol), resolver);
				} catch (IOException e) {
					// default image
					bm = BitmapFactory.decodeResource(res,
							R.drawable.default_cover);
				}
				resGridItems.add(new ResourceGridItem(cur.getString(idCol), cur
						.getString(titleCol), cur.getString(authorCol), null,
						bm, cur.getInt(progressCol), cur
								.getString(descriptionCol), 0));
				i++;
			}

		} finally {
			if (cur != null)
				cur.close();
		}

		return resGridItems;
	}

	public List<Object> getBookCategory() {
		ArrayList<Object> cateGridItems = new ArrayList<Object>();

		try {
			Cursor cur = resolver.query(
					MetadataContract.BookCategories.CONTENT_URI, null, null,
					null, null);

			int idCol = cur.getColumnIndex(BookCategories._TAG_ID);
			int nameCol = cur.getColumnIndex(BookCategories._NAME);
			int countCol = cur.getColumnIndex(BookCategories._COUNT);

			while (cur.moveToNext()) {
				cateGridItems.add(new CategoryGridItem(cur.getString(idCol),
						cur.getString(nameCol), cur.getInt(countCol), null,
						null));
			}
		} finally {

		}
		return cateGridItems;
	}

	public Cursor getBookLists(String[] projection, String selection) {
		return resolver.query(MetadataContract.BookLists.CONTENT_URI,
				projection, selection, null, null);
	}

	public static Bitmap getBookCoverBitmap(String bookId,
			ContentResolver resolver) throws IOException {
		ItemBookCover cover = new ItemBookCover(bookId);
		return getBitmap(cover, resolver);
	}

	public Bitmap getBookCollectionCoverBitmap(String bookCollectionId,
			ContentResolver resolver) throws IOException {
		ItemBookCollectionCover cover = new ItemBookCollectionCover(
				bookCollectionId);

		return getBitmap(cover, resolver);
	}

	public static Bitmap getBitmap(ItemCover cover, ContentResolver resolver)
			throws IOException {
		ParcelFileDescriptor pfdInput = resolver.openFileDescriptor(
				cover.getThumbnailUri(), "r");
		if (pfdInput == null)
			return null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inSampleSize = 2;
		Bitmap bitmap = BitmapFactory.decodeFileDescriptor(
				pfdInput.getFileDescriptor(), null, opts);
		pfdInput.close();
		return bitmap;
	}

}
