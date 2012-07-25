package com.sunshine.support;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import com.sunshine.support.mock.ImageTestData;
import com.sunshine.support.webclient.WebClient;

public class SupportServiceActivity extends Activity {
    private PackageManager localPackageManager;
    private WebClient serverClient;
    private ImageTestData imageTestData;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        localPackageManager = this.getPackageManager();
//        serverClient = new WebClient();
        setContentView(R.layout.main);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        initFileStorage();
    }

    private void initFileStorage() {
        imageTestData = new ImageTestData(getResources(), getContentResolver());
        imageTestData.prepareDataForGallery("image01.jpeg", "thumbnail01.jpg", R.drawable.image01, R.drawable.thumbnail01, "test 01");
        imageTestData.prepareDataForGallery("image02.jpeg", "thumbnail02.jpg", R.drawable.image02, R.drawable.thumbnail02, "test 02");
        imageTestData.prepareDataForGallery("image03.jpeg", "thumbnail03.jpg", R.drawable.image03, R.drawable.thumbnail03, "test 03");
        imageTestData.prepareDataForGallery("image04.jpeg", "thumbnail04.jpg", R.drawable.image04, R.drawable.thumbnail04, "test 04");
        imageTestData.prepareDataForGallery("image05.jpeg", "thumbnail05.jpg", R.drawable.image04, R.drawable.thumbnail04, "test 05");
        imageTestData.prepareDataForGallery("image06.jpeg", "thumbnail06.jpg", R.drawable.image04, R.drawable.thumbnail04, "test 06");
        imageTestData.prepareDataForGallery("image07.jpeg", "thumbnail07.jpg", R.drawable.image04, R.drawable.thumbnail04, "test 07");
        imageTestData.prepareDataForGallery("image08.jpeg", "thumbnail08.jpg", R.drawable.image04, R.drawable.thumbnail04, "test 08");
        imageTestData.prepareDataForGallery("image09.jpeg", "thumbnail09.jpg", R.drawable.image04, R.drawable.thumbnail04, "test 09");
        imageTestData.prepareDataForGallery("image10.jpeg", "thumbnail10.jpg", R.drawable.image04, R.drawable.thumbnail04, "test 09");
        imageTestData.prepareDataForGallery("image11.jpeg", "thumbnail11.jpg", R.drawable.image04, R.drawable.thumbnail04, "test 09");
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
}