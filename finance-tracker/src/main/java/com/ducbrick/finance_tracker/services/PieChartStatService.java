package com.ducbrick.finance_tracker.services;

import com.ducbrick.finance_tracker.dtos.CategoryDetailsDto;
import com.ducbrick.finance_tracker.dtos.CategoryStatDto;
import com.ducbrick.finance_tracker.dtos.StatRequestDto;
import com.ducbrick.finance_tracker.entities.Category;
import com.ducbrick.finance_tracker.entities.Transaction;
import com.ducbrick.finance_tracker.entities.User;
import com.ducbrick.finance_tracker.repos.TransactionRepo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Validated
@RequiredArgsConstructor
public class PieChartStatService {
  private final TransactionRepo transactionRepo;
  private final LoggedInUserFinderService loggedInUserFinderService;

  /**
   * Returns a {@link List} of {@link CategoryStatDto}s from all {@link Transaction} of the logged in {@link User} in the given time window.
   * This method retrieves the current {@link User}'s {@link Transaction}s, whose {@code timestamp}'s date is between
   * {@code start} (inclusive) and {@code end} (inclusive).
   * The returned {@link List} is immutable.
   *
   * @param start the start date
   * @param end the end date
   * @return an immutable {@link List} of {@link CategoryStatDto}s
   */
  public List<CategoryStatDto> get(@NotNull @Valid StatRequestDto dto) {
    LocalDate start = dto.getStart();
    LocalDate end = dto.getEnd();

    int userId = loggedInUserFinderService.getId();

    end = end.plusDays(1);

    Map<Category, CategoryStatDto> categoryStats = new HashMap<>();

    List<Transaction> transactions = transactionRepo.findByTimeWindow(start.atStartOfDay(), end.atStartOfDay(), userId);

    for (Transaction transaction : transactions) {
      Category category = transaction.getCategory();

      CategoryStatDto categoryStat = categoryStats
              .getOrDefault(category, createEmptyStat(category));

      BigDecimal transactionVal = transaction.getValue();

      if (isNegative(transactionVal)) {
        categoryStat.addExpense(transactionVal.negate());
      } else {
        categoryStat.addIncome(transactionVal);
      }

      categoryStats.put(category, categoryStat);
    }

    return categoryStats.values().stream().toList();
  }

  private boolean isNegative(@NotNull BigDecimal value) {
    int compare = value.compareTo(BigDecimal.ZERO);

    return (compare < 0);
  }

  private CategoryStatDto createEmptyStat(@NotNull @Valid Category category) {
    CategoryDetailsDto details
            = CategoryDetailsDto
            .builder()
            .id(category.getId())
            .color(category.getColor())
            .name(category.getName())
            .build();

    return CategoryStatDto
    .builder()
    .categoryDetails(details)
    .expense(BigDecimal.ZERO)
    .income(BigDecimal.ZERO)
    .build();
  }
}