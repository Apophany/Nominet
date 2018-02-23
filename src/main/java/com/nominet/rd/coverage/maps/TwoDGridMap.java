package com.nominet.rd.coverage.maps;

import com.nominet.rd.coverage.models.MapType;

import java.util.List;

public class TwoDGridMap implements Map {

    private final int xSize;
    private final int ySize;
    private final List<Transmitter> transmitters;
    private final List<Receiver> receivers;

    public TwoDGridMap(int xSize, int ySize, List<Transmitter> transmitters, List<Receiver> receivers) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.transmitters = transmitters;
        this.receivers = receivers;
    }

    @Override
    public MapType getType() {
        return MapType.TWO_DIMENSIONAL_GRID;
    }

    @Override
    public List<Receiver> getReceivers() {
        return receivers;
    }

    @Override
    public List<Transmitter> getTransmitters() {
        return transmitters;
    }

    public int getXMapSize() {
        return xSize;
    }

    public int getYMapSize() {
        return ySize;
    }
}
