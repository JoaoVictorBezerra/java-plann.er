package com.rocketseat.planner.participant.dto;

import java.util.UUID;

public record ParticipantDataResponse(UUID id, String email, boolean isConfirmed) {
}
