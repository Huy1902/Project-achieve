package com.ducbrick.finance_tracker.exceptions;

public class TransactionDependencyException extends Exception {
  public TransactionDependencyException(String message) {
    super(message);
  }
}
