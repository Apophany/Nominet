package com.nominet.rd.coverage.models.coverage;

import com.nominet.rd.coverage.models.MapType;
import com.nominet.rd.coverage.models.Model;

/**
 * Creates the coverage model based on the type of
 * map that we have. Different maps have different
 * characteristics (2d flat, 3d hilly etc) which require
 * different models to determine the extent of the coverage
 */
@FunctionalInterface
public interface CoverageModelFactory {
    Model create(MapType mapType);
}
