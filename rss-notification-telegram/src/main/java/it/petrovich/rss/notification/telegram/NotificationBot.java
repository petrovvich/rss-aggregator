package it.petrovich.rss.notification.telegram;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@AllArgsConstructor
public final class NotificationBot extends TelegramLongPollingBot {

    private final String token;
    private final String username;

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(final Update update) {

    }

    @Override
    public void clearWebhook() {

    }

    @SneakyThrows
    public void sendMessage(final String messageBody, final String chatId) {
        val message = new MessageEntity();
        message.setText(messageBody);
        execute(SendMessage.builder()
                .chatId(chatId)
                .entities(List.of(message))
                .build());
    }
}
