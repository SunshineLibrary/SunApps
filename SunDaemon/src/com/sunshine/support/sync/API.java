package com.sunshine.support.sync;

import java.util.HashMap;
import java.util.Map;

import com.sunshine.metadata.database.tables.PackageTable;

public class API {
	
	private static final Map<String, String> apiMap;
	
	static {
		apiMap = new HashMap<String, String>();
		apiMap.put(PackageTable.TABLE_NAME, "packages");
	}
	
	public static String getApiPath(String tableName) {
		return apiMap.get(tableName);
	}
}
