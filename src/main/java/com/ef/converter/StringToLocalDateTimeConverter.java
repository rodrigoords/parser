package com.ef.converter;

import com.ef.utils.WalletHubConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StringToLocalDateTimeConverter implements WalletHubConverter<LocalDateTime> {

  private Integer index;
  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

  public StringToLocalDateTimeConverter(Integer index){

    this.index = index;
  }

  @Override
  public Integer getIndex() {
    return index;
  }

  @Override
  public LocalDateTime convert(String string) {
    return LocalDateTime.parse(string, formatter);
  }
}
