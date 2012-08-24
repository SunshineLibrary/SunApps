package com.sunshine.support.application;

import com.sunshine.metadata.database.DBHandler;
import com.sunshine.metadata.database.MetadataDBHandlerFactory;
import com.sunshine.metadata.database.SystemDBHandlerFactory;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class DaemonApplication extends MessageApplication {

    private DBHandler metadataDBHandler;
    private DBHandler systemDBHandler;

    public synchronized DBHandler getMetadataDBHandler() {
        if (metadataDBHandler == null) {
            metadataDBHandler = MetadataDBHandlerFactory.newMetadataDBHandler(this);
        }
        return metadataDBHandler;
    }

    public synchronized DBHandler getSystemDBHandler(){
        if (systemDBHandler == null) {
            systemDBHandler = SystemDBHandlerFactory.newSystemDBHandler(this);
        }
        return systemDBHandler;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        closeHandlers();
    }

    private void closeHandlers() {
        closeDBHandler(metadataDBHandler);
        closeDBHandler(systemDBHandler);
    }

    private void closeDBHandler(DBHandler dbHandler) {
        if (dbHandler != null) {
            dbHandler.close();
        }
    }
}
