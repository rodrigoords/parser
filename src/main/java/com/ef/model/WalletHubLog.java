package com.ef.model;

import com.ef.converter.StringToLocalDateTimeConverter;
import com.ef.utils.WalletHubConverter;
import com.ef.converter.BasicConverter;
import com.ef.utils.ParseableLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Table(name = "wallethub_logs")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WalletHubLog implements ParseableLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private LocalDateTime date;

  @Column
  private String ip;

  @Column
  private String request;

  @Column
  private String status;

  @Column
  private String userAgent;

  @Override
  public boolean isNew() {
    return Objects.isNull(this.id);
  }

  @Override
  public Map<String,WalletHubConverter> getMapFields() {
    Map<String, WalletHubConverter> fieldsMap = new HashMap<>();

    fieldsMap.put("date", new StringToLocalDateTimeConverter(0));
    fieldsMap.put("ip", new BasicConverter(1));
    fieldsMap.put("request", new BasicConverter(2));
    fieldsMap.put("status", new BasicConverter(3));
    fieldsMap.put("userAgent", new BasicConverter(4));

    return fieldsMap;
  }
}
