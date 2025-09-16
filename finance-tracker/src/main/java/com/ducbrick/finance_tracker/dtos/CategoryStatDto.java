package com.ducbrick.finance_tracker.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
public class CategoryStatDto {
  @NotNull
  @Valid
  private CategoryDetailsDto categoryDetails;

  @NotNull
  @PositiveOrZero
  private BigDecimal expense;

  @NotNull
  @PositiveOrZero
  private BigDecimal income;

  public void addIncome(@NotNull BigDecimal value) {
    income = income.add(value);
  }

  public void addExpense(@NotNull BigDecimal value) {
    expense = expense.add(value);
  }
}
