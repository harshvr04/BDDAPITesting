package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationManager {
    private static Properties properties = new Properties();

    static {
        String env = System.getProperty("env", "local");
        try {
            FileInputStream fis = new FileInputStream("src/test/resources/" + env + ".properties");
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config for env: " + env, e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
