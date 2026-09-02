package com.tejas.eventapi.exception;

/**
 * Thrown when a client asks for an Event or Participant by an id
 * that doesn't exist. We turn this into a clean 404 response —
 * see GlobalExceptionHandler.java.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
