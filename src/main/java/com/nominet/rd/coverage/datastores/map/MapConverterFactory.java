package com.nominet.rd.coverage.datastores.map;

import com.nominet.rd.coverage.maps.Map;
import com.nominet.rd.coverage.models.MapType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.function.Function;

public class MapConverterFactory {

    private static final Logger LOG = LogManager.getLogger(MapConverterFactory.class);

    public Function<List<String>, Map> createConverter(MapType mapType) {
        switch (mapType) {
            case TWO_DIMENSIONAL_GRID:
                return new TwoDGridFileConverter();
             default:
                 throw new IllegalArgumentException("Could not create converter for " + mapType);
        }
    }
}
