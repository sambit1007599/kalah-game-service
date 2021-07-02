package com.bol.kalah.error.exception;

import lombok.Getter;

/**
 * The type Wrong player exception.
 */
@Getter
public class WrongGameStatusException extends RuntimeException {

  /**
   * Instantiates a new Wrong player exception.
   *
   * @param message the message
   */
  public WrongGameStatusException(final String message) {
    super(message);
  }

}
