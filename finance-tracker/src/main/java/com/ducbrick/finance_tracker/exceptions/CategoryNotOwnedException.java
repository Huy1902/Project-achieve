package com.ducbrick.finance_tracker.exceptions;

public class CategoryNotOwnedException extends RuntimeException {
  public CategoryNotOwnedException(String message) {
    super(message);
  }
}
