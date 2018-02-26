package com.nominet.rd.coverage.publishers;

import com.nominet.rd.coverage.models.ModelResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConsolePublisher implements Publisher {

    private static final Logger LOG = LogManager.getLogger(ConsolePublisher.class);

    @Override
    public void publish(ModelResponse response) {
        LOG.info(response.serialise());
    }
}
