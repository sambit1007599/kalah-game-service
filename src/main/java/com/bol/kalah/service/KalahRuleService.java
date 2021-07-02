package com.bol.kalah.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.bol.kalah.dto.GameStatus;
import com.bol.kalah.error.exception.InvalidPitIndexException;
import com.bol.kalah.error.exception.WrongGameStatusException;
import com.bol.kalah.model.Game;
import com.bol.kalah.model.Pit;

/**
 * The interface Kalah rule service.
 */
public interface KalahRuleService {

  /**
   * Sum of Stones in Player 1 Pits
   *
   * @param gameId the game id
   * @return sum of stones of player 1
   */
  Integer getSumOfStonesOfPlayer1(Long gameId);

  /**
   * Sum of Stones in Player 2 Pits
   *
   * @param gameId the game id
   * @return sum of stones of player 2
   */
  Integer getSumOfStonesOfPlayer2(Long gameId);

  /**
   * Get the Pit Entity
   *
   * @param gameId   the game id
   * @param pitIndex the pit index
   * @return pit by pit index and game id
   */
  Optional<Pit> getPitByPitIndexAndGameId(Long gameId, int pitIndex);

  /**
   * Find the  turn for Player
   *
   * @param pitIndex the pit index
   * @return player turn
   * @throws InvalidPitIndexException the invalid pit index exception
   */
  GameStatus getPlayerTurn(int pitIndex) throws InvalidPitIndexException;

  /**
   * Get the opposite pit in Board
   *
   * @param pitIndex the pit index
   * @return opposite pit
   * @throws InvalidPitIndexException the invalid pit index exception
   */
  Integer getOppositePit(int pitIndex) throws InvalidPitIndexException;

  /**
   * find which player will be next
   *
   * @param currentGameStatus the current game status
   * @param lastStonePit      the last stone pit
   * @return next turn of player
   * @throws WrongGameStatusException the wrong game status exception
   */
  GameStatus getNextTurnOfPlayer(GameStatus currentGameStatus, int lastStonePit)
      throws WrongGameStatusException;

  /**
   * method to find with last pit was empty already
   *
   * @param mapPit       the map pit
   * @param lastStonePit the last stone pit
   * @param gameStatus   the game status
   * @return map map
   */
  Map<Integer, Pit> checkAndUpdateIfLastPitIsEmpty(Map<Integer, Pit> mapPit, int lastStonePit,
      GameStatus gameStatus);

  /**
   * put the stones on the corresponding pits
   *
   * @param pits                         ,the pits in game
   * @param playerPitToNotPutStone       the player pit to not put stone
   * @param lastStonePit                 the last stone pit
   * @param pitIndex                     , the pit selected
   * @param numberOfStonesToPutInEachPit the number of stones to put in each pit
   * @param initialStones                the initial stones
   * @return map map
   */
  Map<Integer, Pit> moveTheStonesInBoard(List<Pit> pits, int playerPitToNotPutStone, int lastStonePit,
      int pitIndex, int numberOfStonesToPutInEachPit, int initialStones);

  /**
   * check and find with the game is over
   *
   * @param game the game
   * @return game game
   */
  Game checkGameEnded(Game game);

}
