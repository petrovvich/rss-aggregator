package it.petrovich.rss.domain.refactoring;

import it.petrovich.rss.domain.RegistrationStatus;

import java.util.UUID;

public record StoreFeedResponse(UUID subscriptionId, RegistrationStatus status, String description) {
}
