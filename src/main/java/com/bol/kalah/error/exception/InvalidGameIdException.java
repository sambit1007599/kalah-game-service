package com.bol.kalah.error.exception;

import lombok.Getter;

/**
 * The type In valid game id exception.
 */
@Getter
public class InvalidGameIdException extends RuntimeException {

  /**
   * Instantiates a new In valid game id exception.
   *
   * @param message the message
   */
  public InvalidGameIdException(final String message) {
    super(message);
  }

}
