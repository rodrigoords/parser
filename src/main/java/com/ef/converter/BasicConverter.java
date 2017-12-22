package com.ef.converter;

import com.ef.utils.WalletHubConverter;

public class BasicConverter implements WalletHubConverter<String> {

  private Integer index;

  public BasicConverter(Integer index) {
    this.index = index;
  }

  @Override
  public Integer getIndex() {
    return index;
  }

  @Override
  public String convert(String string) {
    return string.replace("\"", "");
  }
}
