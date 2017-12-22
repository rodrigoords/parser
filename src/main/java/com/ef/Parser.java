package com.ef;

import com.ef.service.WalletHubParserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

@SpringBootApplication
@Slf4j
public class Parser implements CommandLineRunner{

  private WalletHubParserService walletHubParserService;

  @Value("${accesslog}")
  private String pathTofile;

  @Value("${filename:access.log}")
  private String fileName;

  @Value("${startDate}")
  private String startDate;

  @Value("${duration:daily}")
  private String duration;

  @Value("${threshold}")
  private Long threshold;

  @Value("${datePattern:yyyy-MM-dd HH:mm:ss}")
  private String datePattern;

  @Value("${separatorRegex:\\|}")
  private String separatorRegex;

  @Autowired
  public Parser(WalletHubParserService walletHubParserService) {
    this.walletHubParserService = walletHubParserService;
  }

  public static void main(String[] args) {
		SpringApplication.run(Parser.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
    try{

      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
      LocalDateTime startDateTime = LocalDateTime.parse(startDate, formatter);

      log.info("======================= Process Started =======================");

      try (Stream<String> lines = Files.lines(Paths.get(pathTofile + "/" + fileName))) {
        log.info("======================= Consuming file data =======================");
        walletHubParserService.consume(lines, separatorRegex);
        log.info("======================= Finished file data consuming =======================");
      }

      log.info("======================= Start data analysis =======================");
      walletHubParserService.filterBlockedIPs(startDateTime, duration, threshold);
      log.info("======================= Finished data analysis =======================");

    }catch (DateTimeParseException e){
      throw new RuntimeException("Can't parse start date param, make sure that your date is yyyy-MM-dd HH:mm:ss pattern or configure a custom " +
              "pattern setting datePattern param");
    }
	}
}
