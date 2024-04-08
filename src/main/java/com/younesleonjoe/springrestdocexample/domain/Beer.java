package com.younesleonjoe.springrestdocexample.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Beer {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(columnDefinition = "varchar(36)", updatable = false, nullable = false)
  private UUID id;

  @Version private Long version;

  @CreationTimestamp
  @Column(updatable = false)
  private Timestamp createdAt;

  @UpdateTimestamp private Timestamp updatedAt;

  private String beerName;
  private String beerStyle;

  @Column(unique = true)
  private Long upc;

  private BigDecimal price;

  private Integer minOnHand;
  private Integer quantityToBrew;
}
