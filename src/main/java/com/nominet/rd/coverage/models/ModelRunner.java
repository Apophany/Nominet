package com.nominet.rd.coverage.models;

public interface ModelRunner<T, R> {
    R run(T t);
}
