package com.sunshine.support.downloader;

import java.net.URL;


import com.sunshine.support.concurrent.AsyncTaskFuture;
import com.sunshine.support.concurrent.ListenableFuture;

import android.net.Uri;


public class DownloadClient {
	
	public ListenableFuture<Uri> downloadApk(String name, String version){
		return new AsyncTaskFuture<URL,Integer,Uri>(getPackageUrl(name,version)){

			@Override
			protected Uri doInBackground(URL... params) {
				// TODO Auto-generated method stub
				return null;
			}};
	}
	
	URL getPackageUrl(String name, String version){
		return null;
	}
	

}
