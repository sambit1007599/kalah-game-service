package com.bol.kalah.error.exception;

import lombok.Getter;

/**
 * The type Empty pit exception.
 */
@Getter
public class EmptyPitException extends RuntimeException {

  /**
   * Instantiates a new Empty pit exception.
   *
   * @param message the message
   */
  public EmptyPitException(final String message) {
    super(message);
  }

}
