package org.dimamir999.controller;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dimamir999.model.KeyValue;
import org.dimamir999.service.OperationService;
import org.dimamir999.service.OperationServiceException;

import java.io.IOException;

public class CommandController {
    private static final Logger log = LogManager.getLogger(CommandController.class);
    private OperationService operationService = new OperationService();

    public CommandController() {
    }

    public String read(String key) throws OperationServiceException {
        try {
            return operationService.get(key).getValue();
        } catch (IOException e) {
            log.error("Can't perform read operation");
            e.printStackTrace();
            throw new OperationServiceException("Can't perform read operation", e);
        }
    }

    public void create(String key, String value) throws OperationServiceException {
        try {
            this.operationService.create(new KeyValue(key, value));
        } catch (IOException e) {
            log.error("Can't perform create operation");
            e.printStackTrace();
            throw new OperationServiceException("Can't perform create operation", e);
        }
    }

    public void update(String key, String value) throws OperationServiceException {
        try {
            this.operationService.update(new KeyValue(key, value));
        } catch (IOException e) {
            log.error("Can't perform update operation");
            e.printStackTrace();
            throw new OperationServiceException("Can't perform update operations", e);
        }
    }

    public void delete(String key) throws OperationServiceException {
        try {
            this.operationService.delete(key);
        } catch (IOException e) {
            log.error("Can't perform delete operation");
            e.printStackTrace();
            throw new OperationServiceException("Can't perform delete operations", e);
        }
    }
}
