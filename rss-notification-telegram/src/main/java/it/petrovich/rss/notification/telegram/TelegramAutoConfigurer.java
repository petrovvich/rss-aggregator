package it.petrovich.rss.notification.telegram;

import it.petrovich.rss.notification.NotificationProcessor;
import it.petrovich.rss.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(value = {NotificationProperties.class})
@ConditionalOnExpression(value = "#{'${app.notification.channels}'.contains('TG')}")
public class TelegramAutoConfigurer {

    @Bean
    public Collection<NotificationProcessor> notificationProcessors() {
        return List.of(new Rss20NotificationProcessor());
    }

    @Bean
    public Map<String, NotificationProcessor> notificationProcessorMap(Collection<NotificationProcessor> processors) {
        return processors.stream()
                .collect(Collectors.toMap(NotificationProcessor::getType, Function.identity()));
    }

    @Bean
    public NotificationService tgNotificationService(Map<String, NotificationProcessor> map) {
        return new TelegramNotificationService(map);
    }
}
