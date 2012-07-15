package com.sunshine.support.pkgmgr;

import com.sunshine.support.concurrent.ListenableFuture;

public class LocalPackageManager implements PackageManager{
	
	@Override
	public ListenableFuture<Package[]> list() {
		throw new java.lang.UnsupportedOperationException();
	}
	
}
