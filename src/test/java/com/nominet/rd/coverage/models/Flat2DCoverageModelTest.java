package com.nominet.rd.coverage.models;

import com.nominet.rd.coverage.datastores.Store;
import com.nominet.rd.coverage.datastores.StoreKey;
import com.nominet.rd.coverage.datastores.StoreManager;
import com.nominet.rd.coverage.maps.Map;
import com.nominet.rd.coverage.maps.Receiver;
import com.nominet.rd.coverage.maps.Transmitter;
import com.nominet.rd.coverage.maps.TwoDGridMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Flat2DCoverageModelTest {

    private StoreManager manager;

    @Before
    public void setup() {
        manager = new StoreManager();
    }

    @Test
    public void test_single_power_increase() {
        final List<Transmitter> tmpTransmitters = Arrays.asList(
                new Transmitter(1, 2, 5, 1),
                new Transmitter(2, 0, 6, 3),
                new Transmitter(3, 1, 2, 2),
                new Transmitter(4, 3, 5, 3)
        );
        final List<Receiver> tmpReceivers = Arrays.asList(
                new Receiver(1, 0, 1),
                new Receiver(2, 8, 8),
                new Receiver(3, 6, 5)
        );
        final Map map = new TwoDGridMap(
                10, 10,
                new ArrayList<>(tmpTransmitters),
                new ArrayList<>(tmpReceivers)
        );
        manager.addStore(new MapStore(map));

        final Flat2DCoverageModel model = new Flat2DCoverageModel(manager);
        model.snapshot();

        final ModelResponse actualResponse = model.calculate();
        final String expectedResponse = "2/3\n" + "4 5";

        Assert.assertEquals(expectedResponse, actualResponse.serialise());
    }

    @Test
    public void test_multiple_power_increases() {
        final List<Transmitter> tmpTransmitters = Arrays.asList(
                new Transmitter(1, 8, 3, 1),
                new Transmitter(2, 10, 9, 1),
                new Transmitter(3, 3, 7, 1)
        );
        final List<Receiver> tmpReceivers = Arrays.asList(
                new Receiver(1, 12, 7),
                new Receiver(2, 5, 6)
        );
        final Map map = new TwoDGridMap(
                13, 12,
                new ArrayList<>(tmpTransmitters),
                new ArrayList<>(tmpReceivers)
        );
        manager.addStore(new MapStore(map));

        final Flat2DCoverageModel model = new Flat2DCoverageModel(manager);
        model.snapshot();

        final ModelResponse actualResponse = model.calculate();
        final String expectedResponse = "0/2\n" + "2 2\n" + "3 2";

        Assert.assertEquals(expectedResponse, actualResponse.serialise());
    }

    private static final class MapStore implements Store<Map> {
        private final Map map;

        public MapStore(Map map) {
            this.map = map;
        }

        @Override
        public StoreKey getKey() {
            return StoreKey.MapFile;
        }

        @Override
        public Map getData() {
            return map;
        }
    }
}
