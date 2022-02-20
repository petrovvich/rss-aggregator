package it.petrovich.rss.notification.telegram;

import it.petrovich.rss.notification.NotificationProcessor;
import it.petrovich.rss.notification.NotificationService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Setter
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(value = {NotificationProperties.class})
@ConditionalOnExpression(value = "#{'${app.notification.channels}'.contains('TG')}")
public class TelegramAutoConfigurer {

    private final NotificationProperties notificationProperties;

    @Bean
    public Collection<NotificationProcessor> notificationProcessors(final Collection<NotificationBot> bots) {
        return List.of(new Rss20NotificationProcessor(bots, notificationProperties));
    }

    @Bean
    public Map<String, NotificationProcessor> notificationProcessorMap(
            final Collection<NotificationProcessor> processors) {
        return processors.stream()
                .collect(Collectors.toMap(NotificationProcessor::getType, Function.identity()));
    }

    @Bean
    public NotificationService tgNotificationService(final Map<String, NotificationProcessor> map) {
        return new TelegramNotificationService(map);
    }

    @Bean
    public Collection<NotificationBot> bots() {
        return List.of(new NotificationBot(notificationProperties.getToken(), notificationProperties.getUsername()));
    }

    @Bean
    @SneakyThrows({TelegramApiException.class})
    public TelegramBotsApi telegramBotsApi(final Collection<NotificationBot> bots) {
        val apiClient = new TelegramBotsApi(DefaultBotSession.class);
        bots.forEach(bot -> {
            try {
                apiClient.registerBot(bot);
            } catch (TelegramApiException e) {
                log.error("Exception occurred while register bot {}", bot, e);
            }
        });
        return apiClient;
    }
}
