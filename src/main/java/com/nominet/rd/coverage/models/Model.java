package com.nominet.rd.coverage.models;

public interface Model {
    SnapshotResponse snapshot();

    ModelResponse calculate();
}
