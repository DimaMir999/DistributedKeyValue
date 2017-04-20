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
    private PropertyReader propertyReader = new PropertyReader("distributed-key-value.properties");
    private String tempFile = "temp_data";
    private String dataFile = "data";
    private FileDao fileDao = new FileDao();
    private StringKeyValueConverter stringKeyValueConverter = new StringKeyValueConverter();

    private boolean fileIsNotEmpty(String fileString) throws IOException {
        return !fileString.equals("");
    }

    public OperationService() {
        File file = new File(System.getProperty("user.dir") + "/" + tempFile);

        if(!file.exists()) {
            file.getParentFile().mkdirs();

            try {
                file.createNewFile();
            } catch (IOException e) {
                LOG.error("New file creating error", e);
                throw new RuntimeErrorException(null);
            }
        }

        final int TIMEOUT = Integer.parseInt(propertyReader.getProperty("merging.timeout"));

        Thread fileMerger = new Thread(new FileMerger(dataFile, tempFile, TIMEOUT));
        fileMerger.start();
    }

    public OperationService(FileDao fileDao, StringKeyValueConverter stringKeyValueConverter) {
        this();
        this.fileDao = fileDao;
        this.stringKeyValueConverter = stringKeyValueConverter;
    }

    public KeyValue<String, String> create(KeyValue<String, String> keyValue) throws IOException {
        String allData = fileDao.read(dataFile);

        if (fileIsNotEmpty(allData)) {
            for (String line : allData.split("\n")) {
                KeyValue<String, String> lineKeyValue = stringKeyValueConverter.decode(line);
                if ((lineKeyValue.getKey()).equals(keyValue.getKey())) {
                    LOG.info("Object not added - key: '" + keyValue.getKey() + "' is already occupied, value: '" + lineKeyValue.getValue() + "'");
                    return null;
                }
            }
        }

        String tempData = fileDao.read(tempFile);

        if (fileIsNotEmpty(tempData)) {
            for (String line : tempData.split("\n")) {
                KeyValue<String, String> lineKeyValue = stringKeyValueConverter.decode(line);
                if ((lineKeyValue.getKey()).equals(keyValue.getKey())) {
                    LOG.info("Object not added - key: '" + keyValue.getKey() + "' is already occupied, value: '" + lineKeyValue.getValue() + "'");
                    return null;
                }
            }
        }

        fileDao.append(stringKeyValueConverter.encode(keyValue), tempFile);
        LOG.info("Object added - key: '" + keyValue.getKey() + "', value:'" + keyValue.getValue() + "'");
        return keyValue;
    }

    public KeyValue<String, String> get(String key) throws IOException {
        String allData = fileDao.read(dataFile);

        if (fileIsNotEmpty(allData)) {
            for (String line : allData.split("\n")) {
                KeyValue<String, String> keyValue = stringKeyValueConverter.decode(line);
                if ((keyValue.getKey()).equals(key)) {
                    LOG.info("Object returned - key: '" + key + "', value:'" + keyValue.getValue() + "'");
                    return keyValue;
                }
            }
        }

        String tempData = fileDao.read(tempFile);

        if (fileIsNotEmpty(tempData)) {
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

    private void changeValue(KeyValue<String, String> oldKeyValue, KeyValue<String, String> keyValue, String data, String file) throws IOException {
        String oldLine = stringKeyValueConverter.encode(oldKeyValue);
        String newLine = stringKeyValueConverter.encode(keyValue);
        String newData = data.replace(oldLine, newLine);
        fileDao.write(newData, file);
    }

    public KeyValue<String, String> update(KeyValue<String, String> keyValue) throws IOException {
        String key = keyValue.getKey();

        String allData = fileDao.read(dataFile);

        if (fileIsNotEmpty(allData)) {
            for (String line : allData.split("\n")) {
                KeyValue<String, String> oldKeyValue = stringKeyValueConverter.decode(line);
                if ((oldKeyValue.getKey()).equals(key)) {
                    changeValue(oldKeyValue, keyValue, allData, dataFile);
                    LOG.info("Object updated - key: '" + key + "', old value:'" + oldKeyValue.getValue() + "', new value:'" + keyValue.getValue() + "'");
                    return oldKeyValue;
                }
            }
        }

        String tempData = fileDao.read(tempFile);

        if (fileIsNotEmpty(tempData)) {
            for (String line : tempData.split("\n")) {
                KeyValue<String, String> oldKeyValue = stringKeyValueConverter.decode(line);
                if ((oldKeyValue.getKey()).equals(key)) {
                    changeValue(oldKeyValue, keyValue, tempData, tempFile);
                    LOG.info("Object updated - key: '" + key + "', old value:'" + oldKeyValue.getValue() + "', new value:'" + keyValue.getValue() + "'");
                    return oldKeyValue;
                }
            }
        }

        return null;
    }

    private void deleteValue(KeyValue<String, String> oldKeyValue, String data, String file) throws IOException {
        String oldLine = stringKeyValueConverter.encode(oldKeyValue) + "\n";
        String newData = data.replace(oldLine, "");
        fileDao.write(newData, file);
    }

    public KeyValue<String, String> delete(String key) throws IOException {
        String allData = fileDao.read(dataFile);

        if (fileIsNotEmpty(allData)) {
            for (String line : allData.split("\n")) {
                KeyValue<String, String> oldKeyValue = stringKeyValueConverter.decode(line);
                if ((oldKeyValue.getKey()).equals(key)) {
                    deleteValue(oldKeyValue, allData, dataFile);
                    LOG.info("Object deleted - key: '" + key + "', value:'" + oldKeyValue.getValue() + "'");
                    return oldKeyValue;
                }
            }
        }

        String tempData = fileDao.read(tempFile);

        if (fileIsNotEmpty(tempData)) {
            for (String line : tempData.split("\n")) {
                KeyValue<String, String> oldKeyValue = stringKeyValueConverter.decode(line);
                if ((oldKeyValue.getKey()).equals(key)) {
                    deleteValue(oldKeyValue, tempData, tempFile);
                    LOG.info("Object deleted - key: '" + key + "', value:'" + oldKeyValue.getValue() + "'");
                    return oldKeyValue;
                }
            }
        }

        return null;
    }
}
