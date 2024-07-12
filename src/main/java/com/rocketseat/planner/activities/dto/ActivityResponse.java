package com.rocketseat.planner.activities.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ActivityResponse(UUID id, String title, LocalDateTime occursAt) {
}
