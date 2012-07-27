package com.sunshine.support;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import com.sunshine.metadata.database.tables.ActivityTable;
import com.sunshine.metadata.provider.MetadataContract;
import com.sunshine.support.api.ApiClient;
import com.sunshine.support.mock.ImageTestData;
import com.sunshine.support.storage.FileDownloadTask;
import com.sunshine.support.sync.APISyncReceiver;
import com.sunshine.support.webclient.WebClient;

import java.io.FileNotFoundException;

import static com.sunshine.metadata.provider.MetadataContract.*;

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
//        startService(new Intent("com.sunshine.support.action.sync"));
//        initFileStorage();
        prepareData(1, Activities.TYPE_VIDEO);
        prepareData(2, Activities.TYPE_GALLERY);
        ContentValues values = new ContentValues();
        values.put(Downloadable._DOWNLOAD_STATUS, Downloadable.STATUS.QUEUED.ordinal());
        getContentResolver().update(Activities.getActivityUri(1), values, null, null);
        getContentResolver().update(Activities.getActivityUri(2), values, null, null);
    }

    private void prepareData(int id, int type) {
        ContentValues values = new ContentValues();
        values.put(Activities._ID, id);
        values.put(Activities._TYPE, type);
        if (!getContentResolver().query(Activities.getActivityUri(id),
                ActivityTable.ALL_COLUMNS, null, null, null).moveToFirst()) {
            getContentResolver().insert(Activities.CONTENT_URI, values);

        }
    }


    private void initFileStorage() {
        if (imageTestData.hasPreparedData()) return;
        imageTestData = new ImageTestData(this);
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
        imageTestData.setHasPreparedData(true);
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
