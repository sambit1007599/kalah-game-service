package com.bol.kalah.service;

import com.bol.kalah.dao.PitDao;
import com.bol.kalah.dto.GameStatus;
import com.bol.kalah.model.Game;
import com.bol.kalah.model.Pit;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bol.kalah.dto.GameStatus.FINISHED;
import static com.bol.kalah.dto.GameStatus.PLAYER1_TURN;
import static com.bol.kalah.util.KalahCommonUtil.HOUSE_PITS;
import static com.bol.kalah.util.KalahCommonUtil.PLAYER1_HOUSE;
import static com.bol.kalah.util.KalahCommonUtil.PLAYER2_HOUSE;
import static com.bol.kalah.util.KalahCommonUtil.PLAYER_1_PITS;
import static com.bol.kalah.util.KalahCommonUtil.PLAYER_2_PITS;
import static com.bol.kalah.util.KalahCommonUtil.getNumberOfStonesToPutInEachPit;
import static com.bol.kalah.util.KalahCommonUtil.getOppositePit;
import static com.bol.kalah.util.KalahCommonUtil.getSumOfStonesOfPlayerPits;

/**
 * The type Kalah rule service.
 */
@RequiredArgsConstructor
@Service
public class KalahRuleService {

    private final PitDao pitDao;

    /**
     * Check and update if last pit is empty map.
     *
     * @param boardMap     the map pit
     * @param lastStonePit the last stone pit
     * @param gameStatus   the game status
     * @return the map
     */
    public Map<Integer, Pit> checkAndUpdateIfLastPitIsEmpty(Map<Integer, Pit> boardMap,
                                                            int lastStonePit, GameStatus gameStatus) {

        if (!ArrayUtils.contains(HOUSE_PITS, lastStonePit)) {

            if (boardMap.get(lastStonePit).getNumberOfStones() == 1) {
                Pit oppPit = boardMap.get(getOppositePit(lastStonePit));
                Pit housePit = boardMap.get(gameStatus == PLAYER1_TURN ? PLAYER1_HOUSE : PLAYER2_HOUSE);

                housePit.setNumberOfStones(housePit.getNumberOfStones()
                        + oppPit.getNumberOfStones()
                        + boardMap.get(lastStonePit).getNumberOfStones());
                boardMap.put(housePit.getPitIndex(), housePit);

                oppPit.setNumberOfStones(0);
                boardMap.put(oppPit.getPitIndex(), oppPit);

                Pit lastPit = boardMap.get(lastStonePit);
                lastPit.setNumberOfStones(0);
                boardMap.put(boardMap.get(lastStonePit).getPitIndex(), lastPit);

            }
        }

        return boardMap;
    }

    /**
     * Move the stones in board.
     *
     * @param pits                   the pits
     * @param playerPitToNotPutStone the player pit to not put stone
     * @param lastStonePit           the last stone pit
     * @param playerSelectedPit      the pit index
     * @param initialStones          the initial stones
     * @return the map
     */
    public Map<Integer, Pit> moveTheStonesInBoard(List<Pit> pits, int playerPitToNotPutStone, int lastStonePit,
                                                  int playerSelectedPit, int initialStones) {

        int numberOfStonesToPutInEachPit = getNumberOfStonesToPutInEachPit(initialStones);
        Map<Integer, Pit> stonesInBoard = new HashMap<>();

        for (Pit pit : pits) {
            int currentIndex = pit.getPitIndex();
            if (currentIndex != playerPitToNotPutStone) {
                if (isPitAllowedToPutStone(lastStonePit, playerSelectedPit, currentIndex)) {
                    pit.setNumberOfStones(pit.getNumberOfStones() + 1);
                }
                pit.setNumberOfStones(pit.getNumberOfStones() + numberOfStonesToPutInEachPit);
            }
            stonesInBoard.put(currentIndex, pit);
        }

        Pit stonesPickedFromPit = stonesInBoard.get(playerSelectedPit);
        stonesPickedFromPit.setNumberOfStones(stonesPickedFromPit.getNumberOfStones() - initialStones);
        stonesInBoard.put(stonesPickedFromPit.getPitIndex(), stonesPickedFromPit);

        return stonesInBoard;
    }

    /**
     * Check and update if game is end.
     *
     * @param game the game
     * @return the game
     */
    public Game checkAndUpdateIfGameIsEnd(Game game) {

        int sumOfStonesOfPlayer1 = getSumOfStonesOfPlayerPits(game, PLAYER_1_PITS);
        int sumOfStonesOfPlayer2 = getSumOfStonesOfPlayerPits(game, PLAYER_2_PITS);

        if (sumOfStonesOfPlayer1 == 0) {
            gameEndPitAndStatusUpdate(game, PLAYER2_HOUSE, sumOfStonesOfPlayer2);
        } else if (sumOfStonesOfPlayer2 == 0) {
            gameEndPitAndStatusUpdate(game, PLAYER1_HOUSE, sumOfStonesOfPlayer1);
        }
        return game;
    }

    private void gameEndPitAndStatusUpdate(Game game, int houseIndex, int stonesToAdd) {
        Pit pit = game.getPits().get(houseIndex - 1);
        pit.setNumberOfStones(pit.getNumberOfStones() + stonesToAdd);
        game.setGameStatus(FINISHED);
        pitDao.savePit(pit);
    }

    private boolean isPitAllowedToPutStone(int lastStonePit, int playerSelectedPit, int currentIndex) {
        return currentIndex > playerSelectedPit && currentIndex <= lastStonePit
                || lastStonePit < playerSelectedPit && (currentIndex > playerSelectedPit || currentIndex <= lastStonePit)
                || lastStonePit == playerSelectedPit;
    }

}
