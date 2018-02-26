package com.nominet.rd.coverage.models;

public interface ModelManager {
    void updateModelFactory(ModelType modelType, ModelFactory modelFactory);

    boolean removeModelFactory(ModelType modelType);

    Model createModel(ModelType modelType);
}
