package com.nominet.rd.coverage.models;

import com.nominet.rd.coverage.datastores.StoreManager;

import java.util.List;

public interface Model<T extends Context> {
    T createContext(StoreManager storeManager);

    boolean snapshot(T context, List<String> errors);

    void calculate(T context, List<String> errors);
}
