package com.rocketseat.planner.participant;

import com.rocketseat.planner.core.constants.api.Routes;
import com.rocketseat.planner.participant.dto.ConfirmParticipantRequest;
import com.rocketseat.planner.participant.dto.ParticipantDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(Routes.PARTICIPANTS)
public class ParticipantController {

    @Autowired
    private ParticipantRepository repository;

    @Autowired
    private ParticipantsService service;

    @PostMapping("/{id}/confirm")
    public ResponseEntity<Participant> confirmParticipant(@PathVariable("id") UUID id, @RequestBody ConfirmParticipantRequest body) {
        Optional<Participant> participant = this.repository.findById(id);

        if (participant.isPresent()) {
            Participant rawParticipant = participant.get();
            rawParticipant.setConfirmed(true);
            rawParticipant.setName(body.name());

            this.repository.save(rawParticipant);

            return ResponseEntity.ok(rawParticipant);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/trip/{id}")
    public ResponseEntity<List<ParticipantDataResponse>> getAllParticipantsByTripId(@PathVariable("id") UUID id) {
        List<ParticipantDataResponse> participants = this.service.getAllParticipantsByTripId(id);

        return ResponseEntity.ok(participants);
    }
}
