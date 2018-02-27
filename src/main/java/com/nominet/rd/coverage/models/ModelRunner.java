package com.nominet.rd.coverage.models;

import com.nominet.rd.coverage.publishers.Publisher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Basic class for wrapping model creation and running
 * functionality. Could easily be used with a thread
 * safe publisher to run distributed tasks
 */
public final class ModelRunner {

    private static final Logger LOG = LogManager.getLogger(ModelRunner.class);

    private final ModelManager modelManager;
    private final Publisher publisher;

    public ModelRunner(ModelManager modelManager, Publisher publisher) {
        this.modelManager = modelManager;
        this.publisher = publisher;
    }

    /**
     * Runs the specified model and publishes the result
     */
    public boolean run(ModelType modelType) {
        final Model model = modelManager.createModel(modelType);
        final SnapshotResponse snapshot = model.snapshot();

        if (!snapshot.didSucceed()) {
            publisher.publish(snapshot);
            return false;
        }

        LOG.info("Model snapshot succeeded");

        final ModelResponse response = model.calculate();
        publisher.publish(response);

        return response.didSucceed();
    }
}
