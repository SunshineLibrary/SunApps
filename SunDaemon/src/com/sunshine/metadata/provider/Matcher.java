package com.sunshine.metadata.provider;

import android.content.UriMatcher;

import static com.sunshine.metadata.provider.MetadataContract.AUTHORITY;

public class Matcher {

    /*
     * Defining constants for matching the content URI
     */

    public static final int PACKAGES = 100;

    public static final int COURSES = 200;
    public static final int CHAPTERS = 201;
    public static final int LESSONS = 202;
    public static final int SECTIONS = 203;
    public static final int SECTIONS_ACTIVITIES = 204;

    public static final int ACTIVITIES = 300;
    public static final int ACTIVITIES_ID = 301;
    public static final int ACTIVITIES_THUMBNAIL = 302;

    public static final int ACTIVITIES_VIDEO = 303;

    public static final int GALLERY_IMAGES = 304;
    public static final int GALLERY_IMAGES_ID = 305;
    public static final int GALLERY_IMAGES_THUMBNAIL = 306;

    public static final int PROBLEMS = 307;

    public static final int PROBLEMS_BELONG_TO_QUIZ_ACTIVITY = 308;

    public static final int EDGES = 309;

    public static final int PROBLEMS_CHOICE = 310;

    public static final int BOOKS = 400;
    public static final int BOOKS_ID = 401;
    public static final int BOOKS_THUMBNAIL = 402;
    public static final int BOOK_COLLECTIONS = 403;
    public static final int BOOK_COLLECTIONS_ID = 404;
    public static final int BOOK_COLLECTIONS_THUMBNAIL = 405;
    public static final int BOOK_LISTS = 406;
    public static final int BOOK_LISTS_ID = 407;
    

    public static final int TAGS = 500;
    public static final int BOOK_TAG = 501;
    public static final int BOOK_COLLECTION_TAG = 502;
    public static final int BOOK_LIST_TAG = 503;
    public static final int BOOK_LIST_COLLECTION = 504;

    public static class Factory {
        public static UriMatcher getMatcher() {
            UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

            matcher.addURI(AUTHORITY, "packages", PACKAGES);

            matcher.addURI(AUTHORITY, "courses", COURSES);
            matcher.addURI(AUTHORITY, "chapters", CHAPTERS);
            matcher.addURI(AUTHORITY, "lessons", LESSONS);
            matcher.addURI(AUTHORITY, "sections", SECTIONS);
            matcher.addURI(AUTHORITY, "sections/activities/#", SECTIONS_ACTIVITIES);

            matcher.addURI(AUTHORITY, "edges", EDGES);

            matcher.addURI(AUTHORITY, "activities", ACTIVITIES);
            matcher.addURI(AUTHORITY, "activities/#", ACTIVITIES_ID);
            matcher.addURI(AUTHORITY, "activities/gallery/images", GALLERY_IMAGES);

            matcher.addURI(AUTHORITY, "activities/video/#", ACTIVITIES_VIDEO);
            matcher.addURI(AUTHORITY, "activities/thumbnail/#", ACTIVITIES_THUMBNAIL);
            matcher.addURI(AUTHORITY, "activities/gallery/images/#", GALLERY_IMAGES_ID);
            matcher.addURI(AUTHORITY, "activities/gallery/images/thumbnail/#", GALLERY_IMAGES_THUMBNAIL);

            matcher.addURI(AUTHORITY, "books", BOOKS);
            matcher.addURI(AUTHORITY, "books/#", BOOKS_ID);
            matcher.addURI(AUTHORITY, "books/thumbnail/#", BOOKS_THUMBNAIL);
            matcher.addURI(AUTHORITY, "book_collections", BOOK_COLLECTIONS);
            matcher.addURI(AUTHORITY, "book_collections/#", BOOK_COLLECTIONS_ID);
            matcher.addURI(AUTHORITY, "book_collections/thumbnail/#", BOOK_COLLECTIONS_THUMBNAIL);
            matcher.addURI(AUTHORITY, "book_lists", BOOK_LISTS);
            matcher.addURI(AUTHORITY, "book_lists/#", BOOK_LISTS_ID);

            matcher.addURI(AUTHORITY, "problems", PROBLEMS);
            matcher.addURI(AUTHORITY, "problems/quiz/#", PROBLEMS_BELONG_TO_QUIZ_ACTIVITY);
            matcher.addURI(AUTHORITY, "problem_choices", PROBLEMS_CHOICE);

            matcher.addURI(AUTHORITY, "tags", TAGS);
            matcher.addURI(AUTHORITY, "book_tag", BOOK_TAG);
            matcher.addURI(AUTHORITY, "book_collection_tag", BOOK_COLLECTION_TAG);
            matcher.addURI(AUTHORITY, "book_list_tag", BOOK_LIST_TAG);
            matcher.addURI(AUTHORITY, "book_list_collection", BOOK_LIST_COLLECTION);
            

            return matcher;
        }
    }
}
