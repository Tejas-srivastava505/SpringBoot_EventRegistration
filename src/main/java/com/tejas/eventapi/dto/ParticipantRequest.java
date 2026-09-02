package com.tejas.eventapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO = Data Transfer Object.
 *
 * Why not just accept a Participant entity directly in the controller?
 * Because the incoming JSON for "register a participant" shouldn't
 * include a full nested Event object or a checkedIn flag — the client
 * only sends name + email, and we attach the event on the server side
 * based on the URL path (/events/{eventId}/participants).
 */
public class ParticipantRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
