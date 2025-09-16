package com.ducbrick.finance_tracker.exceptions;

public class NoLoggedInUserException extends RuntimeException {
  public NoLoggedInUserException(String message) {
    super(message);
  }
}
