package com.KogtetochkaaBot.controller;

public interface BotController {
    void startBot(long chatId, String userName);
    void getHistoryBotCommand(long chatId, String userName);
    void getFeedbackBotCommand(long chatId, String userName);
    void getPromoBotCommand(long chatId, String userName);
    void scheduleBotCommand(long chatId, String userName);
    void servicesBotCommand(long chatId, String userName);
}
