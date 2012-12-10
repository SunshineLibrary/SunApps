package com.ssl.resourcecenter.model;

import android.net.Uri;

public abstract class ItemCover {
	private Uri imageUri;
	private Uri thumbnailUri;

	public Uri getImageUri() {
		return imageUri;
	}

	public Uri getThumbnailUri() {
		return thumbnailUri;
	}
}
