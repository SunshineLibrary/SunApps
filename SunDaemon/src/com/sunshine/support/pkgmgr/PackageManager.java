package com.sunshine.support.pkgmgr;

import com.sunshine.support.concurrent.ListenableFuture;

public interface PackageManager {
	
	public ListenableFuture<Package[]> list();
	
}
