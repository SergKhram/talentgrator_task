package org.testslotegrator.config;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

public class ConfigLoader {

    public static String getProperty(String key, String defaultValue) {
        // Сначала проверяем environment переменные (с приоритетом)
        String envValue = System.getenv(key);
        if (!isEmpty(envValue)) {
            return envValue;
        }

        // Затем проверяем system properties (устанавливаются Gradle)
        String propValue = System.getProperty(key);
        if (!isEmpty(propValue)) {
            return propValue;
        }

        return defaultValue;
    }

    public static String getProperty(String key) {
        return getProperty(key, null);
    }
}



