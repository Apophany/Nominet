package com.nominet.rd.coverage.datastores;

import com.nominet.rd.coverage.maps.*;
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
//TODO: Handling for duplicate IDs
public final class TwoDGridFileConverter implements Function<List<String>, Map> {

    private static final Logger LOG = LogManager.getLogger(TwoDGridMap.class);

    private static final String SEPARATOR = " ";
    private static final Pattern SIZE_PATTERN = Pattern.compile("^\\d+ \\d+$");
    private static final Pattern TRANSMITTER_PATTERN = Pattern.compile("^\\d+ \\d+ \\d+ \\d+$");
    private static final Pattern RECEIVER_PATTERN = Pattern.compile("^\\d+ \\d+ \\d+");

    @Override
    public Map apply(List<String> fileLines) {
        final List<Transmitter> transmitters = new ArrayList<>();
        final List<Receiver> receivers = new ArrayList<>();

        int xMapSize = 0;
        int yMapSize = 0;

        for (String line : fileLines) {
            if (SIZE_PATTERN.matcher(line).matches()) {
                final String[] sizes = line.split(SEPARATOR);
                xMapSize = Integer.valueOf(sizes[0]);
                yMapSize = Integer.valueOf(sizes[1]);
            }
            else if (TRANSMITTER_PATTERN.matcher(line).matches()) {
                final Transmitter transmitter = createTransmitter(line);
                if (validTower(transmitter, xMapSize, yMapSize)) {
                    transmitters.add(transmitter);
                }
            }
            else if (RECEIVER_PATTERN.matcher(line).matches()) {
                final Receiver receiver = createReceiver(line);
                if (validTower(receiver, xMapSize, yMapSize)) {
                    receivers.add(receiver);
                }
            }
            else {
                throw new IllegalStateException(format("Unexpected line in the map file: %s", line));
            }
        }

        if (xMapSize == 0 || yMapSize == 0) {
            throw new IllegalStateException("Map file did not contain valid map size");
        }

        return new TwoDGridMap(xMapSize, yMapSize, transmitters, receivers);
    }

    private Transmitter createTransmitter(String line) {
        final String[] transmitter = line.split(SEPARATOR);
        return new Transmitter(
                Integer.valueOf(transmitter[0]),
                Integer.valueOf(transmitter[1]),
                Integer.valueOf(transmitter[2]),
                Integer.valueOf(transmitter[3])
        );
    }

    private Receiver createReceiver(String line) {
        final String[] receiver = line.split(SEPARATOR);
        return new Receiver(
                Integer.valueOf(receiver[0]),
                Integer.valueOf(receiver[1]),
                Integer.valueOf(receiver[2])
        );
    }

    private boolean validTower(Tower transmitter, int xMapSize, int yMapSize) {
        boolean isValid = true;
        if (transmitter.getXCoordinate() > xMapSize || transmitter.getYCoordinate() > yMapSize) {
            LOG.error("Tower {} is outside of bounds: x: {}; y: {}", transmitter, xMapSize, yMapSize);
            isValid = false;
        }
        return isValid;
    }
}
