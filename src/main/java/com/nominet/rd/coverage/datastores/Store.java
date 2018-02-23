package com.nominet.rd.coverage.datastores;

/**
 * General interface for obtaining a piece
 * of data required by the models
 */
public interface Store<T> {
    StoreKey getKey();

    T getData();
}
