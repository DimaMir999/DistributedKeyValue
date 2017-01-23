package org.dimamir999.controller;


import org.dimamir999.model.KeyValue;
import org.dimamir999.service.OperationService;

import java.io.IOException;

public class CommandController {

    private OperationService operationService = new OperationService();

    public String read(String key){
        try {
            return operationService.get(key).getValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void create(String key, String value){
        try {
            operationService.create(new KeyValue<>(key, value));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(String key, String value){
        try {
            operationService.update(new KeyValue<>(key, value));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(String key){
        try {
            operationService.delete(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
