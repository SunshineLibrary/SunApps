package com.sunshine.metadata.provider;

import static com.sunshine.metadata.provider.MetadataContract.AUTHORITY;

public class MimeType {

    public static final String DIR_MIME_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY;
    public static final String ITEM_MIME_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY;

    public static final String METADATA_MIME_TYPE = DIR_MIME_TYPE + ".metadata";

    public static final String METADATA_ID_MIME_TYPE = ITEM_MIME_TYPE + ".metadata";

    public static final String GALLERY_IMAGES_MIME_TYPE = DIR_MIME_TYPE + ".images";
}
