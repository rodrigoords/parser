package com.ef.utils;

public interface WalletHubConverter<T> {
  Integer getIndex();
  T convert(String string);
}
