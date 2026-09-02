package com.tejas.eventapi.controller;

import com.tejas.eventapi.dto.ParticipantRequest;
import com.tejas.eventapi.dto.ParticipantResponse;
import com.tejas.eventapi.service.ParticipantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Note the URL design here: registration and listing are nested under
 * /api/events/{eventId}/participants, because a participant only makes
 * sense in the context of an event. Check-in and cancellation use a
 * flat /api/participants/{id} path instead, because at that point we're
 * acting on a specific participant directly and don't need the event
 * context in the URL. This kind of REST resource-nesting decision is a
 * reasonable thing to be asked to justify in an interview.
 */
@RestController
public class ParticipantController {

    private final ParticipantService participantService;

    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    // POST /api/events/3/participants
    @PostMapping("/api/events/{eventId}/participants")
    public ResponseEntity<ParticipantResponse> register(
            @PathVariable Long eventId,
            @Valid @RequestBody ParticipantRequest request) {
        ParticipantResponse response = participantService.registerParticipant(eventId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/events/3/participants
    @GetMapping("/api/events/{eventId}/participants")
    public ResponseEntity<List<ParticipantResponse>> listForEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(participantService.getParticipantsForEvent(eventId));
    }

    // PATCH /api/participants/7/checkin
    // PATCH (not PUT) because we're partially updating one field
    // (checkedIn), not replacing the whole resource.
    @PatchMapping("/api/participants/{id}/checkin")
    public ResponseEntity<ParticipantResponse> checkIn(@PathVariable Long id) {
        return ResponseEntity.ok(participantService.checkIn(id));
    }

    // DELETE /api/participants/7
    @DeleteMapping("/api/participants/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        participantService.cancelRegistration(id);
        return ResponseEntity.noContent().build();
    }
}
