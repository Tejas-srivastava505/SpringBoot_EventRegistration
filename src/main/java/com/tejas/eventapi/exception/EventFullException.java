package com.tejas.eventapi.exception;

/**
 * Thrown when someone tries to register a participant to an event
 * that has already reached its capacity. This is our one piece of
 * "business logic" beyond plain CRUD — good to be able to explain
 * in an interview: "what happens if the event is full?"
 */
public class EventFullException extends RuntimeException {
    public EventFullException(String message) {
        super(message);
    }
}
