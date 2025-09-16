package com.ducbrick.finance_tracker.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TransactionDetailsDto {
  @Valid
  @NotNull
  private NewTransactionDetailsDto details;

  @NotNull
  private Integer id;
}
