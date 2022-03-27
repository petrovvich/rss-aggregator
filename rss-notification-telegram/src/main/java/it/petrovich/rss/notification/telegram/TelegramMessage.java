package it.petrovich.rss.notification.telegram;

import java.text.MessageFormat;

public final class TelegramMessage {
    private String description;
    private String title;
    private String link;

    private TelegramMessage() {
    }

    public String format(final String template) {
        return MessageFormat.format(template, title, description, link);
    }

    public static TgMessageBuilder builder() {
        return new TgMessageBuilder();
    }

    public static class TgMessageBuilder {
        private final TelegramMessage instance = new TelegramMessage();

        public TgMessageBuilder description(final String description) {
            instance.description = description;
            return this;
        }

        public TgMessageBuilder title(final String title) {
            instance.title = title;
            return this;
        }

        public TgMessageBuilder link(final String link) {
            instance.link = link;
            return this;
        }

        public TelegramMessage build() {
            return instance;
        }
    }
}
