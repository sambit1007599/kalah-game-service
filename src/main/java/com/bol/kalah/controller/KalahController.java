package com.bol.kalah.controller;

import com.bol.kalah.dto.CreateGameReply;
import com.bol.kalah.dto.MakeAMoveReply;
import com.bol.kalah.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

/**
 * The type Kalah controller.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/games")
public class KalahController {

    /**
     * The Game service.
     */
    private final GameService gameService;

    /**
     * Create a game.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<CreateGameReply> createGame(HttpServletRequest request) {
        return new ResponseEntity<>(gameService.createGame(request.getRequestURL().toString()),
                HttpStatus.CREATED);
    }

    /**
     * Play game response entity.
     *
     * @param gameId            the game id
     * @param playerSelectedPit the pit index
     * @param request           the request
     * @return the response entity
     */
    @PutMapping("/{game-id}/pits/{pit-index}")
    public ResponseEntity<MakeAMoveReply> playGame(
            @NotNull @PathVariable("game-id") Long gameId,
            @NotNull @PathVariable("pit-index") Integer playerSelectedPit,
            HttpServletRequest request) {

        return new ResponseEntity<>(gameService
                .makeAMoveByPlayer(gameId, playerSelectedPit, request.getRequestURL().toString()
                        .replace(request.getRequestURI(), "")), HttpStatus.OK);

    }

}
