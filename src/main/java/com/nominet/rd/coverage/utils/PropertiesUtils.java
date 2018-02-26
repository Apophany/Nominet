package com.nominet.rd.coverage.utils;

import java.util.Properties;

/**
 * Properties util class for working with
 * prefixed property files and extracting
 * non-string based property values
 */
public final class PropertiesUtils {

    public static Properties subset(Properties properties, String prefix, boolean keepPrefix) {
        if (properties == null || (prefix == null || prefix.isEmpty())) {
            return properties;
        }

        final Properties subsetProps = new Properties();
        for (String name : properties.stringPropertyNames()) {
            String newKey = name;
            if (newKey.startsWith(prefix)) {
                newKey = keepPrefix ? newKey : newKey.replace(prefix, "");
                if (newKey.startsWith(".")) {
                    newKey = newKey.substring(1);
                }
                subsetProps.put(newKey, properties.getProperty(name));
            }
        }
        return subsetProps;
    }

    public static int getIntProperty(Properties properties, String name, int defaultVal) {
        final String val = properties.getProperty(name);

        int result;
        try {
            result = Integer.valueOf(val);
        } catch (NumberFormatException e) {
            result = defaultVal;
        }

        return result;
    }
}
