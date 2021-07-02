package com.bol.kalah.error.exception;

import lombok.Getter;

/**
 * The type Game over exception.
 */
@Getter
public class GameOverException extends RuntimeException {

  /**
   * Instantiates a new Game over exception.
   *
   * @param message the message
   */
  public GameOverException(final String message) {
    super(message);
  }

}
