package com.sunshine.support;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import com.sunshine.support.webclient.WebClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SupportServiceActivity extends Activity {
    private PackageManager localPackageManager;
    private WebClient serverClient;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        localPackageManager = this.getPackageManager();
//        serverClient = new WebClient();
        setContentView(R.layout.main);
        startService(new Intent("com.sunshine.support.action.sync"));
        try {
            prepareForGallery();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    private List<Package> getLocalPackages(){
//    	List<PackageInfo> infos = localPackageManager.getInstalledPackages(0);
//    	List<Package> ret = new Vector<Package>();
//    	for(int i = 0; i < infos.size(); ++i){
//    		ret.add(new Package(infos.get(i).packageName, infos.get(i).versionName, "local"));
//    	}
//    	return ret;
//    }

//    private List<Package> getLocalRemoteDifference(){
//    	List<Package> local = getLocalPackages();
//    	ListenableFuture<List<Package> > future = serverClient.getServerPackageList();
//    	List<Package> remote = future.get();
//    	List<Package> diff = new Vector<Package>();
//    	for( int i=0; i<remote.size(); ++i ){
//    		if( !local.contains(remote.get(i)) ){
//    			diff.add(remote.get(i));
//    		}
//    	}
//    	return diff;
//    }


    private void prepareForGallery() throws IOException {
        File rootFile = getFilesDir();
        File galleryFile = new File(rootFile, "gallery");
        if (!galleryFile.exists() || !galleryFile.isDirectory()) {
            galleryFile.mkdir();
        }
        File imageDir = new File(galleryFile, "image");
        if (!imageDir.exists() || !imageDir.isDirectory()) {
            imageDir.mkdir();
        }
        File testImage = new File(imageDir, "test.jpg");
        if (!testImage.exists()) {
            testImage.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(testImage);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        bm.compress(Bitmap.CompressFormat.JPEG, 80, fos);
        System.out.println("-----------------------------testImage = " + testImage.getAbsolutePath());
//      insert into gallery values (1, "content://com.sunshine.metadata.provider/gallery/image/test.jpg", "content://com.sunshine.metadata.provider/gallery/image/test.jpg", "description");
    }
}