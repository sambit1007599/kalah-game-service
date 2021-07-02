package com.bol.kalah.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.bol.kalah.dto.CreateGameReply;
import com.bol.kalah.dto.MakeAMoveReply;
import com.bol.kalah.service.GameService;

/**
 * The type Kalah controller.
 */
@RestController
@RequiredArgsConstructor
public class KalahController {

  /**
   * The Game service.
   */
  private final GameService gameService;

  /**
   * Create game response entity.
   *
   * @param request the request
   * @return the response entity
   */
  @PostMapping("/games")
  public ResponseEntity<CreateGameReply> createGame(HttpServletRequest request) {
    return new ResponseEntity<>(gameService.createGame(request.getRequestURL().toString()),
        HttpStatus.CREATED);
  }

  /**
   * Play game response entity.
   *
   * @param gameId   the game id
   * @param pitIndex the pit index
   * @param request  the request
   * @return the response entity
   */
  @PutMapping("/games/{gameId}/pits/{pitIndex}")
  public ResponseEntity<MakeAMoveReply> playGame(@PathVariable Long gameId, @PathVariable Integer pitIndex,
      HttpServletRequest request) {

    return new ResponseEntity<>(gameService
        .makeAMoveByPlayer(gameId, pitIndex, request.getRequestURL().toString().replace(request.getRequestURI(), "")),
        HttpStatus.OK);

  }

}
