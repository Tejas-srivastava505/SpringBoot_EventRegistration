package com.tejas.eventapi.service;

import com.tejas.eventapi.dto.EventRequest;
import com.tejas.eventapi.dto.EventResponse;
import com.tejas.eventapi.exception.ResourceNotFoundException;
import com.tejas.eventapi.model.Event;
import com.tejas.eventapi.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The SERVICE layer sits between the controller and the repository.
 *
 * Why not just call the repository directly from the controller?
 * Because the controller's job is "handle HTTP" (status codes, request/
 * response mapping) and the repository's job is "talk to the database."
 * Business logic (validation rules, calculations, orchestration across
 * multiple repositories) belongs in its own layer so it's testable and
 * reusable independent of HTTP.
 *
 * For this simple project the service layer looks thin, almost like it's
 * "just calling the repository" — that's normal and expected at this
 * scale. The value shows up as soon as you add real logic (like the
 * capacity check in ParticipantService), which doesn't belong in a
 * controller.
 */
@Service
public class EventService {

    private final EventRepository eventRepository;

    // Constructor injection: Spring sees this constructor and automatically
    // passes in an EventRepository instance when it creates an EventService
    // bean. This is the recommended way to do dependency injection in
    // modern Spring (preferred over @Autowired on a field).
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public EventResponse createEvent(EventRequest request) {
        Event event = new Event(request.getName(), request.getDate(), request.getCapacity());
        Event saved = eventRepository.save(event);
        return new EventResponse(saved);
    }

    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(EventResponse::new)
                .toList();
    }

    public EventResponse getEventById(Long id) {
        Event event = findEventOrThrow(id);
        return new EventResponse(event);
    }

    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new ResourceNotFoundException("Event not found with id: " + id);
        }
        eventRepository.deleteById(id);
    }

    /**
     * Package-private helper used by ParticipantService too, so both
     * services throw the same consistent "event not found" error instead
     * of duplicating this lookup-or-404 logic in two places.
     */
    Event findEventOrThrow(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
    }
}
