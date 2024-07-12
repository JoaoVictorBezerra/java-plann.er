package com.rocketseat.planner.participant;

import com.rocketseat.planner.participant.dto.InviteParticipantResponse;
import com.rocketseat.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ParticipantsService {

    @Autowired
    private ParticipantRepository repository;


    public void registerParticipantsOnEvent(List<String> participantsToInvite, Trip trip) {
        List<Participant> participantsList = participantsToInvite
              .stream()
              .map(email -> new Participant(email, trip))
              .toList();

        this.repository.saveAll(participantsList);

    }

    public InviteParticipantResponse registerParticipant(String email, Trip trip) {
        Participant newParticipant = new Participant(email, trip);
        this.repository.save(newParticipant);
        return new InviteParticipantResponse(newParticipant.getId());
    }

    public void triggerConfirmationEmailToParticipants(UUID tripId) {

    }

    public void triggerConfirmationEmailToParticipant(String email) {

    }
}
