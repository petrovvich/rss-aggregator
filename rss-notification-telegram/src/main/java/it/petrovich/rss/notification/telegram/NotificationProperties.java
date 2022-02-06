package it.petrovich.rss.notification.telegram;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collection;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.notification")
public class NotificationProperties {

    private Collection<String> channels;
}
