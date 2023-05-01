package com.bjoggis.bot.valorant;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "valorant")
public record ValorantProperties(String webhook) {

}
