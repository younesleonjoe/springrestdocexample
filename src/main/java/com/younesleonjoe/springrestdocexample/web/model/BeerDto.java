package com.younesleonjoe.springrestdocexample.web.model;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerDto {

  @Null private UUID id;

  @Null private Integer version;

  @Null private OffsetDateTime createdAt;

  @Null private OffsetDateTime updatedAt;

  @NotBlank
  @Size(min = 3, max = 100)
  private String beerName;

  @NotNull private BeerStyleEnum beerStyle;

  @Positive @NotNull private Long upc;

  @Positive @NotNull private BigDecimal price;

  private Integer quantityOnHand;
}
