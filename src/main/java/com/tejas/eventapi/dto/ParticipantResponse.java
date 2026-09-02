package com.tejas.eventapi.dto;

import com.tejas.eventapi.model.Participant;

/**
 * Why we need this: Participant has a reference to Event, and if Event
 * ever serialized its participants list too, you'd get Event -> Participant
 * -> Event -> Participant ... infinitely (a StackOverflowError when Jackson
 * tries to convert it to JSON). This is a classic Spring/JPA gotcha.
 *
 * The clean fix is to never return entities directly from a controller —
 * always convert to a flat response DTO first, which is what this class is.
 */
public class ParticipantResponse {
    private final Long id;
    private final String name;
    private final String email;
    private final boolean checkedIn;
    private final Long eventId;

    public ParticipantResponse(Participant participant) {
        this.id = participant.getId();
        this.name = participant.getName();
        this.email = participant.getEmail();
        this.checkedIn = participant.isCheckedIn();
        this.eventId = participant.getEvent().getId();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public Long getEventId() {
        return eventId;
    }
}
