package com.sunshine.sunresourcecenter.model;

import android.net.Uri;

import com.sunshine.metadata.provider.MetadataContract;

public class ItemBookCollectionCover {
	private Uri imageUri;
	private Uri thumbnailUri;

	public ItemBookCollectionCover(String bookId) {
		imageUri = MetadataContract.BookCollections.getBookCollectionUri(Integer.valueOf(bookId));
		thumbnailUri = MetadataContract.BookCollections.getBookCollectionThumbnailUri(Integer.valueOf(bookId));
		
	}

	public Uri getImageUri() {
		return imageUri;
	}

	public Uri getThumbnailUri() {
		return thumbnailUri;
	}

}