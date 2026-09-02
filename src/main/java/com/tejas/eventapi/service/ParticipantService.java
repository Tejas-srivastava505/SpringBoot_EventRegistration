package com.tejas.eventapi.service;

import com.tejas.eventapi.dto.ParticipantRequest;
import com.tejas.eventapi.dto.ParticipantResponse;
import com.tejas.eventapi.exception.EventFullException;
import com.tejas.eventapi.exception.ResourceNotFoundException;
import com.tejas.eventapi.model.Event;
import com.tejas.eventapi.model.Participant;
import com.tejas.eventapi.repository.ParticipantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final EventService eventService; // reused to look up the event + reuse its "not found" logic

    public ParticipantService(ParticipantRepository participantRepository, EventService eventService) {
        this.participantRepository = participantRepository;
        this.eventService = eventService;
    }

    /**
     * This is the one piece of real business logic in the project:
     * reject registration once the event is at capacity.
     *
     * Walking through this method out loud is a good interview answer
     * to "tell me about a design decision you made":
     *   1. Look up the event (404 if it doesn't exist)
     *   2. Count how many participants are already registered
     *   3. If count >= capacity, reject with a 409 Conflict, not a 400 —
     *      409 specifically means "the request conflicts with the
     *      current state of the resource," which fits better than
     *      "bad request" here (the request itself was valid).
     *   4. Otherwise, save the new participant linked to this event
     */
    public ParticipantResponse registerParticipant(Long eventId, ParticipantRequest request) {
        Event event = eventService.findEventOrThrow(eventId);

        long currentCount = participantRepository.countByEventId(eventId);
        if (currentCount >= event.getCapacity()) {
            throw new EventFullException(
                    "Event '" + event.getName() + "' is at full capacity (" + event.getCapacity() + ")"
            );
        }

        Participant participant = new Participant(request.getName(), request.getEmail(), event);
        Participant saved = participantRepository.save(participant);
        return new ParticipantResponse(saved);
    }

    public List<ParticipantResponse> getParticipantsForEvent(Long eventId) {
        // Confirm the event exists first, so we return a clean 404
        // instead of silently returning an empty list for a bad eventId.
        eventService.findEventOrThrow(eventId);
        return participantRepository.findByEventId(eventId)
                .stream()
                .map(ParticipantResponse::new)
                .toList();
    }

    public ParticipantResponse checkIn(Long participantId) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new ResourceNotFoundException("Participant not found with id: " + participantId));
        participant.setCheckedIn(true);
        Participant saved = participantRepository.save(participant);
        return new ParticipantResponse(saved);
    }

    public void cancelRegistration(Long participantId) {
        if (!participantRepository.existsById(participantId)) {
            throw new ResourceNotFoundException("Participant not found with id: " + participantId);
        }
        participantRepository.deleteById(participantId);
    }
}
