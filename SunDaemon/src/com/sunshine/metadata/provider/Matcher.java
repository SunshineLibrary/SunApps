package com.sunshine.metadata.provider;

import android.content.UriMatcher;

import static com.sunshine.metadata.provider.MetadataContract.AUTHORITY;

public class Matcher {

    /*
     * Defining constants for matching the content URI
     */

    public static final int PACKAGES = 1;

    public static final int COURSES = 2;
    public static final int CHAPTERS = 3;
    public static final int LESSONS = 4;
    public static final int SECTIONS = 5;

    public static final int ACTIVITIES = 6;
    public static final int ACTIVITIES_ID = 7;
    public static final int ACTIVITIES_THUMBNAIL = 8;

    public static final int ACTIVITIES_VIDEO = 9;

    public static final int GALLERY_IMAGES = 10;
    public static final int GALLERY_IMAGES_ID = 11;
    public static final int GALLERY_IMAGES_THUMBNAIL = 12;

    public static final int BOOKS = 13;
    public static final int BOOKS_ID = 14;
    public static final int BOOK_COLLECTIONS = 15;
    public static final int BOOK_COLLECTIONS_ID = 16;
    public static final int BOOK_LISTS = 17;
    public static final int BOOK_LISTS_ID = 18;
    
    public static final int PROBLEMS = 19;
    
    public static final int TAGS = 20;
    


    public static class Factory {
        public static UriMatcher getMatcher() {
            UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

            matcher.addURI(AUTHORITY, "packages", PACKAGES);

            matcher.addURI(AUTHORITY, "courses", COURSES);
            matcher.addURI(AUTHORITY, "chapters", CHAPTERS);
            matcher.addURI(AUTHORITY, "lessons", LESSONS);
            matcher.addURI(AUTHORITY, "sections", SECTIONS);

            matcher.addURI(AUTHORITY, "activities", ACTIVITIES);
            matcher.addURI(AUTHORITY, "activities/#", ACTIVITIES_ID);
            matcher.addURI(AUTHORITY, "activities/gallery/images", GALLERY_IMAGES);

            matcher.addURI(AUTHORITY, "activities/video/#", ACTIVITIES_VIDEO);
            matcher.addURI(AUTHORITY, "activities/thumbnail/#", ACTIVITIES_THUMBNAIL);
            matcher.addURI(AUTHORITY, "activities/gallery/images/#", GALLERY_IMAGES_ID);
            matcher.addURI(AUTHORITY, "activities/gallery/images/thumbnail/#", GALLERY_IMAGES_THUMBNAIL);

            matcher.addURI(AUTHORITY, "books", BOOKS);
            matcher.addURI(AUTHORITY, "books/#", BOOKS_ID);
            matcher.addURI(AUTHORITY, "book_collections", BOOK_COLLECTIONS);
            matcher.addURI(AUTHORITY, "book_collections/#", BOOK_COLLECTIONS_ID);
            matcher.addURI(AUTHORITY, "book_lists", BOOK_LISTS);
            matcher.addURI(AUTHORITY, "book_lists/#", BOOK_LISTS_ID);

            matcher.addURI(AUTHORITY, "problems", PROBLEMS);
            
            matcher.addURI(AUTHORITY, "tags", TAGS);

            return matcher;
        }
    }
}
