package com.nominet.rd.coverage.datastores;

import com.nominet.rd.coverage.datastores.map.TwoDGridFileConverter;
import com.nominet.rd.coverage.maps.Map;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@RunWith(Enclosed.class)
public class TwoDGridFileConverterTest {

    private static final Function<List<String>, Map> converter = new TwoDGridFileConverter();

    @RunWith(Parameterized.class)
    public static final class InvalidFileTests {
        @Parameterized.Parameter()
        public List<String> invalidFiles;

        @Parameterized.Parameters
        public static Collection<List<String>> invalidFileData() {
            return Arrays.asList(
                    //Invalid size
                    Collections.singletonList(
                            "10,10"
                    ),
                    //Incorrect number of parameters - single
                    Arrays.asList(
                            "10 10",
                            "199",
                            "299"
                    ),
                    //Incorrect number of parameters - multiple
                    Arrays.asList(
                            "10 10",
                            "1 2 3 4 5"
                    ),
                    //Incorrect separator
                    Arrays.asList(
                            "10 10",
                            "1,2,3,4,5"
                    )
            );
        }

        @Test(expected = IllegalStateException.class)
        public void test_invalid_tower_formats() {
            converter.apply(invalidFiles);
        }
    }

    public static final class SingleRunTests {

        @Test(expected = IllegalStateException.class)
        public void test_empty_file() {
            converter.apply(Collections.emptyList());
        }

        @Test
        public void test_transmitter_out_of_bounds() {
            final List<String> fileLines = Arrays.asList(
                    "10 10",
                    "1 11 12 3"
            );
            final Map res = converter.apply(fileLines);

            Assert.assertNotNull(res);
            Assert.assertTrue(res.getTransmitters().isEmpty());
            Assert.assertTrue(res.getTransmitters().isEmpty());
        }

        @Test
        public void test_receiver_out_of_bounds() {
            final List<String> fileLines = Arrays.asList(
                    "10 10",
                    "1 9 9 3",
                    "1 11 11"
            );
            final Map res = converter.apply(fileLines);

            Assert.assertNotNull(res);
            Assert.assertEquals(1, res.getTransmitters().size());
            Assert.assertTrue(res.getReceivers().isEmpty());
        }

        @Test
        public void test_valid_transmitter_and_valid_receiver() {
            final List<String> fileLines = Arrays.asList(
                    "10 10",
                    "1 9 9 3",
                    "2 8 7 2",
                    "1 8 9",
                    "2 1 2",
                    "3 3 4"
            );
            final Map res = converter.apply(fileLines);

            Assert.assertNotNull(res);
            Assert.assertEquals(2, res.getTransmitters().size());
            Assert.assertEquals(3, res.getReceivers().size());
        }
    }
}
