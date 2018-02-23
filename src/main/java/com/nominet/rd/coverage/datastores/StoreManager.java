package com.nominet.rd.coverage.datastores;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public final class StoreManager {
    private static final Logger LOG = LogManager.getLogger(StoreManager.class);

    private final Map<StoreKey, Store<?>> stores = new HashMap<>();

    public void addStore(Store<?> store) {
        final Store<?> existing = stores.putIfAbsent(store.getKey(), store);
        if (existing != null) {
            LOG.error("Store {} has already been added", store.getKey());
        }
    }

    @SuppressWarnings("unchecked")
    public <T> Store<T> getStore(StoreKey key) {
        return (Store<T>) stores.get(key);
    }
}
