package com.nominet.rd.coverage;

import com.nominet.rd.coverage.datastores.MapFileDataStore;
import com.nominet.rd.coverage.datastores.Store;
import com.nominet.rd.coverage.datastores.StoreManager;
import com.nominet.rd.coverage.datastores.TwoDGridFileConverter;
import com.nominet.rd.coverage.maps.Map;

/**
 * Factory for creating classes required for execution.
 * Used in order to faciliate 'poor mans' dependency injection
 * and avoid the use of heavy dependency injection frameworks
 */
public final class AppFactory {

    private final String fileLocation;

    public AppFactory(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public Store<Map> createMapFileStore() {
        return new MapFileDataStore(fileLocation, new TwoDGridFileConverter());
    }

    public StoreManager createStoreManager() {
        return new StoreManager();
    }
}
