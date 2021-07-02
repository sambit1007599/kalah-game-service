package com.bol.kalah.error;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.bol.kalah.config.ErrorConstants;
import com.bol.kalah.dto.Error;
import com.bol.kalah.dto.ErrorAttributes;
import com.bol.kalah.error.exception.EmptyPitException;
import com.bol.kalah.error.exception.GameOverException;
import com.bol.kalah.error.exception.InvalidGameIdException;
import com.bol.kalah.error.exception.InvalidPitIndexException;
import com.bol.kalah.error.exception.WrongGameStatusException;

/**
 * The type Rest response entity exception handler.
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * The Error constants.
   */
  private final ErrorConstants errorConstants;

  /**
   * Handle in valid game id exception response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(InvalidGameIdException.class)
  public ResponseEntity<Error> handleInValidGameIdException(InvalidGameIdException ex) {

    Error error = getErrorResponseEntity(errorConstants.getInvalidGameIdException(), HttpStatus.BAD_REQUEST, ex);

    log.error("{} :: {}", errorConstants.getInvalidGameIdException().getCode(),
        errorConstants.getInvalidGameIdException().getMessage());

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handle empty pit exception response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(EmptyPitException.class)
  public ResponseEntity<Error> handleEmptyPitException(EmptyPitException ex) {

    Error error = getErrorResponseEntity(errorConstants.getEmptyPitException(), HttpStatus.BAD_REQUEST, ex);

    log.error("{} :: {}", errorConstants.getEmptyPitException().getCode(),
        errorConstants.getEmptyPitException().getMessage());
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handle invalid pit index exception response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(InvalidPitIndexException.class)
  public ResponseEntity<Error> handleInvalidPitIndexException(InvalidPitIndexException ex) {

    Error error = getErrorResponseEntity(errorConstants.getInvalidPitIndexException(), HttpStatus.BAD_REQUEST, ex);

    log.error("{} :: {}", errorConstants.getInvalidPitIndexException().getCode(),
        errorConstants.getInvalidPitIndexException().getMessage());
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handle wrong player exception response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(WrongGameStatusException.class)
  public ResponseEntity<Error> handleWrongPlayerException(WrongGameStatusException ex) {

    String message = errorConstants.getWrongGameStatusException().getMessage() + ex.getMessage();
    errorConstants.getWrongGameStatusException().setMessage(message);

    Error error = getErrorResponseEntity(errorConstants.getWrongGameStatusException(), HttpStatus.BAD_REQUEST, ex);

    log.error("{} :: {}", errorConstants.getWrongGameStatusException().getCode(), message);
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handle game over exception response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(GameOverException.class)
  public ResponseEntity<Error> handleGameOverException(GameOverException ex) {

    Error error = getErrorResponseEntity(errorConstants.getGameOverException(), HttpStatus.BAD_REQUEST, ex);

    log.error("{} :: {}", errorConstants.getGameOverException().getCode(),
        errorConstants.getGameOverException().getMessage());
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handle not found response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<Error> handleNotFound(final Exception ex) {

    ErrorAttributes errorAttributes = ErrorAttributes.builder().code("RESOURCE_NOT_FOUND").message("Resource not found")
        .status(HttpStatus.NOT_FOUND.value()).params(List.of(ex.getMessage())).build();
    Error error = new Error();
    error.setErrors(List.of(errorAttributes));
    log.error("{} :: {}", errorAttributes.getCode(), errorAttributes.getMessage());
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  /**
   * Gets error response entity.
   *
   * @param errorAttributes the error attributes
   * @param httpStatus      the http status
   * @param ex              the ex
   * @return the error response entity
   */
  private Error getErrorResponseEntity(ErrorAttributes errorAttributes, HttpStatus httpStatus, Exception ex) {

    Error error = new Error();
    error.setErrors(
        List.of(ErrorAttributes.builder().code(errorAttributes.getCode()).message(errorAttributes.getMessage())
            .status(httpStatus.value()).params(List.of(ex.getMessage())).build()));

    return error;
  }

}
