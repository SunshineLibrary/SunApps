package com.sunshine.test.suites;

import com.sunshine.metadata.database.DatabaseHandlerTest;
import com.sunshine.metadata.database.tables.BookTableTest;
import com.sunshine.metadata.database.tables.PackageTableTest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DatabaseTests extends TestCase {
	
	public static Test suite() {
		TestSuite suite = new TestSuite();
		
		suite.addTest(new TestSuite(DatabaseHandlerTest.class));
		suite.addTest(new TestSuite(PackageTableTest.class));
		suite.addTest(new TestSuite(BookTableTest.class));

		
		return suite; 
	}

}
