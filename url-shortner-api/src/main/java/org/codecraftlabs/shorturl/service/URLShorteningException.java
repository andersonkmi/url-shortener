package org.codecraftlabs.shorturl.service;

public class URLShorteningException extends RuntimeException {
    public URLShorteningException(String message, Throwable exception) {
        super(message, exception);
    }
}
