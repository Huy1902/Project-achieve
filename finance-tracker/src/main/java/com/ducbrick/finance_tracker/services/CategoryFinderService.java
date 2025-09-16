package com.ducbrick.finance_tracker.services;

import com.ducbrick.finance_tracker.dtos.CategoryDetailsDto;
import com.ducbrick.finance_tracker.exceptions.NoLoggedInUserException;
import com.ducbrick.finance_tracker.repos.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
//@RequiredArgsConstructor
public class CategoryFinderService {
  private final LoggedInUserFinderService loggedInUserFinderService;
  private final CategoryRepo categoryRepo;

  public CategoryFinderService(LoggedInUserFinderService loggedInUserFinderService, CategoryRepo categoryRepo) {
    this.loggedInUserFinderService = loggedInUserFinderService;
    this.categoryRepo = categoryRepo;
  }

  /**
   * Retrieves all categories for the currently logged-in user in a list
   *
   * @return an immutable {@link List} of {@link CategoryDetailsDto}
   */
  public List<CategoryDetailsDto> findAll() {
    int loggedInUserId = loggedInUserFinderService.getId();

    return categoryRepo
            .findByOwnerId(loggedInUserId)
            .stream()
            .map(category -> CategoryDetailsDto
                      .builder()
                      .id(category.getId())
                      .name(category.getName())
                      .color(category.getColor())
                      .build())
            .toList();
  }
}
