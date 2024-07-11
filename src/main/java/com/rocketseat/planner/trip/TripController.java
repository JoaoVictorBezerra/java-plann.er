package com.rocketseat.planner.trip;

import com.rocketseat.planner.core.constants.api.Routes;
import com.rocketseat.planner.participant.ParticipantsService;
import com.rocketseat.planner.trip.dto.CreatedTripResponseDTO;
import com.rocketseat.planner.trip.dto.TripRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = Routes.TRIP)
public class TripController {

    @Autowired
    private ParticipantsService participantsService;

    @Autowired
    private TripRepository repository;

    @PostMapping(Routes.CREATE_TRIP)
    public ResponseEntity<CreatedTripResponseDTO> createTrip(@RequestBody TripRequestDTO body) {
        Trip newTrip = new Trip(body);

        this.repository.save(newTrip);

        this.participantsService.registerParticipantsOnEvent(body.emails_to_invite(), newTrip.getId());

        return ResponseEntity.ok(new CreatedTripResponseDTO(newTrip.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable("id") UUID id) {
        Optional<Trip> trip = this.repository.findById(id);

        return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable("id") UUID id, @RequestBody TripRequestDTO body) {
        Optional<Trip> trip = this.repository.findById(id);

        if(trip.isPresent()) {
            Trip rawTrip = trip.get();
            rawTrip.setDestination(body.destination());
            rawTrip.setStartsAt(LocalDateTime.parse(body.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setEndsAt(LocalDateTime.parse(body.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
            return ResponseEntity.ok(rawTrip);
        }

        return ResponseEntity.notFound().build();
    }
}
