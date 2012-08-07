package com.sunshine.metadata.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public final class MetadataContract {

    public static final String AUTHORITY = "com.sunshine.metadata.provider";

    public static final Uri AUTHORITY_URI = new Uri.Builder().scheme("content")
        .authority(AUTHORITY).build();

    public static final class Edges {
        public static final String _ID = BaseColumns._ID;
        public static final String _FROM_ID = "from_id";
        public static final String _TO_ID = "to_id";
        public static final String _CONDITION = "condition";
        public static final String _SECTION_ID ="section_id";
        
        public static final Uri CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("edges").build();
    }

    public static final class Problems {
        public static final String _ID = BaseColumns._ID;
        public static final String _BODY = "body";
        public static final String _TYPE = "problem_type";
        public static final String _ANSWER = "answer";
        public static final String _PARENT_ID = "quiz_activity_id";
        
        public static final int TYPE_FILLBLANK = 1;
        public static final int TYPE_CHOICE = 0;
        
        public static int getInternalType(String type){
        	return 0;
        }
        
        
        public static final Uri CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("problems").build();
    }

    public static final class ProblemChoices {
        public static final String _ID = BaseColumns._ID;
        public static final String _CHOICE = "choice";
        public static final String _BODY = "body";
        public static final String _PARENT_ID = "problem_id";
        
        public static final Uri CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("problem_choices").build();
    }
    
    /*
     * Please use English comments only!
     */
    public static final class GalleryImages extends Downloadable{
        public static final String _ID = BaseColumns._ID;
        public static final String _GALLERY_ID = "gallery_id";
        public static final String _DESCRIPTION = "description";

        public static final Uri CONTENT_URI = Activities.CONTENT_URI.buildUpon().
                appendPath("gallery").appendPath("images").build();

        public static Uri getGalleryImageUri(int id) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
        }

        public static Uri getGalleryImageThumbnailUri(int id) {
            return CONTENT_URI.buildUpon().appendPath("thumbnail").appendPath(String.valueOf(id)).build();
        }
    }

    public static final class Courses {
        public static final String _ID = BaseColumns._ID;
        public static final String _NAME = "name";

        public static final Uri CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("courses").build();
    }

    public static final class Chapters {
        public static final String _ID = BaseColumns._ID;
        public static final String _PARENT_ID = "course_id";
        public static final String _NAME = "name";

        public static final Uri CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("chapters").build();
    }

    public static final class Lessons {
        public static final String _ID = BaseColumns._ID;
        public static final String _PARENT_ID = "chapter_id";
        public static final String _NAME = "name";

        public static final Uri CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("lessons").build();
    }

    public static final class Sections {
        public static final String _ID = BaseColumns._ID;
        public static final String _PARENT_ID = "lesson_id";
        public static final String _NAME = "name";
        public static final String _DESCRIPTION = "description";
        public static final Uri CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("sections").build();
    }

    public static final class Activities extends Downloadable {
        public static final String _ID = BaseColumns._ID;
        public static final String _SECTION_ID = "section_id";
        public static final String _PROVIDER_ID = "provider_id";
        public static final String _TYPE = "activity_type";
        public static final String _SEQUENCE = "seq";
        public static final String _NAME = "name";
        public static final String _DURATION = "duration";
        public static final String _NOTES = "notes";
        public static final String _DIFFICULTY = "difficulty";

        public static final Uri CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("activities").build();

        public static final int TYPE_NONE = -1;
        public static final int TYPE_TEXT = 0;
        public static final int TYPE_AUDIO = 1;
        public static final int TYPE_VIDEO = 2;
        public static final int TYPE_GALLERY = 3;
        public static final int TYPE_QUIZ = 4;
        public static final int TYPE_HTML = 5;

        public static Uri getActivityVideoUri(long id) {
            return CONTENT_URI.buildUpon().appendPath("video").appendPath(String.valueOf(id)).build();
        }

        public static Uri getActivityThumbnailUri(long id) {
            return CONTENT_URI.buildUpon().appendPath("thumbnail").appendPath(String.valueOf(id)).build();
        }

        public static Uri getActivityUri(int id) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
        }


    }


    /*
     * English comments only
     */
    public static final class BookCollections {

        public static final String _ID = BaseColumns._ID;
        public static final String _NAME = "name";
        public static final String _AUTHOR = "author";
        public static final String _DESCRIPTION = "description";
        public static final String _PUBLISHER = "publisher";
        public static final String _COVER = "cover";
        public static final Uri CONTENT_URI;

        public static Uri getTags(String collectionId) {
            // content://AUTHORITY/book_collections/#book_collection_id/tags
        	return CONTENT_URI.buildUpon().appendPath(collectionId).appendPath("tags").build();
        }

        public static Uri getBooks(String collectionId) {
            // content://AUTHORITY/book_collections/#book_collection_id/books
            return CONTENT_URI.buildUpon().appendPath(collectionId).appendPath("books").build();
        }

        static {
            CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("book_collections").build();
        }
    }

    public static final class VideoCollections {

        public static final String _ID = BaseColumns._ID;
        public static final String _NAME = "";
        public static final String _AUTHOR = "";
        public static final String _DESCRIPTION = "";
        public static final String _PUBLISHER = "";

        public static final Uri CONTENT_URI;

        public static Uri getTags(String id) {
            // content://AUTHORITY/video_collections/#video_collection_id/tags
        	return CONTENT_URI.buildUpon().appendPath(id).appendPath("tags").build();
        }

        public static Uri getVideos(String id) {
            // content://AUTHORITY/video_collections/#video_collection_id/videos
        	return CONTENT_URI.buildUpon().appendPath(id).appendPath("videos").build();
        }

        static {
            CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("video_collections").build();
        }
    }

    public static final class AudioCollections {

        public static final String _ID = BaseColumns._ID;
        public static final String _NAME = "";
        public static final String _AUTHOR = "";
        public static final String _DESCRIPTION = "";
        public static final String _PUBLISHER = "";

        public static final Uri CONTENT_URI;

        public static Uri getTags(String id) {
            // content://AUTHORITY/audio_collections/#audio_collection_id/tags
        	return CONTENT_URI.buildUpon().appendPath(id).appendPath("tags").build();
        }

        public static Uri getAudios(String id) {
            // content://AUTHORITY/audio_collections/#audio_collection_id/audios
        	return CONTENT_URI.buildUpon().appendPath(id).appendPath("audios").build();
        }

        static {
            CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("audio_collections").build();
        }
    }

    public static final class BookLists {

        public static final String _ID = BaseColumns._ID;
        public static final String _NAME = "name";
        public static final String _INTRO = "intro";
        public static final String _AUTHOR = "author";

        public static final Uri CONTENT_URI;

        public static Uri getTags(String listId) {
            // content://AUTHORITY/book_lists/#book_list_id/tags
        	return CONTENT_URI.buildUpon().appendPath(listId).appendPath("tags").build();
            
        }

        public static Uri getBookCollections(String listId) {
            // content://AUTHORITY/book_lists/#book_list_id/book_collections
        	return CONTENT_URI.buildUpon().appendPath(listId).appendPath("book_collections").build();
        }

        static {
            CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("book_lists").build();
        }
    }
    
    public static final class BookListCollections {

    	public static final String _ID = BaseColumns._ID;
    	public static final String _BOOK_LIST_ID = "book_list_id";
    	public static final String _BOOK_COLLECTION_ID = "book_collection_id";
    	
    	public static final Uri CONTENT_URI;
    	
    	static {
            CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("book_list_collections").build();
        }
    }

    public static final class Books extends Downloadable {

        public static final String _ID = BaseColumns._ID;
        public static final String _TITLE = "title";
        public static final String _AUTHOR = "author";
        public static final String _DESCRIPTION = "description";
        public static final String _PROGRESS = "progress";
        public static final String _COVER = "cover";
        public static final String _TAGS = "tags";
        public static final String _ORIGINAL_TITLE = "original_title";
        public static final String _PUBLISHER = "publisher";
        public static final String _PUBLICATION_YEAR = "publication_year";
        public static final String _COLLECTION_ID = "collection_id";
        public static final Uri CONTENT_URI;

        public static Uri getTags(String bookId) {
            return CONTENT_URI.buildUpon().appendPath(bookId).appendPath("tags").build();
        	// content://AUTHORITY/books/#book_id/tags
        }

        static {
            CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("books").build();
        }
    }

    public static final class Videos extends Downloadable {

        public static final String _ID = BaseColumns._ID;
        public static final String _NAME = "";
        public static final String _AUTHOR = "";
        public static final String _DESCRIPTION = "";
        public static final String _PROGRESS = "";
        public static final String _DURATION = "";
        public static final Uri CONTENT_URI;
        
        public static Uri getTags(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).appendPath("tags").build();
        	// content://AUTHORITY/videos/#video_id/tags
        }
        
        static {
            CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("videos").build();
        }
    }

    public static final class Audios extends Downloadable {

        public static final String _ID = BaseColumns._ID;
        public static final String _NAME = "";
        public static final String _AUTHOR = "";
        public static final String _DESCRIPTION = "";
        public static final String _PROGRESS = "";
        public static final String _DURATION = "";
        public static final Uri CONTENT_URI;
        
        public static Uri getTags(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).appendPath("tags").build();
        	// content://AUTHORITY/books/#book_id/tags
        }
        
        static {
            CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("audios").build();
        }
    }

    public static final class Tags {

        public static final String _ID = BaseColumns._ID;
        public static final String _NAME = "name";
        public static final String _TYPE = "tag_type";

        public static enum TYPE {
            THEME, DIFFICULTY
        }

        public static final Uri CONTENT_URI;

        public static Uri getBookCollections(String tagId) {
            //content://AUTHORITY/tags/#tag_id/book_collections
            return CONTENT_URI.buildUpon().appendPath(tagId).appendPath("book_collections").build();
        }

        public static Uri getVideoCollections(String tagId) {
            //content://AUTHORITY/tags/#tag_id/video_collections
            return CONTENT_URI.buildUpon().appendPath(tagId).appendPath("video_collections").build();
        }

        public static Uri getAudioCollections(String tagId) {
            //content://AUTHORITY/tags/#tag_id/audio_collections
        	return CONTENT_URI.buildUpon().appendPath(tagId).appendPath("audio_collections").build();
        }

        static {
            CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("tags").build();
        }
    }
    
    public static final class BookTags {

    	public static final String _ID = BaseColumns._ID;
    	public static final String _BOOK_ID = "book_id";
    	public static final String _TAG_ID = "tag_id";
    	
    	public static final Uri CONTENT_URI;
    	
    	static {
            CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("book_tag").build();
        }
    }
    
    public static final class BookCollectionTags {

    	public static final String _ID = BaseColumns._ID;
    	public static final String _BOOK_COLLLECTION_ID = "book_collection_id";
    	public static final String _TAG_ID = "tag_id";
    	
    	public static final Uri CONTENT_URI;
    	
    	static {
            CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("book_collection_tag").build();
        }
    }
    
    public static final class BookListTags {

    	public static final String _ID = BaseColumns._ID;
    	public static final String _BOOK_LIST_ID = "book_list_id";
    	public static final String _TAG_ID = "tag_id";
    	
    	public static final Uri CONTENT_URI;
    	
    	static {
            CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("book_list_tag").build();
        }
    }

    
    public static final class Packages extends Downloadable {
        public static final String _ID = BaseColumns._ID;
        public static final String _NAME = "name";
        public static final String _VERSION = "version";

        public static final Uri CONTENT_URI;

        static {
            CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("packages")
                .build();
        }
    }

    public static class Downloadable {

        public static final String _DOWNLOAD_STATUS = "download_status";
        
        public static final String _DOWNLOAD_PROGRESS = "download_progress";
        
        public static final String _DOWNLOAD_TIME = "download_time";
        
        public static enum STATUS {
            NOT_DOWNLOADED, QUEUED, DOWNLOADING, DOWNLOADED, MARK_DELETE
        }
    }
}
