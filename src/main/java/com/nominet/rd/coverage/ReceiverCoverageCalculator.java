package com.nominet.rd.coverage;

import com.nominet.rd.coverage.datastores.StoreManager;
import com.nominet.rd.coverage.models.ModelFactory;
import com.nominet.rd.coverage.models.ModelRunner;
import com.nominet.rd.coverage.models.ModelType;
import com.nominet.rd.coverage.publishers.Publisher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Properties;

/**
 * Main app class. Delegates to the app factory to build
 * the dependency hierarchy for the app and initialises
 * the models to be run
 */
public final class ReceiverCoverageCalculator {

    private static final Logger LOG = LogManager.getLogger(ReceiverCoverageCalculator.class);

    private final ModelRunner modelRunner;

    public ReceiverCoverageCalculator(Properties appProperties) {
        final AppFactory appFactory = new AppFactory(appProperties);

        final StoreManager storeManager = appFactory.createStoreManager();
        storeManager.addStore(appFactory.createMapFileStore());

        final Publisher publisher = appFactory.createPublisher();
        final HashMap<ModelType, ModelFactory> models = appFactory.createModelFactories(storeManager);

        this.modelRunner = appFactory.createModelRunner(models, publisher);
    }

    public void run() {
        LOG.info("Running coverage model");
        modelRunner.run(ModelType.Coverage);
    }
}
