package com.nominet.rd.coverage.datastores.map;

import com.nominet.rd.coverage.datastores.Store;
import com.nominet.rd.coverage.datastores.StoreKey;
import com.nominet.rd.coverage.maps.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**
 * Loads a map object from a file
 */
public final class MapFileDataStore implements Store<Map> {

    private static final Logger LOG = LogManager.getLogger(MapFileDataStore.class);

    private final Map map;

    public MapFileDataStore(String fileLocation, Function<List<String>, Map> converter) {
        if (fileLocation == null || fileLocation.isEmpty()) {
            throw new IllegalArgumentException("File location cannot be null or empty");
        }
        if (converter == null) {
            throw new IllegalArgumentException("Map converter cannot be null");
        }
        this.map = requireNonNull(loadMap(fileLocation, converter));
    }

    @Override
    public StoreKey getKey() {
        return StoreKey.MapFile;
    }

    @Override
    public Map getData() {
        return map;
    }

    private Map loadMap(String fileLocation, Function<List<String>, Map> converter) {
        LOG.info("Loading map from: {}", fileLocation);

        final List<String> lines = new ArrayList<>();

        final Path path = Paths.get(fileLocation);
        try(BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            LOG.error("Exception occurred while reading file: {}", e.getMessage());
            return null;
        }

        try {
            return converter.apply(lines);
        } catch (Exception e) {
            LOG.error("Failed parsing the map file: {}", e.getMessage());
            return null;
        }
    }
}
