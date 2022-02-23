package it.petrovich.rss.provider;

import it.petrovich.rss.notification.NotificationService;
import it.petrovich.rss.notification.telegram.TelegramAutoConfigurer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.val;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Getter
@Setter
@Configuration
@RequiredArgsConstructor
@Import(TelegramAutoConfigurer.class)
public class ProviderAutoConfigurer {

    @Bean
    public NotificationProviderImpl provider(final ListableBeanFactory beanFactory) {
        val notificationServices = beanFactory.getBeansOfType(NotificationService.class);
        return new NotificationProviderImpl(notificationServices.values());
    }
}
