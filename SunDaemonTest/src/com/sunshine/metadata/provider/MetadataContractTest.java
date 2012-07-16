package com.sunshine.metadata.provider;

import android.test.AndroidTestCase;

public class MetadataContractTest extends AndroidTestCase {
	
	public void testAuthorityUri(){
		String expected = "content://com.sunshine.metadata.provider";
		assertEquals(expected, MetadataContract.AUTHORITY_URI.toString());
	}
	
	public void testPackageUri() {
		String expected = "content://com.sunshine.metadata.provider/packages";
		assertEquals(expected, MetadataContract.Packages.CONTENT_URI.toString());
	}
	
	public void testBooksUri() {
		String expected = "content://com.sunshine.metadata.provider/books";
		assertEquals(expected, MetadataContract.Books.CONTENT_URI.toString());
	}
	
	public void testVideosUri() {
		String expected = "content://com.sunshine.metadata.provider/videos";
		assertEquals(expected, MetadataContract.Videos.CONTENT_URI.toString());
	}
	
	public void testAudiosUri() {
		String expected = "content://com.sunshine.metadata.provider/audios";
		assertEquals(expected, MetadataContract.Audios.CONTENT_URI.toString());
	}
	
	public void testBookCollectionsUri() {
		String expected = "content://com.sunshine.metadata.provider/book_collections";
		assertEquals(expected, MetadataContract.BookCollections.CONTENT_URI.toString());
	}
	
	public void testVideoCollectionsUri() {
		String expected = "content://com.sunshine.metadata.provider/video_collections";
		assertEquals(expected, MetadataContract.VideoCollections.CONTENT_URI.toString());
	}
	
	public void testAudioCollectionsUri() {
		String expected = "content://com.sunshine.metadata.provider/audio_collections";
		assertEquals(expected, MetadataContract.AudioCollections.CONTENT_URI.toString());
	}
	
	public void testBookListUri() {
		String expected = "content://com.sunshine.metadata.provider/book_lists";
		assertEquals(expected, MetadataContract.BookLists.CONTENT_URI.toString());
	}
	
	public void testTagsUri() {
		String expected = "content://com.sunshine.metadata.provider/tags";
		assertEquals(expected, MetadataContract.Tags.CONTENT_URI.toString());
	}
}