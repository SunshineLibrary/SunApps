package com.sunshine.support;

import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import com.sunshine.support.pkgmgr.Package;
import com.sunshine.support.webclient.WebClient;
import com.sunshine.support.concurrent.*;

public class SupportServiceActivity extends Activity {
    private PackageManager localPackageManager;
	private WebClient serverClient;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localPackageManager = this.getPackageManager();
        serverClient = new WebClient();
        setContentView(R.layout.main);
    }
    
    
    
    private List<Package> getLocalPackages(){
    	List<PackageInfo> infos = localPackageManager.getInstalledPackages(0);
    	List<Package> ret = new Vector<Package>();
    	for(int i = 0; i < infos.size(); ++i){
    		ret.add(new Package(infos.get(i).packageName, infos.get(i).versionName, "local"));
    	}
    	return ret;
    }
    
    private List<Package> getLocalRemoteDifference(){
    	List<Package> local = getLocalPackages();
    	ListenableFuture<List<Package> > future = serverClient.getServerPackageList();
    	List<Package> remote = future.get();
    	List<Package> diff = new Vector<Package>();
    	for( int i=0; i<remote.size(); ++i ){
    		if( !local.contains(remote.get(i)) ){
    			diff.add(remote.get(i));
    		}
    	}
    	return diff;
    }
}