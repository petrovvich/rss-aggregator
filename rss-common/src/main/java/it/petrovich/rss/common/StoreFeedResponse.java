package it.petrovich.rss.common;

import java.util.UUID;

public record StoreFeedResponse(UUID id, RegistrationStatus status, String description) {
}
