package com.nominet.rd.coverage.models;

import com.nominet.rd.coverage.datastores.Store;
import com.nominet.rd.coverage.maps.Map;
import com.nominet.rd.coverage.models.SnapshotResponse;

import java.util.ArrayList;
import java.util.List;

public final class Flat2DModelContext {
    private final Store<Map> mapStore;

    private Map map;

    public Flat2DModelContext(Store<Map> map) {
        this.mapStore = map;
    }

    public SnapshotResponse snapshot() {
        final List<String> errors = new ArrayList<>();
        boolean didSnapshot = true;

        this.map = mapStore.getData();

        if (this.map == null) {
            errors.add("Failed to find map");
            return new SnapshotResponse(false, errors);
        }

        if (this.map.getTransmitters().isEmpty()) {
            errors.add("Map has no transmitters");
            didSnapshot = false;
        }
        if (this.map.getReceivers().isEmpty()) {
            errors.add("Map has no receivers");
            didSnapshot = false;
        }

        return new SnapshotResponse(didSnapshot, errors);
    }

    public Map getMap() {
        return map;
    }
}
