package com.sunshine.support.sync;

import android.os.AsyncTask;
import android.util.Log;
import com.sunshine.metadata.database.DBHandler;
import com.sunshine.metadata.database.MetadataDBHandlerFactory;
import com.sunshine.metadata.database.Table;
import com.sunshine.metadata.database.tables.*;

public class APISyncTask extends AsyncTask<String, String, Integer> {

	private Table syncTable;
	private DBHandler dbHandler;
	private APISyncService context;

	private static final String[] SYNCED_TABLES = {
            CourseTable.TABLE_NAME,
            ChapterTable.TABLE_NAME,
            LessonTable.TABLE_NAME,
            SectionTable.TABLE_NAME,
            ActivityTable.TABLE_NAME,
            GalleryImageTable.TABLE_NAME,
            BookTable.TABLE_NAME,
            BookCollectionTable.TABLE_NAME,
            ProblemTable.TABLE_NAME,
            ProblemChoiceTable.TABLE_NAME,
            QuizComponentsTable.TABLE_NAME,
            SectionComponentsTable.TABLE_NAME,
    };

	public static final int SYNC_SUCCESS = 0;
	public static final int SYNC_FAILURE = -1;
	
	public APISyncTask(APISyncService context) {
		this.context = context;

		dbHandler = MetadataDBHandlerFactory.newMetadataDBHandler(context);
		syncTable = dbHandler.getTableManager(APISyncStateTable.TABLE_NAME);
	}

	@Override
	protected Integer doInBackground(String... params) {
		int status = SYNC_SUCCESS;
		if (isConnected()) {
            Log.v(getClass().getName(), "Device is connected, starting synchronization...");
			for (String tableName: SYNCED_TABLES) {
                Log.v(getClass().getName(), "Synchronizing table: " + tableName);
				Table table = dbHandler.getTableManager(tableName);
				TableSyncManager syncManager = new TableSyncManager(table, syncTable);
				if (!syncManager.sync() ) {
					status = SYNC_FAILURE;
					break;
				}
			}
 		}
		return status;
	}

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        dbHandler.close();
    }

    protected boolean isConnected() {
		return context.isConnected();
	}
}
