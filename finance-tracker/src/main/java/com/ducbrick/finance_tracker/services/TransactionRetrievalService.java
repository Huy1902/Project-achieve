package com.ducbrick.finance_tracker.services;

import com.ducbrick.finance_tracker.dtos.NewTransactionDetailsDto;
import com.ducbrick.finance_tracker.dtos.TransactionDetailsDto;
import com.ducbrick.finance_tracker.entities.Transaction;
import com.ducbrick.finance_tracker.repos.TransactionRepo;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class TransactionRetrievalService {
  private final TransactionRepo transactionRepo;
  private final LoggedInUserFinderService loggedInUserFinderService;

  @Value("${app.transaction.pageSize}")
  private int pageSize;

  public int getPageCount() {
    int userId = loggedInUserFinderService.getId();

    return transactionRepo.getPageCount(userId, pageSize);
  }

  public List<TransactionDetailsDto> getPagedTransactions(@Positive int pageNum) {
    int pageCount = getPageCount();

    if (pageNum > pageCount) {
      return new ArrayList<>();
    }

    Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

    int userId = loggedInUserFinderService.getId();

    List<Transaction> transactions = transactionRepo.findByOwner(userId, pageable);

    return transactions
            .stream()
            .map(transaction ->
                    TransactionDetailsDto
                            .builder()
                            .id(transaction.getId())
                            .details(NewTransactionDetailsDto
                                    .builder()
                                    .name(transaction.getName())
                                    .value(transaction.getValue())
                                    .notes(transaction.getNotes())
                                    .timestamp(transaction.getTimestamp())
                                    .categoryId(transaction.getCategory().getId())
                                    .build())
                            .build())
            .toList();
  }
}
