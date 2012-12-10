package com.ssl.resourcecenter.model;

import android.net.Uri;

import com.ssl.metadata.provider.MetadataContract;

public class ItemBookCover extends ItemCover{
	private Uri imageUri;
	private Uri thumbnailUri;

	public ItemBookCover(String bookId) {
		imageUri = MetadataContract.Books.getBookUri(Integer.valueOf(bookId));
		thumbnailUri = MetadataContract.Books.getBookThumbnailUri(Integer.valueOf(bookId));
		
	}

	public Uri getImageUri() {
		return imageUri;
	}

	public Uri getThumbnailUri() {
		return thumbnailUri;
	}

}
