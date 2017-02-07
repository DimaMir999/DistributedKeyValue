package org.dimamir999.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyReader {
    private FileInputStream fis;
    private Properties properties = new Properties();
    private String fileName;

    public PropertyReader(String fileName) {
        this.fileName = fileName;
    }

    public String getProperty(String property) {
        if (fis == null) {
            try {
                fis = new FileInputStream("src/main/resources/" + fileName);
                properties.load(fis);
            } catch (IOException e) {
                // Log
            }
        }

        String result = properties.getProperty(property);

        return result;
    }
}
