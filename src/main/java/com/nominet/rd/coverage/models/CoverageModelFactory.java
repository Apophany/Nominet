package com.nominet.rd.coverage.models;

/**
 * Creates the coverage model based on the type of
 * map that we have. Different maps have different
 * characteristics (2d flat, 3d hilly etc) which require
 * different models to determine the extent of the coverage
 */
@FunctionalInterface
public interface CoverageModelFactory {
    CoverageModel create(MapType mapType);
}
