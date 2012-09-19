package com.ssl.support.sync;

import android.os.AsyncTask;
import android.util.Log;
import com.ssl.metadata.database.tables.*;
import com.ssl.metadata.database.DBHandler;
import com.ssl.metadata.database.Table;
import com.ssl.support.application.DaemonApplication;
import com.ssl.support.data.helpers.ApiSyncStateHelper;
import com.ssl.support.data.models.ApiSyncState;
import com.ssl.support.services.APISyncService;

public class APISyncTask extends AsyncTask<String, String, Integer> {

	private Table syncTable;
	private DBHandler dbHandler;
	private APISyncService context;

	private static final String[] SYNCED_TABLES = {
            // Courses
            CourseTable.TABLE_NAME,
            ChapterTable.TABLE_NAME,
            LessonTable.TABLE_NAME,
            SectionTable.TABLE_NAME,

            // Activities
            ActivityTable.TABLE_NAME,
            GalleryImageTable.TABLE_NAME,
            QuizComponentsTable.TABLE_NAME,
            SectionComponentsTable.TABLE_NAME,
            ProblemTable.TABLE_NAME,
            ProblemChoiceTable.TABLE_NAME,

            // Books
            AuthorTable.TABLE_NAME,
            BookTable.TABLE_NAME,
            BookCollectionTable.TABLE_NAME,
            TagTable.TABLE_NAME,
            BookTagTable.TABLE_NAME,
            BookCollectionTagTable.TABLE_NAME,

    };

	public static final int SUCCESS = 0;
	public static final int FAILURE = -1;

    public APISyncTask(APISyncService context) {
		this.context = context;

        dbHandler = ((DaemonApplication) context.getApplication()).getMetadataDBHandler();
	}

	@Override
	protected Integer doInBackground(String... params) {
        int status = SUCCESS;
		if (isConnected()) {
            Log.v(getClass().getName(), "Device is connected, starting synchronization...");
			for (String tableName: SYNCED_TABLES) {
                Log.v(getClass().getName(), "Synchronizing table: " + tableName);
				Table table = dbHandler.getTableManager(tableName);

                ApiSyncState syncState = ApiSyncStateHelper.getOrCreateNewFromTableName(context, tableName);
                syncState.lastSyncStatus = APISyncStateTable.ApiSyncStates.SYNC_ONGOING;
                syncState.lastSyncTime = System.currentTimeMillis();
                ApiSyncStateHelper.saveApiSyncState(context, syncState);

                TableSyncManager syncManager = TableSyncManagerFactory.getManager(context, table, syncState);


				if (!syncManager.sync() ) {
                    syncState.lastSyncStatus = APISyncStateTable.ApiSyncStates.SYNC_FAILURE;
                    ApiSyncStateHelper.saveApiSyncState(context, syncState);
                    status = FAILURE;
				} else {
                    syncState.lastSyncStatus = APISyncStateTable.ApiSyncStates.SYNC_SUCCESS;
                    ApiSyncStateHelper.saveApiSyncState(context, syncState);
                }
			}
 		}
		return status;
	}

    protected boolean isConnected() {
		return context.isConnected();
	}
}
