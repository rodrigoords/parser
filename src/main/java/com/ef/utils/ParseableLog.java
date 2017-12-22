package com.ef.utils;

import org.springframework.data.domain.Persistable;

import java.util.Map;

public interface ParseableLog extends Persistable<Long> {
  Map<String,WalletHubConverter> getMapFields();
}
