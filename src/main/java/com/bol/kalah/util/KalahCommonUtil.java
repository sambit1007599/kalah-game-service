package com.bol.kalah.util;

import com.bol.kalah.dto.GameStatus;
import com.bol.kalah.error.exception.InvalidPitIndexException;
import com.bol.kalah.model.Game;
import com.bol.kalah.model.Pit;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ArrayUtils;

import static com.bol.kalah.dto.GameStatus.PLAYER1_TURN;
import static com.bol.kalah.dto.GameStatus.PLAYER2_TURN;

/**
 * The type Kalah common util.
 */
@UtilityClass
public class KalahCommonUtil {

    public static final Integer PIT_START_INDEX = 1;
    public static final Integer PIT_END_INDEX = 14;
    public static final Integer PLAYER1_HOUSE = 7;
    public static final Integer PLAYER2_HOUSE = 14;
    public static final Integer INITIAL_STONE_ON_PIT = 6;
    public static final Integer INITIAL_STONE_ON_HOUSE = 0;
    public static final int[] PLAYER_1_PITS = {1, 2, 3, 4, 5, 6};
    public static final int[] PLAYER_2_PITS = {8, 9, 10, 11, 12, 13};
    public static final int[] HOUSE_PITS = {7, 14};

    /**
     * Gets last stone pit.
     *
     * @param initialStones     the initial stones
     * @param playerSelectedPit the pit index
     * @return the last stone pit
     */
    public static int getLastStonePit(int initialStones, int playerSelectedPit) {

        if (initialStones >= (KalahCommonUtil.PIT_END_INDEX - 1)) {
            int lastStonePit = initialStones % (KalahCommonUtil.PIT_END_INDEX - 1);
            return lastStonePit == 0 ? playerSelectedPit : lastStonePit + playerSelectedPit;

        } else {
            int lastStonePit = initialStones + playerSelectedPit;
            return lastStonePit > KalahCommonUtil.PIT_END_INDEX ?
                    Math.abs(lastStonePit - KalahCommonUtil.PIT_END_INDEX) :
                    lastStonePit;
        }
    }

    /**
     * Gets player pit to not put stone.
     *
     * @param gameStatus the game status
     * @return the player pit to not put stone
     */
    public static int getOppositePlayerHousePit(GameStatus gameStatus) {
        return gameStatus == GameStatus.PLAYER1_TURN
                ? KalahCommonUtil.PLAYER2_HOUSE
                : KalahCommonUtil.PLAYER1_HOUSE;
    }

    /**
     * Gets number of stones to put in each pit.
     *
     * @param initialStones the initial stones
     * @return the number of stones to put in each pit
     */
    public static int getNumberOfStonesToPutInEachPit(int initialStones) {
        if (initialStones > KalahCommonUtil.PIT_END_INDEX - 1) {
            return initialStones / (KalahCommonUtil.PIT_END_INDEX - 1);
        } else {
            return 0;
        }
    }

    /**
     * Gets player turn.
     *
     * @param pitIndex the pit index
     * @return the player turn
     * @throws InvalidPitIndexException the invalid pit index exception
     */
    public static GameStatus getPlayerTurn(int pitIndex) throws InvalidPitIndexException {

        if (ArrayUtils.contains(PLAYER_1_PITS, pitIndex)) {
            return PLAYER1_TURN;
        } else if (ArrayUtils.contains(PLAYER_2_PITS, pitIndex)) {
            return PLAYER2_TURN;
        }

        throw new InvalidPitIndexException(String.valueOf(pitIndex));
    }

    /**
     * Gets opposite pit.
     *
     * @param pitIndex the pit index
     * @return the opposite pit
     * @throws InvalidPitIndexException the invalid pit index exception
     */
    public static Integer getOppositePit(int pitIndex) throws InvalidPitIndexException {

        if (ArrayUtils.contains(PLAYER_1_PITS, pitIndex)) {
            return PIT_END_INDEX - pitIndex;
        } else if (ArrayUtils.contains(PLAYER_2_PITS, pitIndex)) {
            return Math.abs(pitIndex - PIT_END_INDEX);
        }
        throw new InvalidPitIndexException(String.valueOf(pitIndex));
    }

    /**
     * Gets sum of stones of player pits.
     *
     * @param game       the game
     * @param playerPits the player pits
     * @return the sum of stones of player pits
     */
    public static int getSumOfStonesOfPlayerPits(Game game, int[] playerPits) {
        return game.getPits().stream()
                .filter(pit -> ArrayUtils.contains(playerPits, pit.getPitIndex()))
                .mapToInt(Pit::getNumberOfStones)
                .sum();
    }

}
