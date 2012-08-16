package com.sunshine.sunresourcecenter.model;

import android.net.Uri;

import com.sunshine.metadata.provider.MetadataContract;

public class ItemCover {
	private Uri imageUri;
	private Uri thumbnailUri;

	public ItemCover(String coverId) {
		imageUri = MetadataContract.BookCoverImages.getCoverImageUri(coverId);
		thumbnailUri = MetadataContract.BookCoverImages.getCoverImageThumbnailUri(coverId);
		
	}

	public Uri getImageUri() {
		return imageUri;
	}

	public Uri getThumbnailUri() {
		return thumbnailUri;
	}

}
