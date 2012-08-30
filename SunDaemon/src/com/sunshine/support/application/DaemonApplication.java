package com.sunshine.support.application;

import com.sunshine.metadata.database.DBHandler;
import com.sunshine.metadata.database.MetadataDBHandlerFactory;
import com.sunshine.metadata.database.SystemDBHandlerFactory;
import com.sunshine.support.api.ApiClient;
import com.sunshine.support.config.Configurations;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class DaemonApplication extends MessageApplication {

    private DBHandler metadataDBHandler;
    private DBHandler systemDBHandler;
    private Configurations configs;
    private ApiClient apiClient;

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

    public Configurations getConfigurations() {
        if (configs == null) {
            configs = new Configurations(this);
        }
        return configs;
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
