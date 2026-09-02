package com.tejas.eventapi.dto;

import com.tejas.eventapi.model.Event;

import java.time.LocalDate;

public class EventResponse {
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final int capacity;
    private final int registeredCount;

    public EventResponse(Event event) {
        this.id = event.getId();
        this.name = event.getName();
        this.date = event.getDate();
        this.capacity = event.getCapacity();
        this.registeredCount = event.getParticipants().size();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getRegisteredCount() {
        return registeredCount;
    }
}
