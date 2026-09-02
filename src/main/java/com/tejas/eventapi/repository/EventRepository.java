package com.tejas.eventapi.repository;

import com.tejas.eventapi.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This is the repository layer — it talks directly to the database.
 *
 * By extending JpaRepository<Event, Long>, we get a full set of CRUD
 * methods for free, with ZERO implementation code written by us:
 *   save(event)        -> INSERT or UPDATE
 *   findById(id)        -> SELECT ... WHERE id = ?
 *   findAll()           -> SELECT * FROM events
 *   deleteById(id)       -> DELETE ... WHERE id = ?
 *   existsById(id)       -> checks if a row exists
 *
 * Long is the type of the @Id field in Event.
 *
 * This is what video #12 (Spring Data JPA) was showing you — Spring
 * generates the implementation of this interface at runtime.
 */
public interface EventRepository extends JpaRepository<Event, Long> {
    // We don't need any custom queries for this simple version,
    // but if we did, we could add a method here like:
    // List<Event> findByDate(LocalDate date);
    // and Spring would auto-generate the SQL from the method name.
}
