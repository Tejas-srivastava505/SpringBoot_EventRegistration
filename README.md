# Event Registration & Check-in API

A small Spring Boot REST API for registering participants to events and
tracking check-ins — inspired by organizing TechTatva (300+ teams,
1000+ registrations) and Aurora Tech Week (8+ workshops, 250+
participants) at MIT Manipal.

## Tech Stack
- Java 17
- Spring Boot 3.3
- Spring Data JPA (Hibernate)
- MySQL
- Maven

> **Note:** this version writes all getters/setters explicitly instead
> of using Lombok (`@Getter`/`@Setter`). Lombok requires annotation
> processing to be correctly wired into the build, which is a common
> source of "cannot find symbol: getX()" compile errors when it isn't
> configured exactly right — not worth the risk this close to a
> deadline. Explicit getters/setters are more verbose but guaranteed
> to compile with zero extra config, and it's one less tool to have to
> explain in an interview.

## Features
- Create and list events, each with a capacity limit
- Register participants to an event
- **Registration is rejected once an event reaches capacity** (the one
  piece of real business logic in this project, beyond plain CRUD)
- Mark a participant as checked in
- Cancel a registration
- Clean JSON error responses for not-found and validation failures
- Swagger UI for testing every endpoint from the browser

## Setup

1. Install MySQL locally if you don't have it, and create the database:
   ```sql
   CREATE DATABASE event_registration_db;
   ```
2. Open `src/main/resources/application.properties` and set your MySQL
   username/password.
3. Run the app:
   ```bash
   mvn spring-boot:run
   ```
   The API starts on `http://localhost:8080`. Tables are created
   automatically on first run (`ddl-auto=update`).
4. Open **`http://localhost:8080/swagger-ui.html`** in your browser.
   This gives you a page listing every endpoint, where you can fill in
   fields and click "Execute" to send real requests — no curl, no
   Postman needed. This is the easiest way to test everything below.

## API Endpoints

| Method | Path                                  | Description                        |
|--------|----------------------------------------|-------------------------------------|
| POST   | `/api/events`                          | Create an event                     |
| GET    | `/api/events`                          | List all events                     |
| GET    | `/api/events/{id}`                     | Get one event                       |
| DELETE | `/api/events/{id}`                     | Delete an event                     |
| POST   | `/api/events/{eventId}/participants`   | Register a participant to an event  |
| GET    | `/api/events/{eventId}/participants`   | List participants for an event      |
| PATCH  | `/api/participants/{id}/checkin`       | Mark a participant as checked in    |
| DELETE | `/api/participants/{id}`               | Cancel a registration               |

## Sample requests (for Postman / curl)

**Create an event:**
```bash
curl -X POST http://localhost:8080/api/events \
  -H "Content-Type: application/json" \
  -d '{"name": "Aurora Tech Week - AI Workshop", "date": "2026-09-15", "capacity": 2}'
```

**Register a participant (repeat with a 3rd person to see the capacity error):**
```bash
curl -X POST http://localhost:8080/api/events/1/participants \
  -H "Content-Type: application/json" \
  -d '{"name": "Tejas Srivastava", "email": "tejas@example.com"}'
```

**Check in a participant:**
```bash
curl -X PATCH http://localhost:8080/api/participants/1/checkin
```

---

## Before an interview: things to actually understand

Don't just skim this — these are the questions most likely to come up
if this project is on your resume.

### 1. Walk through what happens when a POST request to register a
participant comes in
Request hits `ParticipantController.register()` → `@Valid` checks
`ParticipantRequest` (name/email present, email format valid) → if
invalid, `GlobalExceptionHandler` catches it and returns 400 before
your code even runs → if valid, calls `ParticipantService
.registerParticipant()` → service asks `EventService` to find the
event (404 if missing) → counts current participants via
`ParticipantRepository.countByEventId()` → if count >= capacity, throws
`EventFullException` (→ 409) → otherwise saves the participant and
returns a `ParticipantResponse`.

### 2. Why three layers (Controller / Service / Repository) instead of
just Controller + Repository?
Controller = HTTP concerns only (status codes, request/response
shape). Service = business rules (the capacity check). Repository =
database access only. Keeping business logic out of the controller
means it's testable without spinning up a web server, and keeping it
out of the repository means the repository stays a dumb, swappable
data-access layer.

### 3. Why DTOs (`EventRequest`/`EventResponse`) instead of returning
the `@Entity` classes directly from controllers?
Two reasons: (a) it avoids the circular JSON problem — `Event` has a
list of `Participant`s, each of which has a reference back to
`Event`, so serializing the raw entity can recurse infinitely; (b) it
lets the API's public shape (what JSON goes in/out) evolve
independently of the database schema — you can rename or restructure
a database column without breaking every client of the API.

### 4. Why `findByEventId` works with no SQL written
Spring Data JPA parses the *method name itself* at startup —
`findBy` + `EventId` — and generates the equivalent SQL
(`SELECT * FROM participants WHERE event_id = ?`) automatically,
because it can see `Participant` has an `event` field with an `id`.
This is "derived query methods," one of JPA's core features.

### 5. Why 409 Conflict for a full event, not 400 Bad Request?
400 means the request itself was malformed. Here the request is
perfectly valid — the problem is the *current state of the server*
(the event happens to be full right now). 409 Conflict specifically
means "this request conflicts with the current state of the
resource," which is the more accurate HTTP semantic.

### 6. What would you add if you had more time?
Honest, interview-safe answer: authentication (so not anyone can
delete any event), pagination on `GET /api/events` for when the list
gets large, and a proper database migration tool (Flyway) instead of
relying on `ddl-auto=update`, which isn't safe for production schema
changes.
