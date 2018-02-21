package com.nominet.rd.coverage.models;

import com.nominet.rd.coverage.maps.Map;

/**
 * Calculates the extent of the coverage i.e
 * the number of receivers which lie within
 * the range of any of the given transmitters
 */
public interface CoverageModel {
    double calculateCoverage(Map map);
}
