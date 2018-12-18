package org.elastos.exception;

public class ElaDidServiceException extends RuntimeException {
    public ElaDidServiceException(String message) {
        super(message);
    }

    public ElaDidServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
