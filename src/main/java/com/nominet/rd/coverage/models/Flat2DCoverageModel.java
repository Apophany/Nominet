package com.nominet.rd.coverage.models;

import com.nominet.rd.coverage.datastores.Store;
import com.nominet.rd.coverage.datastores.StoreKey;
import com.nominet.rd.coverage.datastores.StoreManager;
import com.nominet.rd.coverage.maps.Map;
import com.nominet.rd.coverage.maps.Receiver;
import com.nominet.rd.coverage.maps.Tower;
import com.nominet.rd.coverage.maps.Transmitter;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;

/**
 * This model calculates the coverage of a set of m receivers on a map
 * of n transmitters. A receiver is said to be covered if there is a
 * transmitter of power (p), a chebyshev distance (d) of d <= p away.
 *
 * The model will also calculate the minimum required power increase
 * before all receivers become covered. This is done using a recursive
 * path finding algorithm.
 **/
public final class Flat2DCoverageModel implements Model {

    private final Flat2DModelContext context;

    public Flat2DCoverageModel(StoreManager storeManager) {
        final Store<Map> mapStore = storeManager.getStore(StoreKey.MapFile);
        this.context = new Flat2DModelContext(mapStore);
    }

    @Override
    public SnapshotResponse snapshot() {
        return context.snapshot();
    }

    @Override
    public ModelResponse calculate() {
        final Map map = context.getMap();
        final List<Receiver> receivers = map.getReceivers();
        final List<Transmitter> transmitters = map.getTransmitters();

        final List<Receiver> coveredReceivers = findCoveredReceivers(
                receivers,
                transmitters
        );
        final List<Receiver> uncoveredReceivers = new ArrayList<>(receivers);
        uncoveredReceivers.removeAll(coveredReceivers);

        final IncreaseTracker powerIncrease = new IncreaseTracker();
        final IncreaseTracker minPowerIncrease = findMinimumPowerIncrease(
                new ArrayList<>(uncoveredReceivers),
                new ArrayList<>(transmitters),
                powerIncrease
        );

        return new SimpleFlat2DCoverageModelResponse(coveredReceivers.size(), receivers.size(), minPowerIncrease);
    }

    private List<Receiver> findCoveredReceivers(List<Receiver> receivers, List<Transmitter> transmitters) {
        final List<Receiver> covered = new ArrayList<>();

        for (Receiver receiver : receivers) {
            for (Transmitter transmitter : transmitters) {
                final int[] receiverPos = getPositionArray(receiver);
                final int[] transmitterPos = getPositionArray(transmitter);

                final int transmitterPower = transmitter.getPower();
                final int distance = chebyshevDistance(receiverPos, transmitterPos);

                if (distance <= transmitterPower) {
                    covered.add(receiver);
                    break;
                }
            }
        }
        return covered;
    }

    /**
     * Really inefficient path finding algorithm, since we're copying the arrays on each recursion
     * and we're not doing any memoisation to prevent repeated invocations of subpaths.
     *
     * Since this is an optimisation/minimisation problem, we might also be able to use some
     * linear programming techniques
     */
    public IncreaseTracker findMinimumPowerIncrease(
            List<Receiver> receivers,
            List<Transmitter> transmitters,
            IncreaseTracker tracker
    ) {
        if (receivers.size() == 0) {
            return tracker;
        }
        IncreaseTracker minPowerIncrease = new IncreaseTracker();
        minPowerIncrease.totalPower = Integer.MAX_VALUE;

        for (Receiver receiver : receivers) {
            for (Transmitter transmitter : transmitters) {
                final int[] receiverPos = getPositionArray(receiver);
                final int[] transmitterPos = getPositionArray(transmitter);

                final int transmitterPower = transmitter.getPower();
                final int distance = chebyshevDistance(receiverPos, transmitterPos);
                final int amountToPowerUp = Math.max(distance - transmitterPower, 0);

                //We've covered this receiver now - so no longer needs to be calculated in this path
                final List<Receiver> uncoveredReceivers = new ArrayList<>(receivers);
                uncoveredReceivers.remove(receiver);

                final List<Transmitter> increasedTransmitters = new ArrayList<>(transmitters);
                final Transmitter poweredUpTransmitter = new Transmitter(
                        transmitter.getId(),
                        transmitter.getXCoordinate(),
                        transmitter.getYCoordinate(),
                        transmitterPower + amountToPowerUp
                );
                increasedTransmitters.remove(transmitter);
                increasedTransmitters.add(poweredUpTransmitter);

                final IncreaseTracker updatedTracker = updateTracker(
                        tracker,
                        amountToPowerUp,
                        poweredUpTransmitter
                );

                final IncreaseTracker localIncrease = findMinimumPowerIncrease(
                        uncoveredReceivers,
                        increasedTransmitters,
                        updatedTracker
                );

                if (localIncrease.totalPower < minPowerIncrease.totalPower) {
                    minPowerIncrease = localIncrease;
                }
            }
        }

        return minPowerIncrease;
    }

    private IncreaseTracker updateTracker(IncreaseTracker tracker, int amountToPowerUp, Transmitter poweredUpTransmitter) {
        final IncreaseTracker updatedTracker = new IncreaseTracker();
        updatedTracker.totalPower = tracker.totalPower + amountToPowerUp;
        updatedTracker.transmitters = new ArrayList<>(tracker.transmitters);
        if (!updatedTracker.transmitters.contains(poweredUpTransmitter)) {
            updatedTracker.transmitters.add(poweredUpTransmitter);
        }
        return updatedTracker;
    }

    private int[] getPositionArray(Tower tower) {
        return new int[]{
                tower.getXCoordinate(),
                tower.getYCoordinate()
        };
    }

    /**
     * Chebyshev distance for 2-dimensional vector is given
     * by max(|x_2 - x_1|, |y_2 - y_1|), that is, it is the
     * maximum distance along any given axis
     */
    private int chebyshevDistance(int[] receiver, int[] transmitter) {
        if (receiver.length != 2 || transmitter.length != 2) {
            return -1;
        }
        return max(abs(receiver[0] - transmitter[0]), abs(receiver[0] - transmitter[0]));
    }

    private static class IncreaseTracker {
        int totalPower = 0;
        List<Transmitter> transmitters = new ArrayList<>();
    }

    private static class SimpleFlat2DCoverageModelResponse implements ModelResponse {

        private final int coveredReceivers;
        private final int totalReceivers;
        private final IncreaseTracker tracker;

        SimpleFlat2DCoverageModelResponse(int coveredReceivers, int totalReceivers, IncreaseTracker tracker) {
            this.coveredReceivers = coveredReceivers;
            this.totalReceivers = totalReceivers;
            this.tracker = tracker;
        }

        @Override
        public boolean didSucceed() {
            return true;
        }

        @Override
        public String serialise() {
            final StringBuilder builder = new StringBuilder();
            builder.append(String.format("%d/%d", coveredReceivers, totalReceivers));

            for (Transmitter t : tracker.transmitters) {
                builder.append("\n");
                builder.append(String.format("%d %d", t.getId(), t.getPower()));
            }

            return builder.toString();
        }
    }
}
