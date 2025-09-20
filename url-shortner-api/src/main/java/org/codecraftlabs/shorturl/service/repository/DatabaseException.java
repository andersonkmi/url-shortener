package org.codecraftlabs.shorturl.service.repository;

public class DatabaseException extends Exception {
    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable exception) {
        super(message, exception);
    }
}