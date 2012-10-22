package com.ssl.metadata.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public final class MetadataContract {

    public static final String AUTHORITY = "com.ssl.metadata.provider";

    public static final Uri AUTHORITY_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY).build();

    public static final class Edges {
        public static final String _ID = BaseColumns._ID;
        public static final String _FROM_ID = "from_id";
        public static final String _TO_ID = "to_id";
        public static final String _CONDITION = "condition";
        public static final String _SECTION_ID = "section_id";

        public static final Uri CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("edges").build();
    }

    public static final class Problems {
        public static final String _ID = BaseColumns._ID;
        public static final String _ANSWER = "answer";
        public static final String _BODY = "body";
        public static final String _TYPE = "problem_type";

        public static final Uri CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("problems").build();

        public static final int TYPE_FB = 0;
        public static final int TYPE_SC = 1;
        public static final int TYPE_MC = 2;

        public static int getInternalType(String type) {
            return 0;
        }

    }

    public static final class ProblemChoices {
        public static final String _ID = BaseColumns._ID;
        public static final String _CHOICE = "choice";
        public static final String _BODY = "body";
        public static final String _PARENT_ID = "problem_id";

        public static final Uri CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("problem_choices").build();
    }


    public static final class QuizComponents {
        public static final String _ID = BaseColumns._ID;
        public static final String _QUIZ_ACTIVITY_ID = "quiz_activity_id";
        public static final String _PROBLEM_ID = "problem_id";
        public static final String _SEQUENCE = "seq";

        public static final Uri CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("quiz_components").build();

        public static final Uri PROBLEMS_URI = AUTHORITY_URI.buildUpon().appendPath("quiz_problems").build();
    }

    /*
    * Please use English comments only!
    */
    public static final class GalleryImages extends Downloadable {
        public static final String _ID = BaseColumns._ID;
        public static final String _GALLERY_ID = "gallery_activity_id";
        public static final String _INTRO = "intro";

        public static final Uri CONTENT_URI = Activities.CONTENT_URI.buildUpon()
                .appendPath("gallery").appendPath("images").build();

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

    public static final class Sections extends Downloadable {
        public static final String _ID = BaseColumns._ID;
        public static final String _PARENT_ID = "lesson_id";
        public static final String _NAME = "name";
        public static final String _DESCRIPTION = "description";
        public static final Uri CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("sections").build();

        public static Uri getSectionActivitiesUri(int id) {
            return CONTENT_URI.buildUpon().appendPath("activities").appendPath(String.valueOf(id)).build();
        }

        public static Uri getSectionThumbnailUri(int id) {
            return CONTENT_URI.buildUpon().appendPath("thumbnail").appendPath(String.valueOf(id)).build();
        }
    }

    public static final class SectionComponents {
        public static final String _ID = BaseColumns._ID;
        public static final String _SECTION_ID = "section_id";
        public static final String _ACTIVITY_ID = "activity_id";
        public static final String _SEQUENCE = Activities._SEQUENCE;
    }


    public static final class Activities extends Downloadable {
        public static final String _ID = BaseColumns._ID;
        public static final String _SECTION_ID = "section_id";
        public static final String _PROVIDER_ID = "provider_id";
        public static final String _SEQUENCE = "seq";
        public static final String _TYPE = "activity_type";
        public static final String _NAME = "name";
        public static final String _DURATION = "duration";
        public static final String _NOTES = "notes";

        public static final Uri CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("activities").build();

        public static final int TYPE_NONE = -1;
        public static final int TYPE_TEXT = 0;
        public static final int TYPE_AUDIO = 1;
        public static final int TYPE_VIDEO = 2;
        public static final int TYPE_GALLERY = 3;
        public static final int TYPE_QUIZ = 4;
        public static final int TYPE_HTML = 5;
        public static final int TYPE_PDF = 6;
        
        public static Uri getActivityVideoUri(long id) {
            return CONTENT_URI.buildUpon().appendPath("video").appendPath(String.valueOf(id)).build();
        }

        public static Uri getActivityThumbnailUri(long id) {
            return CONTENT_URI.buildUpon().appendPath("thumbnail").appendPath(String.valueOf(id)).build();
        }

        public static Uri getActivityUri(int id) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
        }

        public static Uri getActivityTextUri(long id) {
            return CONTENT_URI.buildUpon().appendPath("text").appendPath(String.valueOf(id)).build();
        }

        public static Uri getActivityHtmlUri(long id) {
            return CONTENT_URI.buildUpon().appendPath("html").appendPath(String.valueOf(id)).build();
        }
        
        public static Uri getActivityPdfUri(long id) {
            return CONTENT_URI.buildUpon().appendPath("pdf").appendPath(String.valueOf(id)).build();
        }
    }

    /*
     * English comments only
     */
    public static final class BookCollections extends Downloadable{

        public static final String _ID = BaseColumns._ID;
        public static final String _TITLE = "title";
        public static final String _AUTHOR = "author";
//        public static final String _AUTHOR_ID = "author_id";
        public static final String _INTRO = "intro";
        public static final String _PUBLISHER = "publisher";
        public static final Uri CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("book_collections").build();

        public static Uri getTags(String collectionId) {
            return CONTENT_URI.buildUpon().appendPath(collectionId).appendPath("tags").build();
        }

        public static Uri getBooks(String collectionId) {
            return CONTENT_URI.buildUpon().appendPath(collectionId).appendPath("books").build();
        }
        
        public static Uri getBookCollectionThumbnailUri(int id) {
            return CONTENT_URI.buildUpon().appendPath("thumbnail").appendPath(String.valueOf(id)).build();
        }

        public static Uri getBookCollectionUri(int id) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
        }
    }
    
    /*
     *  for BookCollectionInfoView
     */
    public static final class BookCollectionInfo{
    	public static final String _ID = BaseColumns._ID;
    	public static final String _BOOK_COLLECTION_ID = "book_collection_id";
        public static final String _TITLE = "title";
        public static final String _AUTHOR = "author";
        public static final String _INTRO = "intro";
        public static final String _PUBLISHER = "publisher";
        public static final String _TAGS = "tags";
        public static final String _COUNT = "count";
        public static final String _DOWNLOAD_STATUS = "download_status";
        
        public static final Uri CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("book_collection_info").build();
    }
    
    public static final class VideoCollections {

        public static final String _ID = BaseColumns._ID;
        public static final String _NAME = "";
        public static final String _AUTHOR = "";
        public static final String _DESCRIPTION = "";
        public static final String _PUBLISHER = "";

        public static final Uri CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("video_collections").build();

        public static Uri getTags(String id) {
            // content://AUTHORITY/video_collections/#video_collection_id/tags
            return CONTENT_URI.buildUpon().appendPath(id).appendPath("tags").build();
        }

        public static Uri getVideos(String id) {
            // content://AUTHORITY/video_collections/#video_collection_id/videos
            return CONTENT_URI.buildUpon().appendPath(id).appendPath("videos").build();
        }
    }

    public static final class AudioCollections {

        public static final String _ID = BaseColumns._ID;
        public static final String _NAME = "";
        public static final String _AUTHOR = "";
        public static final String _DESCRIPTION = "";
        public static final String _PUBLISHER = "";

        public static final Uri CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("audio_collections").build();

        public static Uri getTags(String id) {
            // content://AUTHORITY/audio_collections/#audio_collection_id/tags
            return CONTENT_URI.buildUpon().appendPath(id).appendPath("tags").build();
        }

        public static Uri getAudios(String id) {
            // content://AUTHORITY/audio_collections/#audio_collection_id/audios
            return CONTENT_URI.buildUpon().appendPath(id).appendPath("audios").build();
        }
    }

    public static final class BookLists extends Downloadable{

        public static final String _ID = BaseColumns._ID;
        public static final String _NAME = "name";
        public static final String _INTRO = "intro";
        public static final String _AUTHOR = "author";

        public static final Uri CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("book_lists").build();

        public static Uri getTags(String listId) {
            // content://AUTHORITY/book_lists/#book_list_id/tags
            return CONTENT_URI.buildUpon().appendPath(listId).appendPath("tags").build();

        }

        public static Uri getBookCollections(String listId) {
            // content://AUTHORITY/book_lists/#book_list_id/book_collections
            return CONTENT_URI.buildUpon().appendPath(listId).appendPath("book_collections").build();
        }
    }

    public static final class BookListCollections {

        public static final String _ID = BaseColumns._ID;
        public static final String _BOOK_LIST_ID = "book_list_id";
        public static final String _BOOK_COLLECTION_ID = "book_collection_id";

        public static final Uri CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("book_list_collections").build();

        public static Uri getBookCollectionThumbnailUri(int id) {
            return CONTENT_URI.buildUpon().appendPath("thumbnail").appendPath(String.valueOf(id)).build();
        }
    }

    public static final class Books extends Downloadable {

        public static final String _ID = BaseColumns._ID;
        public static final String _TITLE = "title";
        public static final String _AUTHOR = "author";
        public static final String _AUTHOR_ID = "author_id";
        public static final String _INTRO = "intro";
        public static final String _PUBLISHER = "publisher";
        public static final String _PUBLICATION_YEAR = "publication_year";
        public static final String _COLLECTION_ID = "book_collection_id";
        public static final String _PROGRESS = "progress";
        public static final String _STARTTIME = "start_time";
        public static final Uri CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("books").build();

        public static Uri getTags(String bookId) {
            return CONTENT_URI.buildUpon().appendPath(bookId).appendPath("tags").build();
            // content://AUTHORITY/books/#book_id/tags
        }

        public static Uri getBookThumbnailUri(int id) {
            return CONTENT_URI.buildUpon().appendPath("thumbnail").appendPath(String.valueOf(id)).build();
        }

        public static Uri getBookUri(int id) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
        }
    }
    
    /*
     *  for BookInfoView
     */
    public static final class BookInfo{
    	public static final String _ID = BaseColumns._ID;
    	public static final String _BOOK_ID = "book_id";
        public static final String _TITLE = "title";
        public static final String _AUTHOR = "author";
        public static final String _AUTHOR_INTRO = "author_intro";
        public static final String _INTRO = "intro";
        public static final String _PUBLISHER = "publisher";
        public static final String _PUBLICATION_YEAR = "publication_year";
        public static final String _COLLECTION_ID = "book_collection_id";
        public static final String _TAGS = "tags";
        public static final String _PROGRESS = "progress";
        public static final String _STARTTIME = "start_time";
        public static final String _DOWNLOAD_STATUS = "download_status";
        public static final String _DOWNLOAD_TIME = "download_time";
        
        public static final Uri CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("book_info").build();
    }

    public static final class Videos extends Downloadable {

        public static final String _ID = BaseColumns._ID;
        public static final String _NAME = "";
        public static final String _AUTHOR = "";
        public static final String _DESCRIPTION = "";
        public static final String _PROGRESS = "";
        public static final String _DURATION = "";
        public static final Uri CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("videos").build();

        public static Uri getTags(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).appendPath("tags").build();
            // content://AUTHORITY/videos/#video_id/tags
        }
    }

    public static final class Audios extends Downloadable {

        public static final String _ID = BaseColumns._ID;
        public static final String _NAME = "";
        public static final String _AUTHOR = "";
        public static final String _DESCRIPTION = "";
        public static final String _PROGRESS = "";
        public static final String _DURATION = "";
        public static final Uri CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("audios").build();

        public static Uri getTags(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).appendPath("tags").build();
            // content://AUTHORITY/books/#book_id/tags
        }
    }
    
    public static final class Authors {
    	
    	public static final String _ID = BaseColumns._ID;
    	public static final String _NAME = "name";
    	public static final String _INTRO = "intro";
    }
    
    public static final class Tags {

        public static final String _ID = BaseColumns._ID;
        public static final String _NAME = "name";
        public static final String _TYPE = "tag_type";

        public static enum TYPE {
            THEME("话题"),
            DIFFICULTY("理解");
            
            String value;
            TYPE(String value){
            	this.value = value;
            }
            
            @Override
            public String toString(){
            	return value;
            }
        }

        public static final Uri CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("tags").build();

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
    }

    public static final class BookTags {

        public static final String _ID = BaseColumns._ID;
        public static final String _BOOK_ID = "book_id";
        public static final String _TAG_ID = "tag_id";

        public static final Uri CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("book_tag").build();
    }

    public static final class BookCollectionTags {

        public static final String _ID = BaseColumns._ID;
        public static final String _BOOK_COLLECTION_ID = "book_collection_id";
        public static final String _TAG_ID = "tag_id";

        public static final Uri CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("book_collection_tag").build();
    }

    public static final class BookListTags {

        public static final String _ID = BaseColumns._ID;
        public static final String _BOOK_LIST_ID = "book_list_id";
        public static final String _TAG_ID = "tag_id";

        public static final Uri CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("book_list_tag").build();
    }
    
    /*
     * 
     * for book category view
     */
    public static final class BookCategories{
    	public static final String _ID = BaseColumns._ID;
    	public static final String _TAG_ID = "tag_id";
        public static final String _NAME = "name";
        public static final String _COUNT = "count";
        
        public static final Uri CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("book_category").build();
    }
    
    public static final class UserBook {
    	
    	public static final String _ID = BaseColumns._ID;
    	public static final String _USER_ID = "user_id";
    	public static final String _BOOK_ID = "book_id";
    	public static final String _PROGRESS = "progress";
    	public static final String _FIRST_READ_AT = "first_read_at";
    	public static final String _LAST_READ_AT = "last_read_at";
    	
    	public static final Uri CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("user_book").build();
    }

    public static final class Packages extends Downloadable {
        public static final String _ID = BaseColumns._ID;
        public static final String _NAME = "name";
        public static final String _VERSION = "version";
        public static final String _INSTALL_STATUS = "install_status";

        public static final Uri CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("packages").build();

        public static final int INSTALL_STATUS_DOWNLOADING = 0;
        public static final int INSTALL_STATUS_PENDING = 1;
        public static final int INSTALL_STATUS_INSTALLED = 2;
        public static final int INSTALL_STATUS_FAILED = 3;

        public static Uri getPackageUri(int id) {
            return CONTENT_URI.buildUpon().appendEncodedPath(String.valueOf(id)).build();
        }
    }

    public static class Downloadable {

        public static final String _DOWNLOAD_STATUS = "download_status";
        public static final String _DOWNLOAD_PROGRESS = "download_progress";
        public static final String _DOWNLOAD_TIME = "download_time";

        public static final int STATUS_NOT_DOWNLOADED = 0;
        public static final int STATUS_QUEUED = 1;
        public static final int STATUS_DOWNLOADING = 2;
        public static final int STATUS_DOWNLOADED = 3;
        public static final int STATUS_MARK_DELETE = 4;
    }
}
