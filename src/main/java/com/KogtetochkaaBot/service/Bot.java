package com.KogtetochkaaBot.service;

import com.KogtetochkaaBot.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class Bot extends TelegramLongPollingBot {

    final Config config;

    public Bot(Config config) {
        this.config = config;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Запустить бота"));
        listOfCommands.add(new BotCommand("/services", "Получить прайс-лист услуг"));
        listOfCommands.add(new BotCommand("/schedule", "Записаться на услугу"));
        listOfCommands.add(new BotCommand("/promo", "Специальные предложения и скидки"));
        listOfCommands.add(new BotCommand("/feedback", "Посмотреть отзывы и примеры работ или оставить свой"));
        listOfCommands.add(new BotCommand("/history", "Помним каждый ваш визит"));
        listOfCommands.add(new BotCommand("/stop", "Остановить бота"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Неудалось создать меню " + e.getMessage());
        }
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }


    @Override
    public void onUpdateReceived(@NonNull Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();

            long chatId = update.getMessage().getChatId();
            String memberName = update.getMessage().getChat().getFirstName();

            switch (messageText) {
                case "/start":
                    startBot(chatId, memberName);
                    break;
                case "/services":
                    servicesBotCommand(chatId, memberName);
                    break;
                case "/schedule":
                    scheduleBotCommand(chatId, memberName);
                    break;
                case "/promo":
                    getPromoBotCommand(chatId, memberName);
                    break;
                case "/feedback":
                    getFeedbackBotCommand(chatId, memberName);
                    break;
                case "/history":
                    getHistoryBotCommand(chatId, memberName);
                    break;
                default:
                    sendMessage(chatId, "Не распознал команду. Попробуйте еще раз.");
                    log.info("Unexpected message");
            }
        }
    }

    private void getHistoryBotCommand(long chatId, String userName) {
        String text = "История заказов: ";
        sendMessage(chatId, text);
        log.info("Была вызвана команда /history");
    }

    private void getFeedbackBotCommand(long chatId, String userName) {
        String text = "Новые отзывы о моей нашей работе";
        sendMessage(chatId, text);
        log.info("Была вызвана команда /feedback");
    }

    private void getPromoBotCommand(long chatId, String userName) {
        String text = "Здесь будут акции и все такое";
        sendMessage(chatId, text);
        log.info("Была вызвана команда /promo");
    }

    private void scheduleBotCommand(long chatId, String userName) {
        String test = "Функция записи через бота в данный момент не доступна";
        sendMessage(chatId, test);
        log.info("Была вызвана команда /schedule");
    }

    private void servicesBotCommand(long chatId, String userName) {
        String text = "STANDART\n" +
                "\n" +
                "Входит в стоимость:\n" +
                "1. Однотонный маникюр\n" +
                "2. Ремонт до 4х ногтей\n" +
                "3. Снятие старого покрытия\n" +
                "4. Выравнивание гелем/базой\n" +
                "5. Массаж рук с кремом после процедуры \n" +
                "PREMIUM\n" +
                "Входит в стоимость:\n" +
                "1. Маникюр STANDART\n" +
                "2. Укрепление полигелем/акригелем\n" +
                "3. Дизайн на ваш выбор \n" +
                "VIP\n" +
                "Входит в стоимость:\n" +
                "1. Маникюр PREMIUM\n" +
                "2. Моделирование ногтей\n" +
                "3. Наращивание гелем ";
        sendMessage(chatId, text);
        log.info("servicesBotCommand to " + userName + " success done");
    }

    private void startBot(long chatId, String userName) {
        StringBuilder sb = new StringBuilder();
        String hello = "Здравствуйте " + userName + "!";
        String help = "Я умный бот салона Когтеточка. Я помогу вам подобрать нужную услугу и записаться к мастеру.";
        String question = "Чем я могу вам помочь?";
        sb.append(hello);
        sb.append("\n");
        sb.append(help);
        sb.append("\n");
        sb.append(question);
        sendMessage(chatId, sb.toString());
        log.info("Success reply command /start to user: " + userName);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);
        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e) {
            log.error("Send message error");
        }
    }
}

