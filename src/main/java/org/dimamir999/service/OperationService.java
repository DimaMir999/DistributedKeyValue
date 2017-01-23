package org.dimamir999.service;

import org.dimamir999.dao.FileDao;
import org.dimamir999.model.KeyValue;

import javax.management.RuntimeErrorException;
import java.io.File;
import java.io.IOException;

public class OperationService {

    private static final String DATA_FILE = "data";

    private FileDao fileDao = new FileDao();
    private StringKeyValueConverter stringKeyValueConverter = new StringKeyValueConverter();

    public OperationService() {
        File dataFile = new File(System.getProperty("user.dir") + "/" + DATA_FILE);
        if(!dataFile.exists()){
            dataFile.getParentFile().mkdirs();
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeErrorException(null);
            }
        }
    }

    public KeyValue<String, String> create(KeyValue<String, String> keyValue) throws IOException {
        fileDao.append(stringKeyValueConverter.encode(keyValue), DATA_FILE);
        return keyValue;
    }

    public KeyValue<String, String> get(String key) throws IOException {
        String allData = fileDao.read(DATA_FILE);
        for(String line : allData.split("\n")){
            KeyValue<String, String> keyValue = stringKeyValueConverter.decode(line);
            if(keyValue.getKey().equals(key)){
                return keyValue;
            }
        }
        return null;
    }

    public KeyValue<String, String> update(KeyValue<String, String> keyValue) throws IOException {
        String allData = fileDao.read(DATA_FILE);
        String key = keyValue.getKey();
        for(String line : allData.split("\n")){
            KeyValue<String, String> oldKeyValue = stringKeyValueConverter.decode(line);
            if(oldKeyValue.getKey().equals(key)){
                String oldLine = stringKeyValueConverter.encode(oldKeyValue);
                String newLine = stringKeyValueConverter.encode(keyValue);
                String newData = allData.replace(oldLine, newLine);
                fileDao.write(newData, DATA_FILE);
                return oldKeyValue;
            }
        }
        return null;
    }

    public KeyValue<String, String> delete(String key) throws IOException {
        String allData = fileDao.read(DATA_FILE);
        for(String line : allData.split("\n")){
            KeyValue<String, String> oldKeyValue = stringKeyValueConverter.decode(line);
            if(oldKeyValue.getKey().equals(key)){
                String oldLine = stringKeyValueConverter.encode(oldKeyValue);
                String newData = allData.replace(oldLine, "");
                fileDao.write(newData, DATA_FILE);
                return oldKeyValue;
            }
        }
        return null;
    }
}
