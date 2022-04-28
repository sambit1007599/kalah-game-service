package com.bol.kalah.service;

import com.bol.kalah.dao.GameDao;
import com.bol.kalah.dao.PitDao;
import com.bol.kalah.dto.GameStatus;
import com.bol.kalah.dto.MakeAMoveReply;
import com.bol.kalah.model.Game;
import com.bol.kalah.model.Pit;
import com.bol.kalah.util.KalahCommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(SpringExtension.class)
public class GameServiceTest {

    @Mock
    private GameDao gameDao;

    @Mock
    private PitDao pitDao;

    @Mock
    private KalahRuleService kalahRuleService;

    @Mock
    private GameValidationService gameValidationService;

    private GameService gameService;

    @BeforeEach
    public void setup() {
        gameService = new GameService(gameDao, kalahRuleService, gameValidationService);
    }

    @Test
    public void testEmptyPitOnLastStone() {
        when(gameDao.retrieveGame(1L)).thenReturn(getGame());

        when(pitDao.retrievePitDetails(anyLong(), anyInt())).thenReturn(getPit());
        when(kalahRuleService.moveTheStonesInBoard(any(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt()))
                .thenReturn(getMapOfPits());

        when(kalahRuleService.checkAndUpdateIfLastPitIsEmpty(any(), anyInt(), any(GameStatus.class)))
                .thenReturn(getResultMapOfPits());

        Game game = getGame();
        game.setPits(getPitListAfterLastPitIsEmpty());

        when(gameDao.saveGame(any(Game.class))).thenReturn(game);

        when(kalahRuleService.checkAndUpdateIfGameIsEnd(any(Game.class))).thenReturn(game);

        MakeAMoveReply reply = gameService.makeAMoveByPlayer(1L, 2, "");

        assertTrue(reply.getStatus().get(7) == 7);

    }

    private Game getGame() {

        Game game = new Game(getPitList().stream().toArray(Pit[]::new));
        game.setGameStatus(GameStatus.PLAYER1_TURN);

        return game;
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
