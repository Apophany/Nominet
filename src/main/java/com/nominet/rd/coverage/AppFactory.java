package com.nominet.rd.coverage;

import com.nominet.rd.coverage.datastores.map.MapConverterFactory;
import com.nominet.rd.coverage.datastores.map.MapFileDataStore;
import com.nominet.rd.coverage.datastores.Store;
import com.nominet.rd.coverage.datastores.StoreManager;
import com.nominet.rd.coverage.maps.Map;
import com.nominet.rd.coverage.models.*;
import com.nominet.rd.coverage.models.CoverageModelFactory;
import com.nominet.rd.coverage.publishers.ConsolePublisher;
import com.nominet.rd.coverage.publishers.Publisher;

import java.util.HashMap;
import java.util.Properties;

/**
 * Factory for creating classes required for execution.
 * Used in order to faciliate 'poor mans' dependency injection
 * and avoid the use of heavy dependency injection frameworks
 */
public final class AppFactory {

    private static final String FILE_PROPERTY = "receiver-coverage.files.map";
    private static final String MAP_TYPE_PROPERTY = "receiver-coverage.maps.type";

    private final Properties properties;

    public AppFactory(Properties properties) {
        this.properties = properties;
    }

    public Store<Map> createMapFileStore() {
        final MapConverterFactory converterFactory = new MapConverterFactory();

        final String mapFile = properties.getProperty(FILE_PROPERTY);
        final MapType mapType = MapType.get(properties.getProperty(MAP_TYPE_PROPERTY));
        if (mapType == null) {
            throw new IllegalStateException("A valid map type must be specified in the properties");
        }

        return new MapFileDataStore(mapFile, converterFactory.createConverter(mapType));
    }

    public StoreManager createStoreManager() {
        return new StoreManager();
    }

    public Publisher createPublisher() {
        return new ConsolePublisher();
    }

    public ModelRunner createModelRunner(HashMap<ModelType, ModelFactory> models, Publisher publisher) {
        final ModelManager modelManager = new DelegatingModelManager(models);
        return new ModelRunner(modelManager, publisher);
    }

    public HashMap<ModelType, ModelFactory> createModelFactories(StoreManager storeManager) {
        final HashMap<ModelType, ModelFactory> modelFactories = new HashMap<>();
        final MapType mapType = MapType.get(properties.getProperty(MAP_TYPE_PROPERTY));
        modelFactories.put(ModelType.Coverage, new CoverageModelFactory(storeManager, mapType));
        return modelFactories;
    }
}
