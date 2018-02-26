package com.nominet.rd.coverage.models;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum MapType {
    TWO_DIMENSIONAL_GRID("2d-flat");

    private final String name;

    MapType(String mapTypeName) {
        this.name = mapTypeName;
    }

    public String getName() {
        return this.name;
    }

    //String -> Enum

    private static final Map<String, MapType> ENUM_MAP;

    public static MapType get(String name) {
        return ENUM_MAP.get(name);
    }

    static {
        Map<String, MapType> map = new HashMap<>();
        for (MapType instance : MapType.values()) {
            map.put(instance.getName(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }
}
