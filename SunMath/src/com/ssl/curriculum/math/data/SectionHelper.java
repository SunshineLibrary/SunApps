package com.ssl.curriculum.math.data;

import android.content.Context;
import android.database.Cursor;
import com.ssl.curriculum.math.model.Section;
import com.ssl.metadata.provider.MetadataContract;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class SectionHelper {

    public static Section getSection(Context context, int id) {
        Cursor cursor = context.getContentResolver().query(MetadataContract.Sections.CONTENT_URI,
                null, MetadataContract.Sections._ID + "=" + id, null, null);
        if (cursor.moveToFirst()) {
            Section section = sectionFromCursor(cursor);
            cursor.close();
            return section;
        } else {
           return null;
        }
    }

    public static Cursor getSectionActivitiesCursor(Context context, int id) {
        Cursor cursor = context.getContentResolver().query(MetadataContract.Sections.getSectionActivitiesUri(id),
                null, MetadataContract.SectionComponents._SECTION_ID + "=" + id, null, null);
        return cursor;
    }

    private static Section sectionFromCursor(Cursor cursor) {
        Section section = new Section();
        section.id = cursor.getInt(cursor.getColumnIndex(MetadataContract.Sections._ID));
        section.name = cursor.getString(cursor.getColumnIndex(MetadataContract.Sections._NAME));
        section.description = cursor.getString(cursor.getColumnIndex(MetadataContract.Sections._DESCRIPTION));
        section.download_status = cursor.getInt(cursor.getColumnIndex(MetadataContract.Sections._DOWNLOAD_STATUS));
        return section;
    }
}
