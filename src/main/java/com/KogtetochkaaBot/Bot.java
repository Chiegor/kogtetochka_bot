package com.KogtetochkaaBot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component // позволит спрингу создать бин из этого класса
@Slf4j
public class Bot extends TelegramLongPollingBot { // есть еще вариант с WebhookBot
    // TelegramLongPollingBot - периодически сам проверяет напсиал ли ему пользователь. Менее подходит для большого трафика
    // WebhookBot - уведомляет сервер каждыйраз когда ему написал пользователь. Более мощный вариант

    final Config config;

    public Bot(Config config) {
        this.config = config;
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    // главный метод всего приложения
    // что должен делать бот когда ему кто-то пишет
    // класс Update содержит сообщения который пользователь посылает боту и другую информацию о пользователе
    @Override
    public void onUpdateReceived(@NonNull Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();

            // чтобы бот понимал кому отвечать т.к. запросы могут идти от разных юзеров
            long chatId = update.getMessage().getChatId();
            String memberName = update.getMessage().getChat().getFirstName();

            switch (messageText) {
                case "/start":
                    startBot(chatId, memberName);
                    break;
                default:
                    sendMessage(chatId, "Я таких слов не знаю, я просто маленький поросёнок");
                    log.info("Unexpected message");
            }
        }
    }

    private void startBot(long chatId, String userName) {
        StringBuilder sb = new StringBuilder();
        String answer = "Привет поросеночек " + userName;
        sb.append(answer);
        String text = "STANDART\n" +
                "\n" +
                "Входит в стоимость:\n" +
                "\n" +
                "1. Однотонный маникюр\n" +
                "\n" +
                "2. Ремонт до 4х ногтей\n" +
                "\n" +
                "3. Снятие старого покрытия\n" +
                "\n" +
                "4. Выравнивание гелем/базой\n" +
                "\n" +
                "5. Массаж рук с кремом после процедуры \n" +
                "PREMIUM\n" +
                "\n" +
                "Входит в стоимость:\n" +
                "\n" +
                "1. Маникюр STANDART\n" +
                "\n" +
                "2. Укрепление полигелем/акригелем\n" +
                "\n" +
                "3. Дизайн на ваш выбор \n" +
                "VIP\n" +
                "\n" +
                "Входит в стоимость:\n" +
                "\n" +
                "1. Маникюр PREMIUM\n" +
                "\n" +
                "2. Моделирование ногтей\n" +
                "\n" +
                "3. Наращивание гелем ";
        sb.append(text);
        sendMessage(chatId, sb.toString());
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);
        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}

