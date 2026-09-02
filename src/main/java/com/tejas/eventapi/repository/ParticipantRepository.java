package com.tejas.eventapi.repository;

import com.tejas.eventapi.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    /**
     * Spring Data JPA parses this method NAME and generates the SQL query
     * automatically — no @Query annotation or SQL string needed.
     *
     * "findByEventId" -> SELECT * FROM participants WHERE event_id = ?
     *
     * This is a good one to understand deeply for an interview: it looks
     * like magic the first time you see it, but it's just Spring parsing
     * the method name ("findBy" + "EventId") and matching it to the
     * entity's field structure.
     */
    List<Participant> findByEventId(Long eventId);

    long countByEventId(Long eventId);
}
