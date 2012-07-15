package com.sunshine.metadata.provider;

import junit.framework.TestCase;

public class MetadataContractTest extends TestCase {
	
	public void testAuthorityUri(){
		String expected = "content://com.sunshine.metadata.provider";
		assertEquals(expected, MetadataContract.AUTHORITY_URI.toString());
	}
	
	public void testPackageUri() {
		String expected = "content://com.sunshine.metadata.provider/packages";
		assertEquals(expected, MetadataContract.Packages.CONTENT_URI.toString());
	}
}