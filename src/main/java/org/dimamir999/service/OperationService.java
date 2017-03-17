package org.dimamir999.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dimamir999.dao.FileDao;
import org.dimamir999.model.KeyValue;

import javax.management.RuntimeErrorException;
import java.io.File;
import java.io.IOException;

public class OperationService {
    private static final Logger LOG = LogManager.getLogger(OperationService.class);
    private static final String TEMP_FILE = "temp_data";
    private static final String DATA_FILE = "data";
    private FileDao fileDao = new FileDao();
    private StringKeyValueConverter stringKeyValueConverter = new StringKeyValueConverter();

    public OperationService() {
        File dataFile = new File(System.getProperty("user.dir") + "/" + TEMP_FILE);
        if(!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();

            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                LOG.error("New file creating error", e);
                throw new RuntimeErrorException(null);
            }
        }

    }

    public KeyValue<String, String> create(KeyValue<String, String> keyValue) throws IOException {
        String allData = fileDao.read(DATA_FILE);

        if (!allData.equals("")) {
            for (String line : allData.split("\n")) {
                KeyValue<String, String> lineKeyValue = stringKeyValueConverter.decode(line);
                if ((lineKeyValue.getKey()).equals(keyValue.getKey())) {
                    LOG.info("Object not added - key: '" + keyValue.getKey() + "' is already occupied, value: '" + lineKeyValue.getValue() + "'");
                    return null;
                }
            }
        }

        String tempData = fileDao.read(TEMP_FILE);

        if (!tempData.equals("")) {
            for (String line : tempData.split("\n")) {
                KeyValue<String, String> lineKeyValue = stringKeyValueConverter.decode(line);
                if ((lineKeyValue.getKey()).equals(keyValue.getKey())) {
                    LOG.info("Object not added - key: '" + keyValue.getKey() + "' is already occupied, value: '" + lineKeyValue.getValue() + "'");
                    return null;
                }
            }
        }

        fileDao.append(stringKeyValueConverter.encode(keyValue), TEMP_FILE);
        LOG.info("Object added - key: '" + keyValue.getKey() + "', value:'" + keyValue.getValue() + "'");
        return keyValue;
    }

    public KeyValue<String, String> get(String key) throws IOException {
        String allData = fileDao.read(DATA_FILE);

        if (!allData.equals("")) {
            for (String line : allData.split("\n")) {
                KeyValue<String, String> keyValue = stringKeyValueConverter.decode(line);
                if ((keyValue.getKey()).equals(key)) {
                    LOG.info("Object returned - key: '" + key + "', value:'" + keyValue.getValue() + "'");
                    return keyValue;
                }
            }
        }

        String tempData = fileDao.read(TEMP_FILE);

        if (!tempData.equals("")) {
            for (String line : tempData.split("\n")) {
                KeyValue<String, String> keyValue = stringKeyValueConverter.decode(line);
                if ((keyValue.getKey()).equals(key)) {
                    LOG.info("Object returned - key: '" + key + "', value:'" + keyValue.getValue() + "'");
                    return keyValue;
                }
            }
        }

        return null;
    }

    public KeyValue<String, String> update(KeyValue<String, String> keyValue) throws IOException {
        String key = keyValue.getKey();

        String allData = fileDao.read(DATA_FILE);

        if (!allData.equals("")) {
            for (String line : allData.split("\n")) {
                KeyValue<String, String> oldKeyValue = stringKeyValueConverter.decode(line);
                if ((oldKeyValue.getKey()).equals(key)) {
                    String oldLine = stringKeyValueConverter.encode(oldKeyValue);
                    String newLine = stringKeyValueConverter.encode(keyValue);
                    String newData = allData.replace(oldLine, newLine);
                    fileDao.write(newData, DATA_FILE);
                    LOG.info("Object updated - key: '" + key + "', old value:'" + oldKeyValue.getValue() + "', new value:'" + keyValue.getValue() + "'");
                    return oldKeyValue;
                }
            }
        }

        String tempData = fileDao.read(TEMP_FILE);

        if (!tempData.equals("")) {
            for (String line : tempData.split("\n")) {
                KeyValue<String, String> oldKeyValue = stringKeyValueConverter.decode(line);
                if ((oldKeyValue.getKey()).equals(key)) {
                    String oldLine = stringKeyValueConverter.encode(oldKeyValue);
                    String newLine = stringKeyValueConverter.encode(keyValue);
                    String newData = allData.replace(oldLine, newLine);
                    fileDao.write(newData, TEMP_FILE);
                    LOG.info("Object updated - key: '" + key + "', old value:'" + oldKeyValue.getValue() + "', new value:'" + keyValue.getValue() + "'");
                    return oldKeyValue;
                }
            }
        }

        return null;
    }

    public KeyValue<String, String> delete(String key) throws IOException {
        String allData = fileDao.read(DATA_FILE);

        if (!allData.equals("")) {
            for (String line : allData.split("\n")) {
                KeyValue<String, String> oldKeyValue = stringKeyValueConverter.decode(line);
                if ((oldKeyValue.getKey()).equals(key)) {
                    String oldLine = stringKeyValueConverter.encode(oldKeyValue) + "\n";
                    String newData = allData.replace(oldLine, "");
                    fileDao.write(newData, DATA_FILE);
                    LOG.info("Object deleted - key: '" + key + "', value:'" + oldKeyValue.getValue() + "'");
                    return oldKeyValue;
                }
            }
        }

        String tempData = fileDao.read(TEMP_FILE);

        if (!tempData.equals("")) {
            for (String line : tempData.split("\n")) {
                KeyValue<String, String> oldKeyValue = stringKeyValueConverter.decode(line);
                if ((oldKeyValue.getKey()).equals(key)) {
                    String oldLine = stringKeyValueConverter.encode(oldKeyValue) + "\n";
                    String newData = allData.replace(oldLine, "");
                    fileDao.write(newData, TEMP_FILE);
                    LOG.info("Object deleted - key: '" + key + "', value:'" + oldKeyValue.getValue() + "'");
                    return oldKeyValue;
                }
            }
        }

        return null;
    }
}
