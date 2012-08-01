package com.sunshine.support.sync;

import android.util.Log;
import com.sunshine.metadata.database.MetadataDBHandlerFactory;
import com.sunshine.metadata.database.Table;
import com.sunshine.metadata.database.tables.AbstractTable;
import com.sunshine.metadata.database.tables.*;

import android.os.AsyncTask;

import com.sunshine.metadata.database.MetadataDBHandler;

public class APISyncTask extends AsyncTask<String, String, Integer> {

	private Table syncTable;
	private MetadataDBHandler dbHandler;
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
			for (String tableName: SYNCED_TABLES) {
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
	
	protected boolean isConnected() {
		return context.isConnected();
	}
}
