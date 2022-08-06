package it.petrovich.rssprocessor.config;

import it.petrovich.rss.domain.refactoring.RssType;
import it.petrovich.rss.notification.NotificationProvider;
import it.petrovich.rss.xml.XmlConfiguration;
import it.petrovich.rssprocessor.processor.AtomProcessor;
import it.petrovich.rssprocessor.processor.FeedProcessor;
import it.petrovich.rssprocessor.processor.Rss20Processor;
import it.petrovich.rssprocessor.service.SubscriptionService;
import it.petrovich.rssprocessor.storage.InMemoryRssStorage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
@Configuration
@RequiredArgsConstructor
@Import(XmlConfiguration.class)
public class ProcessorAutoConfigurer {

    @Bean
    public Collection<FeedProcessor> feedProcessors(final NotificationProvider notificationProvider) {
        return List.of(new AtomProcessor(notificationProvider), new Rss20Processor(notificationProvider));
    }

    @Bean
    public Map<RssType, FeedProcessor> processorsMap(final Collection<FeedProcessor> feedProcessors) {
        return feedProcessors
                .stream()
                .collect(Collectors.toMap(FeedProcessor::getType, Function.identity()));
    }

    @Bean
    public SubscriptionService subscriptionService(final Map<RssType, FeedProcessor> processorsMap) {
        return new SubscriptionService(new InMemoryRssStorage(), processorsMap);
    }


}
