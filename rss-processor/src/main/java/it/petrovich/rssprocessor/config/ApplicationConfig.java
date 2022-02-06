package it.petrovich.rssprocessor.config;

import it.petrovich.rss.xml.XmlConfiguration;
import it.petrovich.rssprocessor.dto.RssType;
import it.petrovich.rssprocessor.service.processor.FeedProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@Import(XmlConfiguration.class)
public class ApplicationConfig {

    @Bean
    public RestTemplate restTemplate(final RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public Map<RssType, FeedProcessor> processors(final Collection<FeedProcessor> processorCollection) {
        return processorCollection
                .stream()
                .collect(Collectors.toMap(FeedProcessor::getType, Function.identity()));
    }
}
