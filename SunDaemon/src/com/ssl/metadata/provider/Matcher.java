package com.ssl.metadata.provider;

import android.content.UriMatcher;

import static com.ssl.metadata.provider.MetadataContract.AUTHORITY;

public class Matcher {

    /*
     * Defining constants for matching the content URI
     */
    public static final int SYSTEM_PASSWORD = 1;

    public static final int PACKAGES = 100;
    public static final int PACKAGES_ID = 101;
    public static final int API_SYNC_STATES = 102;

    public static final int SUBJECTS = 200;
    public static final int COURSES = 201;
    public static final int CHAPTERS = 202;
    public static final int LESSONS = 203;
    public static final int SECTIONS = 204;
    public static final int SECTIONS_ID = 205;
    public static final int SECTIONS_ACTIVITIES = 206;
    public static final int SECTIONS_THUMBNAIL = 207;

    public static final int ACTIVITIES = 300;
    public static final int ACTIVITIES_ID = 301;
    public static final int ACTIVITIES_THUMBNAIL = 302;

    public static final int GALLERY_IMAGES = 304;
    public static final int GALLERY_IMAGES_ID = 305;
    public static final int GALLERY_IMAGES_THUMBNAIL = 306;

    public static final int EDGES = 307;
    public static final int QUIZ_COMPONENTS = 309;
    public static final int QUIZ_PROBLEMS = 310;

    public static final int ACTIVITIES_VIDEO = 312;
    public static final int ACTIVITIES_PDF = 313;
    public static final int ACTIVITIES_HTML = 314;
    public static final int ACTIVITIES_TEXT = 315;

    public static final int PROBLEMS = 350;
    public static final int PROBLEM_CHOICES = 351;
    public static final int PROBLEMS_IMAGE_ID = 352;
    public static final int PROBLEMS_AUDIO_ID = 353;


    public static final int AUTHORS = 400;
    public static final int BOOKS = 401;
    public static final int BOOKS_ID = 402;
    public static final int BOOKS_THUMBNAIL = 403;
    public static final int BOOK_COLLECTIONS = 404;
    public static final int BOOK_COLLECTIONS_ID = 405;
    public static final int BOOK_COLLECTIONS_THUMBNAIL = 406;
    public static final int BOOK_LISTS = 407;
    public static final int BOOK_LISTS_ID = 408;
    public static final int BOOK_INFO = 409;
    public static final int BOOK_COLLECTION_INFO = 410;
    
    public static final int TAGS = 500;
    public static final int BOOK_TAG = 501;
    public static final int BOOK_COLLECTION_TAG = 502;
    public static final int BOOK_LIST_TAG = 503;
    public static final int BOOK_LIST_COLLECTION = 504;
    public static final int BOOK_CATEGORY = 505;
    
    
    
    //public static final int USER = 600;
    public static final int USER_BOOK = 601;
    public static final int USER_BOOK_ID = 602;

    public static class Factory {

        public static UriMatcher getMatcher() {
            UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

            matcher.addURI(AUTHORITY, "system_password", SYSTEM_PASSWORD);

            matcher.addURI(AUTHORITY, "packages", PACKAGES);
            matcher.addURI(AUTHORITY, "packages/#", PACKAGES_ID);
            matcher.addURI(AUTHORITY, "api_sync_states", API_SYNC_STATES);

            matcher.addURI(AUTHORITY, "subjects", SUBJECTS);
            matcher.addURI(AUTHORITY, "courses", COURSES);
            matcher.addURI(AUTHORITY, "chapters", CHAPTERS);
            matcher.addURI(AUTHORITY, "lessons", LESSONS);
            matcher.addURI(AUTHORITY, "sections", SECTIONS);
            matcher.addURI(AUTHORITY, "sections/#", SECTIONS_ID);
            matcher.addURI(AUTHORITY, "sections/thumbnail/#", SECTIONS_THUMBNAIL);
            matcher.addURI(AUTHORITY, "sections/activities/#", SECTIONS_ACTIVITIES);

            matcher.addURI(AUTHORITY, "edges", EDGES);

            matcher.addURI(AUTHORITY, "activities", ACTIVITIES);
            matcher.addURI(AUTHORITY, "activities/#", ACTIVITIES_ID);
            matcher.addURI(AUTHORITY, "activities/gallery/images", GALLERY_IMAGES);

            matcher.addURI(AUTHORITY, "activities/video/#", ACTIVITIES_VIDEO);
            matcher.addURI(AUTHORITY, "activities/text/#", ACTIVITIES_TEXT);
            matcher.addURI(AUTHORITY, "activities/html/#", ACTIVITIES_HTML);
            matcher.addURI(AUTHORITY, "activities/pdf/#", ACTIVITIES_PDF);
            matcher.addURI(AUTHORITY, "activities/thumbnail/#", ACTIVITIES_THUMBNAIL);
            matcher.addURI(AUTHORITY, "activities/gallery/images/#", GALLERY_IMAGES_ID);
            matcher.addURI(AUTHORITY, "activities/gallery/images/thumbnail/#", GALLERY_IMAGES_THUMBNAIL);

            matcher.addURI(AUTHORITY, "authors", AUTHORS);
            matcher.addURI(AUTHORITY, "books", BOOKS);
            matcher.addURI(AUTHORITY, "books/#", BOOKS_ID);
            matcher.addURI(AUTHORITY, "books/thumbnail/#", BOOKS_THUMBNAIL);
            matcher.addURI(AUTHORITY, "book_collections", BOOK_COLLECTIONS);
            matcher.addURI(AUTHORITY, "book_collections/#", BOOK_COLLECTIONS_ID);
            matcher.addURI(AUTHORITY, "book_collections/thumbnail/#", BOOK_COLLECTIONS_THUMBNAIL);
            matcher.addURI(AUTHORITY, "book_lists", BOOK_LISTS);
            matcher.addURI(AUTHORITY, "book_lists/#", BOOK_LISTS_ID);
            matcher.addURI(AUTHORITY, "book_info", BOOK_INFO);
            matcher.addURI(AUTHORITY, "book_collection_info", BOOK_COLLECTION_INFO);
            
            matcher.addURI(AUTHORITY, "quiz_components", QUIZ_COMPONENTS);
            matcher.addURI(AUTHORITY, "quiz_problems", QUIZ_PROBLEMS);
            matcher.addURI(AUTHORITY, "problem_choices", PROBLEM_CHOICES);
            matcher.addURI(AUTHORITY, "problems", PROBLEMS);
            matcher.addURI(AUTHORITY, "problems/image/#", PROBLEMS_IMAGE_ID);
            matcher.addURI(AUTHORITY, "problems/image/#.*", PROBLEMS_IMAGE_ID);
            matcher.addURI(AUTHORITY, "problems/audio/#", PROBLEMS_AUDIO_ID);
            matcher.addURI(AUTHORITY, "problems/audio/#.*", PROBLEMS_AUDIO_ID);

            matcher.addURI(AUTHORITY, "tags", TAGS);
            matcher.addURI(AUTHORITY, "book_tag", BOOK_TAG);
            matcher.addURI(AUTHORITY, "book_collection_tag", BOOK_COLLECTION_TAG);
            matcher.addURI(AUTHORITY, "book_list_tag", BOOK_LIST_TAG);
            matcher.addURI(AUTHORITY, "book_list_collection", BOOK_LIST_COLLECTION);
            matcher.addURI(AUTHORITY, "book_category", BOOK_CATEGORY);

            matcher.addURI(AUTHORITY, "user_book", USER_BOOK);
            matcher.addURI(AUTHORITY, "user_book/#", USER_BOOK_ID);

            return matcher;
        }
    }
}
