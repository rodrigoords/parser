package com.ef.service;

import com.ef.enumeration.DurationEnum;
import com.ef.model.BlockedIP;
import com.ef.model.WalletHubLog;
import com.ef.repository.BlockedIpRepository;
import com.ef.repository.CustomRepository;
import com.ef.utils.ParseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@Slf4j
public class WalletHubParserService {

  private CustomRepository customRepository;
  private BlockedIpRepository blockedIpRepository;

  private static final String INVALID_DURATION_ERROR = "Invalid duration options please use hourly or daily";

  @Autowired
  public WalletHubParserService(CustomRepository customRepository, BlockedIpRepository blockedIpRepository)  {
    this.customRepository = customRepository;
    this.blockedIpRepository = blockedIpRepository;
  }

  public void consume(Stream<String> lines, String separatorRegex){

    List<WalletHubLog> walletHubLogs = new ArrayList<>();

    log.info("iterating over lines");
    lines.forEach(line -> {

      WalletHubLog walletHubLog = WalletHubLog.builder().build();

      ParseUtils.line(walletHubLog, line, separatorRegex);

      walletHubLogs.add(walletHubLog);

    });

    log.info("insert data in database");
    LocalDateTime start = LocalDateTime.now();

    this.customRepository.bulkSave(walletHubLogs);

    LocalDateTime end = LocalDateTime.now();
    log.info("finished");

    this.logTimeDiff(start, end);

  }

  public void filterBlockedIPs(LocalDateTime startDateTime, String duration, Long threshold) {

    LocalDateTime start = LocalDateTime.now();

    LocalDateTime filterDateTime = getFilterDateTime(startDateTime,duration);
    threshold = findThresholdValue(threshold, duration);

    List<BlockedIP> analysis = this.customRepository.analysis(startDateTime, filterDateTime, threshold);
    analysis.forEach(analyse -> {
      log.info("IP: " + analyse.getIp() + " entries qtd: " + analyse.getQtd());
      this.blockedIpRepository.save(analyse);
    });

    LocalDateTime end = LocalDateTime.now();

    this.logTimeDiff(start, end);
  }

  private Long findThresholdValue(Long threshold, String duration){

    if(Objects.isNull(threshold)){
      if(DurationEnum.HOURLY.name().equalsIgnoreCase(duration)){
        threshold = 200L;
      }else if(DurationEnum.DAILY.name().equalsIgnoreCase(duration)){
        threshold = 500L;
      }else{
        throw new RuntimeException(INVALID_DURATION_ERROR);
      }
    }

    return threshold;
  }

  private LocalDateTime getFilterDateTime(LocalDateTime startDateTime, String duration) {
    LocalDateTime dateTimeFilter;

    if(DurationEnum.HOURLY.name().equalsIgnoreCase(duration)){
      dateTimeFilter = startDateTime.plusHours(1L);
    }else if(DurationEnum.DAILY.name().equalsIgnoreCase(duration)){
      dateTimeFilter = startDateTime.plusDays(1L);
    }else{
      throw new RuntimeException(INVALID_DURATION_ERROR);
    }

    return dateTimeFilter;
  }

  private void logTimeDiff(LocalDateTime start, LocalDateTime end){

    Instant instant = Instant.ofEpochMilli ( Duration.between(start, end).toMillis() );
    ZonedDateTime zdt = ZonedDateTime.ofInstant ( instant , ZoneOffset.UTC );

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern ( "HH:mm:ss:SSS" );
    String output = formatter.format ( zdt );

    log.info("time: " + output);
  }
}
