package com.nominet.rd.coverage.models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static java.util.Objects.requireNonNull;

public final class DelegatingModelManager implements ModelManager {

    private static final Logger LOG = LogManager.getLogger(DelegatingModelManager.class);

    private final Map<ModelType, ModelFactory> modelFactories;

    public DelegatingModelManager(Map<ModelType, ModelFactory> modelFactories) {
        this.modelFactories = requireNonNull(modelFactories);
    }

    @Override
    public void updateModelFactory(ModelType modelType, ModelFactory modelFactory) {
        final ModelFactory old = modelFactories.put(modelType, modelFactory);
        if (old != null) {
            LOG.info("Factory for model: {}, updated from: {} to {}", modelType, old, modelFactory);
        }
    }

    @Override
    public boolean removeModelFactory(ModelType modelType) {
        return modelFactories.remove(modelType) != null;
    }

    @Override
    public Model createModel(ModelType modelType) {
        switch (modelType) {
            case Coverage:
                return modelFactories.get(ModelType.Coverage).create();
            default:
                throw new IllegalArgumentException("Invalid model selection: " + modelType);
        }
    }
}
