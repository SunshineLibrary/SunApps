package com.sunshine.metadata.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public final class MetadataContract {

    public static final String AUTHORITY = "com.sunshine.metadata.provider";

    public static final Uri AUTHORITY_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY).build();

    public static final class Gallery {
        public static final String _ID = BaseColumns._ID;
        public static final String _THUMBNAIL_PATH = "thumbnail";
        public static final String _IMAGE_PATH = "image";
        public static final String _DESCRIPTION = "description";

        public static final Uri CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("gallery").build();
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
        
        public static final Uri CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("sections").build();
    }

    public static final class Activities extends Downloadable {
        public static final String _ID = BaseColumns._ID;
        public static final String _SECTION_ID = "section_id";
        public static final String _PROVIDER_ID = "provider_id";
        public static final String _TYPE = "activity_type";
        public static final String _SEQUENCE = "seq";
        public static final String _NAME = "name";
        public static final String _LENGTH = "length";
        public static final String _NOTES = "notes";
        public static final String _DIFFICULTY = "difficulty";

        public static enum Types {
            TEXT,
            AUDIO,
            VIDEO,
            GALLERY,
            QUIZ,
            HTML
        }
        
        public static final Uri CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("activities").build(); 
    }

    public static final class BookCollections {

        public static final String _ID = BaseColumns._ID;
        public static final String _NAME = "";
        public static final String _AUTHOR = "";
        public static final String _DESCRIPTION = "";
        public static final String _PUBLISHER = "";

        public static final Uri CONTENT_URI;

        public static Uri getTags(String bookId) {
            // content://AUTHORITY/book_collections/#book_collection_id/tags
            throw new UnsupportedOperationException();
        }

        public static Uri getBooks(String collectionId) {
            // content://AUTHORITY/book_collections/#book_collection_id/books
            throw new UnsupportedOperationException();
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

        public static Uri getTags(String bookId) {
            // content://AUTHORITY/video_collections/#video_collection_id/tags
            throw new UnsupportedOperationException();
        }

        public static Uri getVideos(String collectionId) {
            // content://AUTHORITY/video_collections/#video_collection_id/videos
            throw new UnsupportedOperationException();
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

        public static Uri getTags(String bookId) {
            // content://AUTHORITY/audio_collections/#audio_collection_id/tags
            throw new UnsupportedOperationException();
        }

        public static Uri getAudios(String collectionId) {
            // content://AUTHORITY/audio_collections/#audio_collection_id/books
            throw new UnsupportedOperationException();
        }

        static {
            CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("audio_collections").build();
        }
    }

    public static final class BookLists {

        public static final String _ID = BaseColumns._ID;
        public static final Uri CONTENT_URI;

        public static Uri getBookCollections(String collectionId) {
            // content://AUTHORITY/book_lists/#book_list_id/book_collections
            throw new UnsupportedOperationException();
        }

        static {
            CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("book_lists").build();
        }
    }

    public static final class Books extends Downloadable {

        public static final String _ID = BaseColumns._ID;
        public static final String _TITLE = "title";
        public static final String _AUTHOR = "author";
        public static final String _DESCRIPTION = "description";
        public static final String _PROGRESS = "progress";
        public static final Uri CONTENT_URI;

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

        static {
            CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("audios").build();
        }
    }

    public static final class Tags {

        public static final String _ID = BaseColumns._ID;
        public static final String _NAME = "";
        public static final String _TYPE = "";

        public static enum TYPE {
            THEME, DIFFICULTY
        }

        public static final Uri CONTENT_URI;

        public static Uri getBookCollections(String bookId) {
            //content://AUTHORITY/tags/#tag_id/book_collections
            throw new UnsupportedOperationException();
        }

        public static Uri getVideoCollections(String bookId) {
            //content://AUTHORITY/tags/#tag_id/video_collections
            throw new UnsupportedOperationException();
        }

        public static Uri getAudioCollections(String bookId) {
            //content://AUTHORITY/tags/#tag_id/audio_collections
            throw new UnsupportedOperationException();
        }

        static {
            CONTENT_URI = AUTHORITY_URI.buildUpon().appendPath("tags").build();
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

        public static enum STATUS {
            NOT_DOWNLOADED, QUEUED, DOWNLOADING, DOWNLOADED, MARK_DELETE
        }
    }
}
