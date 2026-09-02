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





