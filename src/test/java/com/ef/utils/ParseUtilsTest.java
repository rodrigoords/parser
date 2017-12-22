package com.ef.utils;

import com.ef.model.WalletHubLog;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ParseUtilsTest {

  @Test
  public void lineMustParseDateCorrectly(){

    //given
    WalletHubLog walletHubLog = WalletHubLog.builder().build();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    String line = "2017-01-01 00:00:11.763|192.168.234.82|\"GET / HTTP/1.1\"|200|\"swcd (unknown version) CFNetwork/808.2.16 Darwin/15.6.0\"";

    //when
    WalletHubLog hubLog = (WalletHubLog) ParseUtils.line(walletHubLog, line, "\\|");
    //then
    Assert.assertTrue("error to verify date", hubLog.getDate().equals(LocalDateTime.parse("2017-01-01 00:00:11.763", formatter)));
    Assert.assertTrue("error to verify ip", hubLog.getIp().equals("192.168.234.82"));
    Assert.assertTrue("error to verify request", hubLog.getRequest().equals("GET / HTTP/1.1"));
    Assert.assertTrue("error to verify status", hubLog.getStatus().equals("200") );
    Assert.assertTrue("error to verify userAgent", hubLog.getUserAgent().equals("swcd (unknown version) CFNetwork/808.2.16 Darwin/15.6.0"));
  }

  @Test
  public void lineMustDontBrokenWhenIndexOutOfBound(){
    //gieven
    String line = "2017-01-01 00:00:11.763|192.168.234.82|\"GET / HTTP/1.1\"|200";

    WalletHubLog walletHubLog = WalletHubLog.builder().build();

    //when
    WalletHubLog hubLog = (WalletHubLog)ParseUtils.line(walletHubLog, line, "\\|");

    //then
    Assert.assertTrue("Last index need to be null", Objects.isNull(hubLog.getUserAgent()));
  }

}