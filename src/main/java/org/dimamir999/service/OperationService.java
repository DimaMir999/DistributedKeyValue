package org.dimamir999.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dimamir999.dao.FileDao;
import org.dimamir999.model.KeyValue;

import javax.management.RuntimeErrorException;
import java.io.File;
import java.io.IOException;

public class OperationService {
    private static final Logger log = LogManager.getLogger(OperationService.class);
    private static final String DATA_FILE = "data";
    private FileDao fileDao = new FileDao();
    private StringKeyValueConverter stringKeyValueConverter = new StringKeyValueConverter();

    public OperationService() {
        File dataFile = new File(System.getProperty("user.dir") + "/" + DATA_FILE);
        if(!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();

            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                log.error("New file creating error");
                throw new RuntimeErrorException(null);
            }
        }

    }

    public KeyValue<String, String> create(KeyValue<String, String> keyValue) throws IOException {
        fileDao.append(stringKeyValueConverter.encode(keyValue), DATA_FILE);
        log.info("Object added - key: '" + keyValue.getKey() + "', value:'" + keyValue.getValue() + "'");
        return keyValue;
    }

    public KeyValue<String, String> get(String key) throws IOException {
        String allData = fileDao.read(DATA_FILE);

        for(String line : allData.split("\n")) {
            KeyValue keyValue = stringKeyValueConverter.decode(line);
            if((keyValue.getKey()).equals(key)) {
                log.info("Object returned - key: '" + key + "', value:'" + keyValue.getValue() + "'");
                return keyValue;
            }
        }

        return null;
    }

    public KeyValue<String, String> update(KeyValue<String, String> keyValue) throws IOException {
        String allData = fileDao.read(DATA_FILE);
        String key = keyValue.getKey();

        for(String line : allData.split("\n")) {
            KeyValue oldKeyValue = stringKeyValueConverter.decode(line);
            if((oldKeyValue.getKey()).equals(key)) {
                String oldLine = stringKeyValueConverter.encode(oldKeyValue);
                String newLine = stringKeyValueConverter.encode(keyValue);
                String newData = allData.replace(oldLine, newLine);
                fileDao.write(newData, DATA_FILE);
                log.info("Object updated - key: '" + key + "', old value:'" + oldKeyValue.getValue() + "', new value:'" + keyValue.getValue() + "'");
                return oldKeyValue;
            }
        }

        return null;
    }

    public KeyValue<String, String> delete(String key) throws IOException {
        String allData = fileDao.read(DATA_FILE);

        for(String line : allData.split("\n")) {
            KeyValue oldKeyValue = stringKeyValueConverter.decode(line);
            if((oldKeyValue.getKey()).equals(key)) {
                String oldLine = stringKeyValueConverter.encode(oldKeyValue);
                String newData = allData.replace(oldLine, "");
                fileDao.write(newData, DATA_FILE);
                log.info("Object deleted - key: '" + key + "', value:'" + oldKeyValue.getValue() + "'");
                return oldKeyValue;
            }
        }

        return null;
    }
}
