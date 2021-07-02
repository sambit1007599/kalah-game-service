package com.bol.kalah.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lombok.extern.slf4j.Slf4j;

import com.bol.kalah.common.KalahCommonUtil;
import com.bol.kalah.dto.GameStatus;
import com.bol.kalah.dto.MakeAMoveReply;
import com.bol.kalah.model.Game;
import com.bol.kalah.model.Pit;
import com.bol.kalah.repository.GameRepository;
import com.bol.kalah.service.impl.GameServiceImpl;

@Slf4j
@ExtendWith(SpringExtension.class)
public class GameServiceTest {

  @Mock
  private GameRepository gameRepo;

  @Mock
  private KalahRuleService kalahRuleService;

  private GameServiceImpl gameService;

  @BeforeEach
  public void setup() {
    gameService = new GameServiceImpl(gameRepo, kalahRuleService);
  }

  @Test
  public void testEmptyPitOnLastStone() {
    when(gameRepo.findById(1L)).thenReturn(getGame());

    when(kalahRuleService.getPlayerTurn(anyInt())).thenReturn(GameStatus.PLAYER1_TURN);
    when(kalahRuleService.getPitByPitIndexAndGameId(anyLong(), anyInt())).thenReturn(getPit());
    when(kalahRuleService.moveTheStonesInBoard(any(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt()))
        .thenReturn(getMapOfPits());

    when(kalahRuleService.checkAndUpdateIfLastPitIsEmpty(any(), anyInt(), any(GameStatus.class)))
        .thenReturn(getResultMapOfPits());

    Game game = getGame().get();
    game.setPits(getPitListAfterLastPitIsEmpty());

    when(gameRepo.save(any(Game.class))).thenReturn(game);

    when(kalahRuleService.checkGameEnded(any(Game.class))).thenReturn(game);

    MakeAMoveReply reply = gameService.makeAMoveByPlayer(1L, 2, "");

    assertTrue(reply.getStatus().get(7) == 7);

  }

  private java.util.Optional<Game> getGame() {

    Game game = new Game(getPitList().stream().toArray(Pit[]::new));
    game.setGameStatus(GameStatus.PLAYER1_TURN);

    return Optional.of(game);
  }

  private List<Pit> getPitList() {
    List<Pit> pits = new ArrayList<Pit>();
    IntStream.range(KalahCommonUtil.PIT_START_INDEX, KalahCommonUtil.PIT_END_INDEX + 1).sequential()
        .forEach(index -> {

          Pit pit = new Pit();

          if (index == KalahCommonUtil.PLAYER1_HOUSE) {
            pit.setNumberOfStones(1);
          } else if (index == KalahCommonUtil.PLAYER2_HOUSE) {
            pit.setNumberOfStones(1);
          } else {
            pit.setNumberOfStones(KalahCommonUtil.INITIAL_STONE_ON_PIT);
          }
          if (index == 3) {
            pit.setNumberOfStones(0);
          }
          if (index == 2) {
            pit.setNumberOfStones(1);
          }
          pit.setPitIndex(index);
          pits.add(pit);
        });

    return pits;
  }

  private Optional<Pit> getPit() {

    Pit pit = new Pit();
    pit.setNumberOfStones(1);
    pit.setPitIndex(2);
    return Optional.of(pit);
  }

  private Map<Integer, Pit> getMapOfPits() {

    return getPitList().stream().collect(Collectors.toMap(Pit::getPitIndex, pit -> pit));
  }

  private Map<Integer, Pit> getResultMapOfPits() {

    return getPitListAfterLastPitIsEmpty().stream().collect(Collectors.toMap(Pit::getPitIndex, pit -> pit));
  }

  private List<Pit> getPitListAfterLastPitIsEmpty() {
    List<Pit> pits = new ArrayList<Pit>();
    IntStream.range(KalahCommonUtil.PIT_START_INDEX, KalahCommonUtil.PIT_END_INDEX + 1).sequential()
        .forEach(index -> {

          Pit pit = new Pit();

          if (index == KalahCommonUtil.PLAYER1_HOUSE) {
            pit.setNumberOfStones(7);
          } else if (index == KalahCommonUtil.PLAYER2_HOUSE) {
            pit.setNumberOfStones(1);
          } else {
            pit.setNumberOfStones(KalahCommonUtil.INITIAL_STONE_ON_PIT);
          }
          if (index == 3) {
            pit.setNumberOfStones(0);
          }
          if (index == 2) {
            pit.setNumberOfStones(0);
          }
          pit.setPitIndex(index);
          pits.add(pit);
        });

    return pits;
  }

}
