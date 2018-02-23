package com.nominet.rd.coverage.models.coverage;

import com.nominet.rd.coverage.datastores.Store;
import com.nominet.rd.coverage.datastores.StoreKey;
import com.nominet.rd.coverage.datastores.StoreManager;
import com.nominet.rd.coverage.maps.Map;
import com.nominet.rd.coverage.maps.Receiver;
import com.nominet.rd.coverage.maps.Transmitter;
import com.nominet.rd.coverage.models.Model;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.max;

/**
 * This model calculates the coverage of a set of m receivers on a map
 * of n transmitters. A receiver is said to be covered if there is a
 * transmitter of power (p), a chebyshev distance (d) of d < p away.
 *
 * This is done by calculating the chebyshev distance between receiver i
 * and the n transmitters, for all i = 1 -> m. The model will stop once it
 * has found a single receiver that is within the range of the transmitter.
 *
 * The algorithm takes O(m * n) time
 **/
public class SimpleFlat2DCoverageModel implements Model<Flat2DCoverageContext> {

    private static final Logger LOG = LogManager.getLogger(Flat2DCoverageContext.class);

    @Override
    public Flat2DCoverageContext createContext(StoreManager storeManager) {
        final Store<Map> mapStore = storeManager.getStore(StoreKey.MapFile);
        return new Flat2DCoverageContext(mapStore);
    }

    @Override
    public boolean snapshot(Flat2DCoverageContext context, List<String> errors) {
        return context.snapshot(errors);
    }

    @Override
    public void calculate(Flat2DCoverageContext context, List<String> errors) {
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

        LOG.info("Covered receivers: {}/{}", numCovered, receivers.size());
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
}
