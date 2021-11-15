package it.petrovich.rssprocessor.dto;

import java.util.UUID;

public record StoreFeedResponse(UUID id, RegistrationStatus status, String description) {
}
