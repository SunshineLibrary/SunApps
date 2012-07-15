package com.sunshine.metadata.database;

public class DatabaseHandlerTest extends DatabaseTestCase {
	
	public void testDatabase() {
		assertTrue(db.isOpen());
		assertTrue(db.getVersion() == 1);
	}
}
