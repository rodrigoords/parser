package com.ef.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Map;

@Slf4j
public class ParseUtils {

  private ParseUtils(){}

  public static ParseableLog line(ParseableLog parseableLog, String line, String separator) {

    Class<? extends ParseableLog> clazz = parseableLog.getClass();

    final String[] split = line.split(separator);

    Map<String, WalletHubConverter> mapFields = parseableLog.getMapFields();

    mapFields.forEach((k,v) ->{
      try{
        Field declaredField = clazz.getDeclaredField(k);
        declaredField.setAccessible(true);
        declaredField.set(parseableLog, v.convert(split[v.getIndex()]));
      } catch (IllegalAccessException | NoSuchFieldException e) {
        e.printStackTrace();
      } catch (IndexOutOfBoundsException e){
        log.info("field getIndex not in file log");
      }
    });

    return parseableLog;
  }
}
