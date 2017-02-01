package org.dimamir999.controller;


import org.dimamir999.model.KeyValue;
import org.dimamir999.service.OperationService;
import org.dimamir999.service.OperationServiceException;

import java.io.IOException;

public class CommandController {

    private OperationService operationService = new OperationService();

    public String read(String key) throws OperationServiceException {
        try {
            return operationService.get(key).getValue();
        } catch (IOException e) {
            e.printStackTrace();
            throw new OperationServiceException("Can't perform read operation", e);
        }
    }

    public void create(String key, String value) throws OperationServiceException {
        try {
            operationService.create(new KeyValue<>(key, value));
        } catch (IOException e) {
            e.printStackTrace();
            throw new OperationServiceException("Can't perform create operation", e);
        }
    }

    public void update(String key, String value) throws OperationServiceException {
        try {
            operationService.update(new KeyValue<>(key, value));
        } catch (IOException e) {
            e.printStackTrace();
            throw new OperationServiceException("Can't perform update operations", e);
        }
    }

    public void delete(String key) throws OperationServiceException {
        try {
            operationService.delete(key);
        } catch (IOException e) {
            e.printStackTrace();
            throw new OperationServiceException("Can't perform delete operations", e);
        }
    }
}
