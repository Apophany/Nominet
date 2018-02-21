package com.nominet.rd.coverage.datastores;

import com.nominet.rd.coverage.maps.Map;
import com.nominet.rd.coverage.maps.Receiver;
import com.nominet.rd.coverage.maps.Transmitter;
import com.nominet.rd.coverage.maps.TwoDGridMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

import static java.lang.String.format;

/**
 * Converter function which takes a set of lines representing
 * a file, and returns the corresponding map object representation
 */
public final class TwoDGridFileConverter implements Function<List<String>, Map> {

    private static final Logger LOG = LogManager.getLogger(TwoDGridMap.class);

    private static final Pattern SIZE_PATTERN = Pattern.compile("^\\d+ \\d+$");
    private static final Pattern TRANSMITTER_PATTERN = Pattern.compile("^\\d+ \\d+ \\d+ \\d+$");
    private static final Pattern RECEIVER_PATTERN = Pattern.compile("^\\d+ \\d+ \\d+");

    @Override
    public Map apply(List<String> fileLines) {
        final List<Transmitter> transmitters = new ArrayList<>();
        final List<Receiver> receivers = new ArrayList<>();

        int xSize = 0;
        int ySize = 0;

        for (String line : fileLines) {
            if (SIZE_PATTERN.matcher(line).matches()) {
                final String[] sizes = line.split(" ");
                xSize = Integer.valueOf(sizes[0]);
                ySize = Integer.valueOf(sizes[1]);
            }
            else if (TRANSMITTER_PATTERN.matcher(line).matches()) {
                addTransmitter(transmitters, xSize, ySize, line);
            }
            else if (RECEIVER_PATTERN.matcher(line).matches()) {
                addReceiver(receivers, xSize, ySize, line);
            }
            else {
                throw new IllegalStateException(format("Unexpected line in the map file: %s", line));
            }
        }

        if (xSize == 0 || ySize == 0) {
            throw new IllegalStateException("Map file did not contain valid map size");
        }

        return new TwoDGridMap(xSize, ySize, transmitters, receivers);
    }

    private void addReceiver(List<Receiver> receivers, int xSize, int ySize, String line) {
        final Receiver receiver = createReceiver(line, xSize, ySize);
        if (receiver == null) {
            throw new IllegalStateException("Invalid receiver data in map");
        }
        receivers.add(receiver);
    }

    private void addTransmitter(List<Transmitter> transmitters, int xSize, int ySize, String line) {
        final Transmitter transmitter = createTransmitter(line, xSize, ySize);
        if (transmitter == null) {
            throw new IllegalStateException("Invalid transmitter data in map");
        }
        transmitters.add(transmitter);
    }

    private Receiver createReceiver(String line, int mapXSize, int mapYSize) {
        final String[] receiver = line.split(" ");
        Receiver r = new Receiver(
                Integer.valueOf(receiver[0]),
                Integer.valueOf(receiver[1]),
                Integer.valueOf(receiver[2])
        );

        if (r.getXCoordinate() > mapXSize || r.getYCoordinate() > mapYSize) {
            LOG.error("Receiver {} is outside of bounds: X: {}; Y: {}", r, mapXSize, mapYSize);
            r = null;
        }

        return r;
    }

    private Transmitter createTransmitter(String line, int mapXSize, int mapYSize) {
        final String[] transmitter = line.split(" ");
        Transmitter t = new Transmitter(
                Integer.valueOf(transmitter[0]),
                Integer.valueOf(transmitter[1]),
                Integer.valueOf(transmitter[2]),
                Integer.valueOf(transmitter[3])
        );

        if (t.getXCoordinate() > mapXSize || t.getYCoordinate() > mapYSize) {
            LOG.error("Transmitter {} is outside of bounds: X: {}; Y: {}", transmitter, mapXSize, mapYSize);
            t = null;
        }

        return t;
    }
}
