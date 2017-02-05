package org.dimamir999.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by SKY-PC on 05.02.2017.
 */
public class PropertyReader {
    private FileInputStream fis;
    private Properties properties = new Properties();

    public String getProperty(String property) {
        if (fis == null) {
            try {
                fis = new FileInputStream("src/main/resources/distributed-key-value.properties");
                properties.load(fis);
            } catch (IOException e) {
                // Место для лога
            }
        }

        String result = properties.getProperty(property);

        return result;
    }
}
