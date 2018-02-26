package com.nominet.rd.coverage.models.coverage;

import com.nominet.rd.coverage.datastores.Store;
import com.nominet.rd.coverage.datastores.StoreKey;
import com.nominet.rd.coverage.datastores.StoreManager;
import com.nominet.rd.coverage.maps.Map;
import com.nominet.rd.coverage.maps.Receiver;
import com.nominet.rd.coverage.maps.Transmitter;
import com.nominet.rd.coverage.models.Model;
import com.nominet.rd.coverage.models.ModelResponse;
import com.nominet.rd.coverage.models.SnapshotResponse;

import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.max;

/**
 * This model calculates the coverage of a set of m receivers on a map
 * of n transmitters. A receiver is said to be covered if there is a
 * transmitter of power (p), a chebyshev distance (d) of d <= p away.
 *
 * This is done by calculating the chebyshev distance between receiver i
 * and the n transmitters, for all i = 1 -> m, stopping once we have found
 * a transmitter within range
 *
 * The algorithm takes O(m * n) time to find the coverage for m receivers.
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

        int numCovered = 0;
        for (Receiver receiver : receivers) {
            final int[] receiverPos = new int[]{
                    receiver.getXCoordinate(),
                    receiver.getYCoordinate()
            };

            for (Transmitter transmitter : transmitters) {
                final int[] transmitterPos = new int[]{
                        transmitter.getXCoordinate(),
                        transmitter.getYCoordinate()
                };
                final int transmitterPower = transmitter.getPower();

                if (chebyshevDistance(receiverPos, transmitterPos) <= transmitterPower) {
                    numCovered++;
                    break;
                }
            }
        }

        return new SimpleFlat2DCoverageModelResponse(numCovered, receivers.size());
    }

    /**
     * Chebyshev distance for 2-dimensional vector is given
     * by max(|x_2 - x_2|, |y_2 - y_1|), that is, it is the
     * maximum distance along any given axis
     */
    private int chebyshevDistance(int[] receiver, int[] transmitter) {
        if (receiver.length != 2 || transmitter.length != 2) {
            return -1;
        }
        return max(abs(receiver[0] - receiver[1]), abs(transmitter[1] - transmitter[0]));
    }

    private static class SimpleFlat2DCoverageModelResponse implements ModelResponse {

        private final int coveredReceivers;
        private final int totalReceivers;

        SimpleFlat2DCoverageModelResponse(int coveredReceivers, int totalReceivers) {
            this.coveredReceivers = coveredReceivers;
            this.totalReceivers = totalReceivers;
        }

        @Override
        public boolean didSucceed() {
            return true;
        }

        @Override
        public String serialise() {
            return String.format("Covered receivers: %d/%d", coveredReceivers, totalReceivers);
        }
    }
}
