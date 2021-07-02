package com.bol.kalah.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lombok.extern.slf4j.Slf4j;

import com.bol.kalah.dto.CreateGameReply;
import com.bol.kalah.dto.MakeAMoveReply;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class GameServiceIntegrationTest {

  @Autowired
  private GameService gameService;

  @Test
  public void testCreateGame() {
    CreateGameReply reply = gameService.createGame("");

    assertTrue(reply.getGameId() != null);
  }

  @Test
  public void testPlayer1Move() {
    CreateGameReply replyGame = gameService.createGame("");

    MakeAMoveReply reply = gameService.makeAMoveByPlayer(replyGame.getGameId(), 2, "");

    assertTrue(reply.getStatus().containsKey(2));
    assertTrue(reply.getStatus().get(2) == 0);
    assertTrue(reply.getStatus().get(7) == 1);
    assertTrue(reply.getStatus().get(8) == 7);

  }

  @Test
  public void testPlayer2Move() {
    CreateGameReply replyGame = gameService.createGame("");

    MakeAMoveReply reply = gameService.makeAMoveByPlayer(replyGame.getGameId(), 10, "");

    assertTrue(reply.getStatus().containsKey(10));
    assertTrue(reply.getStatus().get(10) == 0);
    assertTrue(reply.getStatus().get(14) == 1);
    assertTrue(reply.getStatus().get(2) == 7);

  }

}
