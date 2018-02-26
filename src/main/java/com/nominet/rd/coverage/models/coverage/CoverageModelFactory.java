package com.nominet.rd.coverage.models.coverage;

import com.nominet.rd.coverage.datastores.StoreManager;
import com.nominet.rd.coverage.models.MapType;
import com.nominet.rd.coverage.models.Model;
import com.nominet.rd.coverage.models.ModelFactory;

import static java.util.Objects.requireNonNull;

/**
 * Model factory for calculating receiver coverage.
 * Currently only 2D flat maps are supported, but
 * if other map types were implemented (such as 2D
 * weighted grids, or 3D maps) this factory could be
 * extended to support those implementations
 */
public final class CoverageModelFactory implements ModelFactory {

    private final MapType mapType;
    private final StoreManager storeManager;

    public CoverageModelFactory(StoreManager storeManager, MapType mapType) {
        this.mapType = requireNonNull(mapType);
        this.storeManager = requireNonNull(storeManager);
    }

    @Override
    public Model create() {
        final Model model;
        switch (mapType) {
            case TWO_DIMENSIONAL_GRID:
                model = new Flat2DCoverageModel(storeManager);
                break;
             default:
                 model = new Flat2DCoverageModel(storeManager);
        }
        return model;
    }
}
