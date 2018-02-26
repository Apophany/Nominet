package com.nominet.rd.coverage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Entry point for the application. Manages property loading
 * and app creation
 */
public class ReceiverCoverageApp {
    private static final Logger LOG = LogManager.getLogger(ReceiverCoverageApp.class);

    public static void main(String[] args) {
        final Properties appProperties = loadProperties();
        if (appProperties == null) {
            LOG.fatal("Cannot start app with no properties");
            System.exit(1);
        }

        final ReceiverCoverageCalculator app = createApp(appProperties);
        if (app != null) {
            app.run();
        }
    }

    private static ReceiverCoverageCalculator createApp(Properties appProperties) {
        try {
            return new ReceiverCoverageCalculator(appProperties);
        } catch (Exception e) {
            LOG.fatal("Failed to initialise application: {}", e.getMessage());
            return null;
        }
    }

    private static Properties loadProperties() {
        final Properties properties;
        try {
            properties = loadFileBasedProperties();
        } catch (Exception e) {
            LOG.error("Could not load properties files: {}", e.getMessage());
            LOG.fatal("Unexpected exception occurred, check log file");
            return null;
        }
        return properties;
    }

    private static Properties loadFileBasedProperties() throws IOException {
        final String fileName = "receiver-coverage.properties";

        final Properties properties = new Properties();
        try (InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(fileName)) {
            properties.load(in);
        }
        return properties;
    }
}
