package it.petrovich.api.rest;

import it.petrovich.api.model.SubscriptionRequest;
import it.petrovich.api.model.SubscriptionResponse;
import it.petrovich.rss.domain.refactoring.StoreFeedRequest;
import it.petrovich.rss.domain.refactoring.StoreFeedResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(imports = {UUID.class})
public interface ApiToDomainMapper {

    ApiToDomainMapper INSTANCE = Mappers.getMapper(ApiToDomainMapper.class);

    @Mapping(target = "refreshInterval", source = "refresh")
    @Mapping(target = "uri", source = "url")
    StoreFeedRequest map(SubscriptionRequest source);


    SubscriptionResponse map(StoreFeedResponse domainResponse);
}
