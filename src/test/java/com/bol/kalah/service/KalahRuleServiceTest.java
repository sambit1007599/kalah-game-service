package com.bol.kalah.service;

import com.bol.kalah.dao.PitDao;
import com.bol.kalah.dto.GameStatus;
import com.bol.kalah.error.exception.InvalidPitIndexException;
import com.bol.kalah.model.Game;
import com.bol.kalah.model.Pit;
import com.bol.kalah.util.KalahCommonUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.bol.kalah.util.KalahCommonUtil.getOppositePit;
import static com.bol.kalah.util.KalahCommonUtil.getPlayerTurn;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
public class KalahRuleServiceTest {

    @Mock
    private PitDao pitDao;

    private KalahRuleService kalahRuleService;

    @BeforeEach
    private void setUp() {
        kalahRuleService = new KalahRuleService(pitDao);
    }

    @Test
    public void testGetPlayerTurn() {
        GameStatus player1Status = getPlayerTurn(2);
        assertTrue(GameStatus.PLAYER1_TURN == player1Status);

        GameStatus player2Status = getPlayerTurn(8);
        assertTrue(GameStatus.PLAYER2_TURN == player2Status);

    }

    @Test
    public void testGetPlayerTurnException() {
        assertThrows(InvalidPitIndexException.class, () -> getPlayerTurn(7));

    }

    @Test
    public void testGetOppositePit() {
        assertTrue(getOppositePit(2) == 12);
    }

    @Test
    public void testGetOppositePitException() {
        assertThrows(InvalidPitIndexException.class, () -> getOppositePit(7));
    }

    @Test
    public void testIfLastPitWasEmptyAndUpdate() {
        Map<Integer, Pit> map = kalahRuleService.checkAndUpdateIfLastPitIsEmpty(getMapOfPits(), 3,
                GameStatus.PLAYER1_TURN);
        System.out.println(map.get(7).getNumberOfStones());
        assertEquals(8, map.get(7).getNumberOfStones());
        assertEquals(0, map.get(3).getNumberOfStones());
        assertEquals(0, map.get(11).getNumberOfStones());
    }

    @Test
    public void testMoveStonesInBoard() {

        Map<Integer, Pit> map = kalahRuleService.moveTheStonesInBoard(getPitList(), 14, 10, 4, 0, 6);
        assertTrue(map.get(4).getNumberOfStones() == 0);
        assertTrue(map.get(10).getNumberOfStones() == 7);

    }

    @Test
    public void testIfGameIsEnded() {
        List<Pit> pitList = getPitList().stream().map(pit -> {
            if (ArrayUtils.contains(KalahCommonUtil.PLAYER_1_PITS, pit.getPitIndex())) {
                pit.setNumberOfStones(0);
            }
            return pit;
        }).collect(Collectors.toList());

        Game game = new Game();
        game.setPits(pitList);

        Game gameResult = kalahRuleService.checkAndUpdateIfGameIsEnd(game);
        assertThat(gameResult.getGameStatus()).isEqualTo(GameStatus.FINISHED);

    }

    private List<Pit> getPitList() {
        List<Pit> pits = new ArrayList<>();
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
                        pit.setNumberOfStones(1);
                    }
                    if (index == 2) {
                        pit.setNumberOfStones(1);
                    }
                    pit.setPitIndex(index);
                    pits.add(pit);
                });

        return pits;
    }

    private Map<Integer, Pit> getMapOfPits() {

        return getPitList().stream().collect(Collectors.toMap(Pit::getPitIndex, pit -> pit));
    }
}
