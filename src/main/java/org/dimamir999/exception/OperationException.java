package org.dimamir999.exception;

public class OperationException extends Exception {

    public OperationException() {}

    public OperationException(String message) {
        super(message);
    }

    public OperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
