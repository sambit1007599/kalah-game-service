package com.bol.kalah.service;

import com.bol.kalah.dto.CreateGameReply;
import com.bol.kalah.dto.MakeAMoveReply;

/**
 * The interface Game service.
 */
public interface GameService {

  /**
   * Creates a game with initial 6 stones on each Pit
   *
   * @param url the url
   * @return create game reply
   */
  CreateGameReply createGame(String url);

  /**
   * Play the game based on Pit Index
   *
   * @param gameId   the game id
   * @param pitIndex the pit index
   * @param url      the url
   * @return make a move reply
   */
  MakeAMoveReply makeAMoveByPlayer(Long gameId, Integer pitIndex, String url);
}
