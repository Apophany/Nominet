package com.nominet.rd.coverage.models.coverage;

import com.nominet.rd.coverage.datastores.Store;
import com.nominet.rd.coverage.maps.Map;
import com.nominet.rd.coverage.models.Context;

import java.util.List;

public class Flat2DCoverageContext implements Context {
    private final Store<Map> mapStore;

    private Map map;

    public Flat2DCoverageContext(Store<Map> map) {
        this.mapStore = map;
    }

    public boolean snapshot(List<String> errors) {
        this.map = mapStore.getData();
        if (this.map == null) {
            errors.add("Failed to find map");
            return false;
        }

        boolean isValid = true;
        if (this.map.getTransmitters().isEmpty()) {
            errors.add("Map has no transmitters");
            isValid = false;
        }
        if (this.map.getReceivers().isEmpty()) {
            errors.add("Map has no receivers");
            isValid = false;
        }

        return isValid;
    }

    public Map getMap() {
        return map;
    }
}
