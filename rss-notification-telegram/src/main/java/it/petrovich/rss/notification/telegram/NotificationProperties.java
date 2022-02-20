package it.petrovich.rss.notification.telegram;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collection;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.notification.telegram")
public class NotificationProperties {

    private String token;
    private String username;
    private Collection<String> chats;

}
