package it.petrovich.rss.domain.storing;

import it.petrovich.rss.domain.RegistrationStatus;

import java.util.UUID;

public record StoreFeedResponse(UUID id, RegistrationStatus status, String description) {
}
