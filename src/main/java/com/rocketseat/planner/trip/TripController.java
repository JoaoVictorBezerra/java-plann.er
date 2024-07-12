package com.rocketseat.planner.trip;

import com.rocketseat.planner.activities.Activity;
import com.rocketseat.planner.activities.ActivityService;
import com.rocketseat.planner.activities.dto.ActivityResponse;
import com.rocketseat.planner.activities.dto.CreateActivityRequest;
import com.rocketseat.planner.activities.dto.CreateActivityResponse;
import com.rocketseat.planner.core.constants.api.Routes;
import com.rocketseat.planner.participant.ParticipantsService;
import com.rocketseat.planner.participant.dto.InviteParticipantRequest;
import com.rocketseat.planner.participant.dto.InviteParticipantResponse;
import com.rocketseat.planner.participant.dto.ParticipantDataResponse;
import com.rocketseat.planner.trip.dto.CreatedTripResponseDTO;
import com.rocketseat.planner.trip.dto.TripRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = Routes.TRIP)
public class TripController {

    @Autowired
    private ParticipantsService participantsService;

    @Autowired
    private TripRepository repository;

    @Autowired
    private ActivityService activityService;

    @PostMapping(Routes.CREATE_TRIP)
    public ResponseEntity<CreatedTripResponseDTO> createTrip(@RequestBody TripRequestDTO body) {
        Trip newTrip = new Trip(body);

        this.repository.save(newTrip);

        this.participantsService.registerParticipantsOnEvent(body.emails_to_invite(), newTrip);

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

    @GetMapping("/{id}/confirm")
    public ResponseEntity<Trip> confirmTrip(@PathVariable("id") UUID id) {
        Optional<Trip> trip = this.repository.findById(id);
        if(trip.isPresent()) {
            Trip rawTrip = trip.get();

            rawTrip.setConfirmed(true);
            this.repository.save(rawTrip);

            this.participantsService.triggerConfirmationEmailToParticipants(id);

            return ResponseEntity.ok(rawTrip);
        }
        return ResponseEntity.notFound().build();
    }


    @PostMapping("/{id}/invite")
    public ResponseEntity<InviteParticipantResponse> inviteParticipant(@PathVariable("id") UUID id, @RequestBody InviteParticipantRequest body) {
        Optional<Trip> trip = this.repository.findById(id);

        if(trip.isPresent()) {
            Trip rawTrip = trip.get();

            InviteParticipantResponse response = this.participantsService.registerParticipant(body.email(), rawTrip);

            if(rawTrip.isConfirmed()) {
                this.participantsService.triggerConfirmationEmailToParticipant(body.email());
            }

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/activity")
    public ResponseEntity<CreateActivityResponse> createActivity(@PathVariable("id") UUID id, @RequestBody CreateActivityRequest body) {
        Optional<Trip> trip = this.repository.findById(id);

        if(trip.isPresent()) {
            Trip rawTrip = trip.get();

            CreateActivityResponse response  = this.activityService.registerActivity(body.title(), body.occurs_at(), rawTrip);

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/activities")
    public ResponseEntity<List<ActivityResponse>> getAllActivitiesByTripId(@PathVariable("id") UUID id) {
        List<ActivityResponse> participants = this.activityService.getAllActivitiesByTripId(id);

        return ResponseEntity.ok(participants);
    }
}
