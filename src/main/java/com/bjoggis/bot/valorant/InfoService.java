package com.bjoggis.bot.valorant;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
class InfoService {

  record GameInfo(String map, LocalDateTime date) {

  }

  record Content(String content) {

  }

  private final Logger logger = LoggerFactory.getLogger(InfoService.class);

  private final List<GameInfo> games = new ArrayList<>();
  private final RestTemplate restTemplate = new RestTemplate();
  private final ValorantProperties valorantProperties;


  public InfoService(ValorantProperties valorantProperties) {
    this.valorantProperties = valorantProperties;
    games.add(new GameInfo("Test", LocalDateTime.of(2023, Month.APRIL, 30, 23, 0)));
    games.add(new GameInfo("Ascent", LocalDateTime.of(2023, Month.MAY, 1, 19, 0)));
    games.add(new GameInfo("System Test", LocalDateTime.of(2023, Month.MAY, 3, 19, 0)));
    games.add(new GameInfo("Pearl", LocalDateTime.of(2023, Month.MAY, 6, 20, 0)));
    games.add(new GameInfo("Pearl", LocalDateTime.of(2023, Month.MAY, 8, 19, 0)));
    games.add(new GameInfo("Bind", LocalDateTime.of(2023, Month.MAY, 13, 20, 0)));
    games.add(new GameInfo("Bind", LocalDateTime.of(2023, Month.MAY, 15, 19, 0)));
    games.add(new GameInfo("Haven", LocalDateTime.of(2023, Month.MAY, 20, 20, 0)));
    games.add(new GameInfo("Playoffs", LocalDateTime.of(2023, Month.MAY, 21, 19, 0)));
  }

  @Scheduled(cron = "0 0 10 * * ?")
  void runner() {
    LocalDate today = LocalDate.now();
    Optional<GameInfo> result = games.stream()
        .filter(game -> game.date().toLocalDate().equals(today)).findFirst();

    if (result.isPresent()) {
      logger.info("The first LocalDateTime that matches today's date is " + result.get());
    } else {
      logger.info("No LocalDateTime found that matches today's date.");
      return;
    }

    String text = "Ny Valorant Premier kamp i kveld klokka " + result.get().date().toLocalTime()
        + ".\n**Map: " + result.get().map() + "**" + "\nMøt opp minst 10 minutter før kampstart.\n";

    final Content content = new Content(text);

    logger.info("Sending message to Discord: " + content);

    restTemplate.postForObject(valorantProperties.webhook(), content, String.class);
  }
}
