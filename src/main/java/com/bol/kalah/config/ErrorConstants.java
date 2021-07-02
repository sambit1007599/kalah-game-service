package com.bol.kalah.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

import com.bol.kalah.dto.ErrorAttributes;

/**
 * The type Error constants.
 */
@Component
@ConfigurationProperties("constants.error")
@Getter
@Setter
public class ErrorConstants {

  private ErrorAttributes invalidGameIdException;
  private ErrorAttributes emptyPitException;
  private ErrorAttributes invalidPitIndexException;
  private ErrorAttributes wrongGameStatusException;
  private ErrorAttributes gameOverException;
}
