package com.ssl.support.application;

import com.ssl.metadata.database.DBHandler;
import com.ssl.metadata.database.MetadataDBHandlerFactory;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class DaemonApplication extends MessageApplication {

    private DBHandler metadataDBHandler;

    public synchronized DBHandler getMetadataDBHandler() {
        if (metadataDBHandler == null) {
            metadataDBHandler = MetadataDBHandlerFactory.newMetadataDBHandler(this);
        }
        return metadataDBHandler;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        closeHandler();
    }

    private void closeHandler() {
        if (metadataDBHandler != null) {
            metadataDBHandler.close();
        }
    }

}
