package com.sunshine.support.installer;

import com.sunshine.support.installer.service.InstallerService;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class InstallerClient {

	Context context;
	
	public InstallerClient(Context context){
		this.context = context;
	}
	
	public void install(String name, String version){
		Intent i = new Intent("sunshine.supportservice.install", Uri.parse("sunshine://app/"+name+"/"+version));
		i.setClass(context, InstallerService.class);
		context.startService(i);		
	}
	
}
