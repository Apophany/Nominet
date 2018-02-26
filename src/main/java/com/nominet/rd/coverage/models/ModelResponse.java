package com.nominet.rd.coverage.models;

public interface ModelResponse {
    boolean didSucceed();

    String serialise();
}
