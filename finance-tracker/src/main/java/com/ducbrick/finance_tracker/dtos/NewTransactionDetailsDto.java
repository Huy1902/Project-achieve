package com.ducbrick.finance_tracker.dtos;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class NewTransactionDetailsDto {
  @NotBlank
  @Size(max = 256)
  private String name;

  @NotNull
  private BigDecimal value;

  private String notes;

  @NotNull
  private LocalDateTime timestamp;

  @NotNull
  private Integer categoryId;
}
