package com.sunshine.support.sync;

import junit.framework.TestCase;

import org.apache.http.impl.client.DefaultHttpClient;

import android.net.Uri;

import com.sunshine.metadata.database.DatabaseTestCase;
import com.sunshine.metadata.database.tables.APISyncStateTable;
import com.sunshine.metadata.database.tables.PackageTable;
import com.sunshine.metadata.database.tables.Table;

public class TableSynchronizerTest extends DatabaseTestCase {

	private Table syncStateTable;
	private Table table;
	private static Uri root_uri;

	private TableSynchronizer synchronizer;

	static {
		Uri.Builder builder = new Uri.Builder();
		builder.scheme("http");
		builder.authority("127.0.0.1");
		root_uri = builder.build();
	}

	@Override
	public void setUp() {
		super.setUp();
		syncStateTable = dbHandler
				.getTableManager(APISyncStateTable.TABLE_NAME);
		table = dbHandler.getTableManager(PackageTable.TABLE_NAME);
		synchronizer = new TableSynchronizer(table, syncStateTable,
				new DefaultHttpClient(), root_uri);
	}

	@Override
	public void tearDown() {
		super.tearDown();
	}

	public void testGetLastUpdateTime() {
		assertEquals(0, synchronizer.getLastUpdateTime());
	}

}
