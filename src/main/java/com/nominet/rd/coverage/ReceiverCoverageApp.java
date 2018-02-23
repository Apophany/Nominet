package com.nominet.rd.coverage;

import com.nominet.rd.coverage.datastores.StoreManager;

public class ReceiverCoverageApp {
    public static void main(String[] args) {
        final ReceiverCoverageApp app = new ReceiverCoverageApp();
    }

    public ReceiverCoverageApp() {
        final AppFactory appFactory = new AppFactory("");

        final StoreManager storeManager = appFactory.createStoreManager();
        storeManager.addStore(appFactory.createMapFileStore());
    }
}
