package com.bol.kalah.error.exception;

import lombok.Getter;

/**
 * The type Invalid pit index exception.
 */
@Getter
public class InvalidPitIndexException extends RuntimeException {

  /**
   * Instantiates a new Invalid pit index exception.
   *
   * @param message the message
   */
  public InvalidPitIndexException(final String message) {
    super(message);
  }

}
