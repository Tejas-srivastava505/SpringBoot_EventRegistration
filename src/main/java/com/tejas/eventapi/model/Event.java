package com.tejas.eventapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an event (e.g. a hackathon, a workshop).
 *
 * @Entity tells Spring/JPA "this class maps to a database table."
 * By default the table is named after the class (event), and each
 * field maps to a column with the same name, unless overridden.
 */
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment, DB assigns the id
    private Long id;

    @NotBlank(message = "Event name is required")
    private String name;

    private LocalDate date;

    @Min(value = 1, message = "Capacity must be at least 1")
    private int capacity;

    /**
     * One event can have many participants registered to it.
     * mappedBy = "event" means the Participant entity owns the
     * foreign key (see Participant.java) — Event just knows about
     * the relationship, it doesn't store a foreign key itself.
     *
     * cascade = CascadeType.ALL means: if an Event is deleted,
     * its Participant records are deleted too (no orphaned rows).
     */
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants = new ArrayList<>();

    // JPA requires a no-argument constructor to build objects when
    // loading rows back out of the database.
    public Event() {
    }

    // Convenience constructor for creating a new event before it has an id
    public Event(String name, LocalDate date, int capacity) {
        this.name = name;
        this.date = date;
        this.capacity = capacity;
    }

    // --- Getters and setters ---
    // (This is exactly what @Getter/@Setter would have generated for us —
    // writing it out by hand is a good way to see that Lombok isn't doing
    // anything magical, just saving keystrokes.)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }
}
