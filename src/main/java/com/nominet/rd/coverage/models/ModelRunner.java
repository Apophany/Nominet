package com.nominet.rd.coverage.models;

import com.nominet.rd.coverage.datastores.StoreManager;

public class ModelRunner {
    private final ModelManager modelManager;
    private final StoreManager storeManager;

    public ModelRunner(ModelManager modelManager, StoreManager storeManager) {
        this.modelManager = modelManager;
        this.storeManager = storeManager;
    }

    void onRequest() {

    }
}
