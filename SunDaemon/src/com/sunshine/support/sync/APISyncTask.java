package com.sunshine.support.sync;

import android.os.AsyncTask;
import android.util.Log;
import com.sunshine.metadata.database.DBHandler;
import com.sunshine.metadata.database.Table;
import com.sunshine.metadata.database.tables.*;
import com.sunshine.metadata.provider.MetadataContract;
import com.sunshine.support.application.DaemonApplication;
import com.sunshine.support.data.daos.ApiSyncStateDao;
import com.sunshine.support.data.models.ApiSyncState;
import com.sunshine.support.services.APISyncService;

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
    private ApiSyncStateDao apiSyncStateDao;

    public APISyncTask(APISyncService context) {
		this.context = context;

        dbHandler = ((DaemonApplication) context.getApplication()).getMetadataDBHandler();
        apiSyncStateDao = new ApiSyncStateDao(context, dbHandler);
	}

	@Override
	protected Integer doInBackground(String... params) {
        int status = SUCCESS;
		if (isConnected()) {
            Log.v(getClass().getName(), "Device is connected, starting synchronization...");
			for (String tableName: SYNCED_TABLES) {
                Log.v(getClass().getName(), "Synchronizing table: " + tableName);
				Table table = dbHandler.getTableManager(tableName);
                ApiSyncState syncState = apiSyncStateDao.getApiSyncStateForTable(tableName);
				TableSyncManager syncManager = TableSyncManagerFactory.getManager(context, table, syncState);

                syncState.setLastSyncStatus(APISyncStateTable.ApiSyncStates.SYNC_ONGOING);
                syncState.setLastSyncTime(System.currentTimeMillis());
                apiSyncStateDao.persist(syncState);
				if (!syncManager.sync() ) {
                    syncState.setLastSyncStatus(APISyncStateTable.ApiSyncStates.SYNC_FAILURE);
                    apiSyncStateDao.persist(syncState);
                    status = FAILURE;
				} else {
                    syncState.setLastSyncStatus(APISyncStateTable.ApiSyncStates.SYNC_SUCCESS);
                    apiSyncStateDao.persist(syncState);
                }
			}
 		}
		return status;
	}

    protected boolean isConnected() {
		return context.isConnected();
	}
}
