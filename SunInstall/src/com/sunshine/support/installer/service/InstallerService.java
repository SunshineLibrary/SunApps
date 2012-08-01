package com.sunshine.support.installer.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import com.sunshine.support.concurrent.ListenableFuture;
import com.sunshine.support.concurrent.Listener;
import com.sunshine.support.downloader.DownloadClient;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InstallerService extends Service {

	private static final String TAG = "Installer";
	private InstallerService thisInstallerService;
	private Map<String, ListenableFuture<Uri>> downloading = Collections.synchronizedMap(new HashMap<String, ListenableFuture<Uri>>());
	
	public InstallerService(){
		thisInstallerService = this;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		registerIntentReceivers();
	}
	
	@Override  
    public void onStart(Intent intent, int startId) {  
		if(intent.getAction().equals("sunshine.supportservice.install")){
			Log.i(TAG, "install "+intent);
			Pattern uriPattern=Pattern.compile("sunshine://app/([^/]+)/(.*)");
			Matcher matcher=uriPattern.matcher(intent.getData().toString());
			if(matcher.matches()){
				final String name=matcher.group(1);
				final String version=matcher.group(2);
				InstallerDataHelper data = null;
				try{
					data = new InstallerDataHelper(this);
					data.setInstallSchedule(name, version, null);
				}finally{
					if(data!=null){
						data.close();
					}
				}
				DownloadClient downloadClient=new DownloadClient();
				ListenableFuture<Uri> d = downloadClient.downloadApk(name, version);
				d.addListener(new Listener<Uri>(){
					public void callback(Uri value) {
						InstallerDataHelper data = null;
						try{
							data = new InstallerDataHelper(thisInstallerService);
							data.setInstallSchedule(name, version, value.toString());
						}finally{
							if(data!=null){
								data.close();
							}
						}
//						Toast.makeText(thisInstallerService, name+"("+version+")��׼���������´�����ʱ����������", Toast.LENGTH_LONG).show();
						downloading.remove(name);
					}					
				});
				downloading.put(name, d);
				d.start();
				
				
			}else{
				Log.w(TAG, intent.getData()+" is not a valid app uri.");
			}
		}else if(intent.getAction().equals("sunshine.supportservice.installactual")){
			InstallerDataHelper data = null;
			try{
				data = new InstallerDataHelper(this);
				for(final InstallRecord record:data.getScheduledInstalls()){
					if(record.getApk()!=null){
						Log.i(TAG, "do actual install: "+record.getName()+"("+record.getVersion()+") in "+record.getApk());	
						InstallerImpl impl=new InstallerImpl();
						if(impl.doActualInstall(Uri.parse(record.getApk()))){
							data.removeInstallSchedule(record.getName());							
						}
					}else{
						if(!downloading.containsKey(record.getName())){
							DownloadClient downloadClient=new DownloadClient();
							ListenableFuture<Uri> d = downloadClient.downloadApk(record.getName(), record.getVersion());
							d.addListener(new Listener<Uri>(){
								public void callback(Uri value) {
									InstallerDataHelper data = null;
									try{
										data = new InstallerDataHelper(thisInstallerService);
										data.setInstallSchedule(record.getName(), record.getVersion(), value.toString());
									}finally{
										if(data!=null){
											data.close();
										}
									}
									downloading.remove(record.getName());
								}					
							});
							downloading.put(record.getName(), d);
							d.start();
						}
					}
				}
			}finally{
				if(data!=null){
					data.close();
				}
			}
			
		}
    }
	
	private void registerIntentReceivers() 
	{ 
		Log.d(TAG, "registerIntentReceivers"); 
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF); 
		filter.addAction(Intent.ACTION_SCREEN_ON); 
		InstallerReceiver receiver = new InstallerReceiver(); 
		registerReceiver(receiver, filter); 
	} 
}
