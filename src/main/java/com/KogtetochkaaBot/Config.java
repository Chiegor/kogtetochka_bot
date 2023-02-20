package com.KogtetochkaaBot;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration // чтобы заработала анотация Value
@Data
@PropertySource("application.properties") // указывается источник проперти
public class Config {
    @Value("${bot.name}")
    String botName;

    @Value("${bot.token}")
    String token;

}
