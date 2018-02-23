package com.nominet.rd.coverage.maps;

import com.nominet.rd.coverage.models.MapType;

import java.util.List;

public interface Map {
    /**
     * Gets the type of the map
     */
    MapType getType();

    List<Transmitter> getTransmitters();

    List<Receiver> getReceivers();
}
