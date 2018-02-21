package com.nominet.rd.coverage.datastores;

/**
 * General interface for obtaining a piece
 * of data required by the models
 */
@FunctionalInterface
public interface Store<T> {
    T getData();
}
