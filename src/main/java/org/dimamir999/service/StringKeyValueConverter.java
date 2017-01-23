package org.dimamir999.service;

import org.dimamir999.model.KeyValue;

public class StringKeyValueConverter {

    private static final String KEY_VALUE_SEPARATOR_PATTERN = "\\|-\\|";
    private static final String KEY_VALUE_SEPARATOR_STRING = "|-|";

    public KeyValue<String, String> decode(String keyValueString){
        String[] keyValueArray = keyValueString.split(KEY_VALUE_SEPARATOR_PATTERN);
        return new KeyValue<>(keyValueArray[0], keyValueArray[1]);
    }

    public String encode(KeyValue<String, String> keyValue){
        return keyValue.getKey() + KEY_VALUE_SEPARATOR_STRING + keyValue.getValue();
    }
}
