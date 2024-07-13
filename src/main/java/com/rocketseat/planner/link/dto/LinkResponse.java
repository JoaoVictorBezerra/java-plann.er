package com.rocketseat.planner.link.dto;

import java.util.UUID;

public record LinkResponse(UUID id, String title, String url) {
}
