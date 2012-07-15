package com.sunshine.support.webclient;


import android.net.Uri;

import com.sunshine.support.concurrent.ListenableFuture;
import com.sunshine.support.pkgmgr.Package;

public class WebClient {

	
	public ListenableFuture<Package[]> getServerPackageList(){
		throw new java.lang.UnsupportedOperationException();
	}
	
	public ListenableFuture<Uri> getPackageUrl(String name, String version){
		throw new java.lang.UnsupportedOperationException();
	}
	
}
