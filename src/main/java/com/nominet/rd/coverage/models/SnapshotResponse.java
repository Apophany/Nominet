package com.nominet.rd.coverage.models;

import java.util.List;

public final class SnapshotResponse implements ModelResponse {
    private final boolean succeeded;
    private final List<String> errors;

    public SnapshotResponse(boolean succeeded, List<String> errors) {
        this.succeeded = succeeded;
        this.errors = errors;
    }

    public boolean didSucceed() {
        return succeeded;
    }

    @Override
    public String serialise() {
        return String.format("Failed to snapshot model: %s", errors.toString());
    }
}
