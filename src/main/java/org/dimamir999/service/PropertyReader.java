package org.dimamir999.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by SKY-PC on 07.02.2017.
 */
public class PropertyReader {
    private static final Logger log = LogManager.getLogger(PropertyReader.class);
    private FileInputStream fis;
    private Properties properties = new Properties();
    private String fileName;

    public PropertyReader(String fileName) {
        this.fileName = fileName;
    }

    public String getProperty(String property) {
        if(fis == null) {
            try {
                fis = new FileInputStream("src/main/resources/" + fileName);
                properties.load(fis);
            } catch (IOException e) {
                log.error("File not found");
            }
        }

        String result = properties.getProperty(property);
        return result;
    }
}
