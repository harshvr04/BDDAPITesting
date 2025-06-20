package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationManager {
    private static final Properties properties = new Properties();

    static {
        String env = System.getProperty("env", "local");
        try {
            FileInputStream fis = new FileInputStream("src/test/resources/" + env + ".properties");
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config for env: " + env, e);
        }

        try {
            // Load secret keys if the file exists
            FileInputStream secretConfig = new FileInputStream("src/test/resources/secret_keys.properties");
            properties.load(secretConfig); // Will override existing keys if duplicated
        } catch (IOException ignored) {
            System.out.println("No secret_keys.properties file found. Continuing without API Key.");
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
