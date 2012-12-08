package com.sunshine.metadata.provider;

import android.net.Uri;

/**
 * @author Bowen Sun
 * 
 */
public class MetadataProviderTest extends SunProviderTestCase {

	public MetadataProviderTest() {
		this(MetadataProvider.class, "com.sunshine.metadata.provider");
	}

	public MetadataProviderTest(Class<MetadataProvider> providerClass,
			String providerAuthority) {
		super(providerClass, providerAuthority);
	}

	public void testPackagesDir() {
		Uri dirUri = MetadataContract.Packages.CONTENT_URI;
		this.completeSingleDirTest(dirUri, new String[][] {
				{ MetadataContract.Packages._NAME, "package" },
				{ MetadataContract.Packages._VERSION, "1" } });

	}
}
