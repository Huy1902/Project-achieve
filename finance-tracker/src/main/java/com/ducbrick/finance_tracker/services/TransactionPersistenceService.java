package com.ducbrick.finance_tracker.services;

import com.ducbrick.finance_tracker.dtos.NewTransactionDetailsDto;
import com.ducbrick.finance_tracker.dtos.TransactionDetailsDto;
import com.ducbrick.finance_tracker.entities.Category;
import com.ducbrick.finance_tracker.entities.Transaction;
import com.ducbrick.finance_tracker.exceptions.CategoryNotFoundException;
import com.ducbrick.finance_tracker.exceptions.CategoryNotOwnedException;
import com.ducbrick.finance_tracker.repos.CategoryRepo;
import com.ducbrick.finance_tracker.repos.TransactionRepo;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.NoSuchElementException;
import java.util.Set;

@Service
@Validated
@RequiredArgsConstructor
public class TransactionPersistenceService {
  private final Validator validator;
  private final LoggedInUserFinderService loggedInUserFinderService;
  private final TransactionRepo transactionRepo;
  private final CategoryRepo categoryRepo;

  @Transactional
  public void update(@NotNull @Valid TransactionDetailsDto dto) {
    /*Should not throw*/
    Category category = categoryRepo
            .findById(dto.getDetails().getCategoryId())
            .orElseThrow(() -> new CategoryNotFoundException(
                    "Unable to find category with id provided in new transaction details"));

    /*Should not throw*/
    if (category.getOwner().getId() != loggedInUserFinderService.getId()) {
      throw new CategoryNotOwnedException("Category provided in new transaction details is not owned by the logged in user");
    }

    NewTransactionDetailsDto details = dto.getDetails();

    Transaction transaction = transactionRepo.findById(dto.getId()).orElseThrow(() -> new NoSuchElementException("Attempting to update a transaction that does not exist"));

    transaction.setName(details.getName());
    transaction.setValue(details.getValue().setScale(2, java.math.RoundingMode.HALF_UP));
    transaction.setNotes(details.getNotes());
    transaction.setTimestamp(details.getTimestamp());
    transaction.setCategory(category);

    Set<ConstraintViolation<Transaction>> violations = validator.validate(transaction);

    /*Should not throw*/
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }

    transactionRepo.save(transaction);
  }

  @Transactional
  public void add(@NotNull @Valid NewTransactionDetailsDto dto) {
    /*Should not throw*/
    Category category = categoryRepo
            .findById(dto.getCategoryId())
            .orElseThrow(() -> new CategoryNotFoundException(
                    "Unable to find category with id provided in new transaction details"));

    /*Should not throw*/
    if (category.getOwner().getId() != loggedInUserFinderService.getId()) {
      throw new CategoryNotOwnedException("Category provided in new transaction details is not owned by the logged in user");
    }

    Transaction transaction = Transaction
            .builder()
            .name(dto.getName())
            .value(dto.getValue().setScale(2, java.math.RoundingMode.HALF_UP))
            .notes(dto.getNotes())
            .timestamp(dto.getTimestamp())
            .category(category)
            .build();

    Set<ConstraintViolation<Transaction>> violations = validator.validate(transaction);

    /*Should not throw*/
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }

    transactionRepo.save(transaction);
  }

  @Transactional
  public void delete(int id) {
    transactionRepo.deleteById(id);
  }
}
