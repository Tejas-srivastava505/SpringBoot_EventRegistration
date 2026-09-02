package com.tejas.eventapi.controller;

import com.tejas.eventapi.dto.EventRequest;
import com.tejas.eventapi.dto.EventResponse;
import com.tejas.eventapi.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @RestController = @Controller + @ResponseBody combined. It tells Spring:
 *   1. This class handles incoming HTTP requests
 *   2. Whatever a method returns should be serialized straight to the
 *      HTTP response body as JSON (instead of being treated as a view
 *      name to render, which is what plain @Controller does — that's
 *      the old Spring MVC / server-rendered-HTML pattern).
 *
 * @RequestMapping("/api/events") sets the base path — every endpoint
 * in this class is nested under /api/events.
 */
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // POST /api/events
    // @Valid triggers the @NotBlank/@Min checks on EventRequest — if they
    // fail, Spring throws MethodArgumentNotValidException before this
    // method body even runs, and GlobalExceptionHandler catches it.
    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody EventRequest request) {
        EventResponse response = eventService.createEvent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/events
    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    // GET /api/events/3
    // @PathVariable pulls the {id} segment out of the URL and binds it
    // to this method parameter.
    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    // DELETE /api/events/3
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build(); // 204, standard for a successful delete
    }
}
