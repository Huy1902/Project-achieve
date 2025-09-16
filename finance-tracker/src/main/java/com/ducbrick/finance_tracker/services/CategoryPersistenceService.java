package com.ducbrick.finance_tracker.services;

import com.ducbrick.finance_tracker.dtos.CategoryDetailsDto;
import com.ducbrick.finance_tracker.dtos.NewCategoryDetailsDto;
import com.ducbrick.finance_tracker.entities.Category;
import com.ducbrick.finance_tracker.entities.User;
import com.ducbrick.finance_tracker.exceptions.CategoryNotOwnedException;
import com.ducbrick.finance_tracker.exceptions.TransactionDependencyException;
import com.ducbrick.finance_tracker.repos.CategoryRepo;
import com.ducbrick.finance_tracker.repos.TransactionRepo;
import com.ducbrick.finance_tracker.repos.UserRepo;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.NoSuchElementException;
import java.util.Set;

@Service
@Validated
@RequiredArgsConstructor
public class CategoryPersistenceService {
  private final Logger logger = LoggerFactory.getLogger(CategoryPersistenceService.class);
  private final Validator validator;
  private final CategoryRepo categoryRepo;
  private final TransactionRepo transactionRepo;
  private final UserRepo userRepo;
  private final LoggedInUserFinderService loggedInUserFinderService;

  @Transactional
  public void add(@NotNull @Valid NewCategoryDetailsDto dto) {
    User loggedInUser = userRepo.findById(loggedInUserFinderService.getId()).orElseThrow();

    Category category = Category.builder()
            .name(dto.getName())
            .color(dto.getColor())
            .owner(loggedInUser)
            .build();

    Set<ConstraintViolation<Category>> violations = validator.validate(category);

    if (!violations.isEmpty()) {
      logger.error("Unexpected constraint violations: {violations}");
      throw new RuntimeException();
    }

    categoryRepo.save(category);
  }

  @Transactional
  public void update(@NotNull @Valid CategoryDetailsDto dto) {
    Category category = categoryRepo.findById(dto.getId())
            .orElseThrow(() -> new NoSuchElementException("Attempting to update a category that does not exist"));

    if (category.getOwner().getId() != loggedInUserFinderService.getId()) {
      throw new CategoryNotOwnedException("Category with provided id is not owned by the logged in user");
    }

    category.setName(dto.getName());
    category.setColor(dto.getColor());

    Set<ConstraintViolation<Category>> violations = validator.validate(category);

    /*Should not throw*/
    if (!violations.isEmpty()) {
      logger.error("Unexpected constraint violations: {violations}");
      throw new RuntimeException();
    }

    categoryRepo.save(category);
  }

  @Transactional
  public void delete(int id) throws TransactionDependencyException {
    Category category = categoryRepo.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Attempting to delete a category that does not exist"));

    if (category.getOwner().getId() != loggedInUserFinderService.getId()) {
      throw new CategoryNotOwnedException("Category with provided id is not owned by the logged in user");
    }

    if (transactionRepo.countByCategory(id) > 0) {
      throw new TransactionDependencyException("Attempting to delete a category with existing transactions");
    }

    categoryRepo.deleteById(id);
  }
}
