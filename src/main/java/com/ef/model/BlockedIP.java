package com.ef.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Tolerate;
import org.springframework.data.domain.Persistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Table(name = "blocked_ips")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlockedIP implements Persistable<Long> {

  @Tolerate
  public BlockedIP(String ip, Long qtd) {
    this.ip = ip;
    this.qtd = qtd;
    this.blockReason = "because of reasons";
    this.processDate = LocalDateTime.now();
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String ip;

  @Column
  private Long qtd;

  @Column(name = "block_reason")
  private String blockReason;

  @Column(name = "process_date")
  private LocalDateTime processDate;

  @Override
  public Long getId() {
    return null;
  }

  @Override
  public boolean isNew() {
    return false;
  }

}
