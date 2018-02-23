package com.nominet.rd.coverage.models;

import com.nominet.rd.coverage.models.coverage.CoverageModelFactory;

import static java.util.Objects.requireNonNull;

public class ModelManager {
    private final CoverageModelFactory coverageModelFactory;

    public ModelManager(CoverageModelFactory coverageModelFactory) {
        this.coverageModelFactory = requireNonNull(coverageModelFactory);
    }

    public Model<?> createModel(ModelType modelType) {
        switch (modelType) {
            case Coverage:
                return coverageModelFactory.create(MapType.TWO_DIMENSIONAL_GRID);
            default:
                throw new IllegalArgumentException("Invalid model selection: " + modelType);
        }
    }
}
