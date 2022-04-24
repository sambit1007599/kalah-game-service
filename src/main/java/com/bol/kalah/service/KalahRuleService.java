package com.bol.kalah.service;

import com.bol.kalah.common.KalahCommonUtil;
import com.bol.kalah.dto.GameStatus;
import com.bol.kalah.error.exception.InvalidPitIndexException;
import com.bol.kalah.error.exception.WrongGameStatusException;
import com.bol.kalah.model.Game;
import com.bol.kalah.model.Pit;
import com.bol.kalah.repository.PitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The type Kalah rule service.
 */
@RequiredArgsConstructor
@Service
public class KalahRuleService {

    /**
     * The Pit repo.
     */
    private final PitRepository pitRepo;

    /**
     * Gets sum of stones of player 1.
     *
     * @param gameId the game id
     * @return the sum of stones of player 1
     */
    public Integer getSumOfStonesOfPlayer1(Long gameId) {
        return pitRepo.getSumOfStonesInPlayerPit(gameId, Arrays.asList(KalahCommonUtil.PLAYER_1_PITS));
    }

    /**
     * Gets sum of stones of player 2.
     *
     * @param gameId the game id
     * @return the sum of stones of player 2
     */
    public Integer getSumOfStonesOfPlayer2(Long gameId) {
        return pitRepo.getSumOfStonesInPlayerPit(gameId, Arrays.asList(KalahCommonUtil.PLAYER_2_PITS));
    }

    /**
     * Gets pit by pit index and game id.
     *
     * @param gameId   the game id
     * @param pitIndex the pit index
     * @return the pit by pit index and game id
     */
    public Optional<Pit> getPitByPitIndexAndGameId(Long gameId, int pitIndex) {
        return pitRepo.getPitByGameIdAndPitIndex(gameId, pitIndex);
    }

    /**
     * Gets opposite pit.
     *
     * @param pitIndex the pit index
     * @return the opposite pit
     * @throws InvalidPitIndexException the invalid pit index exception
     */
    public Integer getOppositePit(int pitIndex) throws InvalidPitIndexException {

        if (pitIndex >= KalahCommonUtil.PLAYER1_PIT_START && pitIndex < KalahCommonUtil.PLAYER1_PIT_END) {
            return KalahCommonUtil.PIT_END_INDEX - pitIndex;
        } else if (pitIndex >= KalahCommonUtil.PLAYER2_PIT_START && pitIndex < KalahCommonUtil.PLAYER2_PIT_END) {
            return Math.abs(pitIndex - KalahCommonUtil.PIT_END_INDEX);
        }

        throw new InvalidPitIndexException(String.valueOf(pitIndex));
    }

    /**
     * Gets player turn.
     *
     * @param pitIndex the pit index
     * @return the player turn
     * @throws InvalidPitIndexException the invalid pit index exception
     */
    public GameStatus getPlayerTurn(int pitIndex) throws InvalidPitIndexException {

        if (pitIndex >= KalahCommonUtil.PLAYER1_PIT_START && pitIndex < KalahCommonUtil.PLAYER1_PIT_END) {
            return GameStatus.PLAYER1_TURN;
        } else if (pitIndex >= KalahCommonUtil.PLAYER2_PIT_START && pitIndex < KalahCommonUtil.PLAYER2_PIT_END) {
            return GameStatus.PLAYER2_TURN;
        }

        throw new InvalidPitIndexException(String.valueOf(pitIndex));
    }

    /**
     * Gets next turn of player.
     *
     * @param currentGameStatus the current game status
     * @param lastStonePit      the last stone pit
     * @return the next turn of player
     * @throws WrongGameStatusException the wrong game status exception
     */
    public GameStatus getNextTurnOfPlayer(GameStatus currentGameStatus, int lastStonePit)
            throws WrongGameStatusException {

        if (currentGameStatus == GameStatus.PLAYER1_TURN) {
            return lastStonePit == KalahCommonUtil.PLAYER1_HOUSE ? GameStatus.PLAYER1_TURN : GameStatus.PLAYER2_TURN;
        } else if (currentGameStatus == GameStatus.PLAYER2_TURN) {
            return lastStonePit == KalahCommonUtil.PLAYER2_HOUSE ? GameStatus.PLAYER2_TURN : GameStatus.PLAYER1_TURN;
        }

        throw new WrongGameStatusException(currentGameStatus.toString());
    }

    /**
     * Check and update if last pit is empty map.
     *
     * @param mapPit       the map pit
     * @param lastStonePit the last stone pit
     * @param gameStatus   the game status
     * @return the map
     */
    public Map<Integer, Pit> checkAndUpdateIfLastPitIsEmpty(Map<Integer, Pit> mapPit, int lastStonePit,
                                                            GameStatus gameStatus) {

        if (!Arrays.asList(KalahCommonUtil.HOUSE_PITS).contains(lastStonePit)) {

            if (mapPit.get(lastStonePit).getNumberOfStones() == 1) {
                Pit oppPitStones = mapPit.get(getOppositePit(lastStonePit));
                Pit housePit = mapPit.get(gameStatus == GameStatus.PLAYER1_TURN ? KalahCommonUtil.PLAYER1_HOUSE
                        : KalahCommonUtil.PLAYER2_HOUSE);

                //capture player stone and opp pit stone
                housePit.setNumberOfStones(
                        housePit.getNumberOfStones() + oppPitStones.getNumberOfStones() + mapPit.get(lastStonePit)
                                .getNumberOfStones());
                mapPit.put(housePit.getPitIndex(), housePit);

                //update opp pit
                oppPitStones.setNumberOfStones(0);
                mapPit.put(oppPitStones.getPitIndex(), oppPitStones);

                //update the last pit
                Pit lastPit = mapPit.get(lastStonePit);
                lastPit.setNumberOfStones(0);
                mapPit.put(mapPit.get(lastStonePit).getPitIndex(), lastPit);

            }
        }

        return mapPit;
    }

    /**
     * Move the stones in board map.
     *
     * @param pits                         the pits
     * @param playerPitToNotPutStone       the player pit to not put stone
     * @param lastStonePit                 the last stone pit
     * @param pitIndex                     the pit index
     * @param numberOfStonesToPutInEachPit the number of stones to put in each pit
     * @param initialStones                the initial stones
     * @return the map
     */
    public Map<Integer, Pit> moveTheStonesInBoard(List<Pit> pits, int playerPitToNotPutStone, int lastStonePit,
                                                  int pitIndex, int numberOfStonesToPutInEachPit, int initialStones) {

        Map<Integer, Pit> mapPit = new HashMap<>();

        for (Pit pit : pits) {

            int currentIndex = pit.getPitIndex();
            int currentNumberOfStones = pit.getNumberOfStones();

            if (currentIndex != playerPitToNotPutStone) {
                if (currentIndex > pitIndex && currentIndex <= lastStonePit) {
                    pit.setNumberOfStones(currentNumberOfStones + numberOfStonesToPutInEachPit + 1);
                } else if (lastStonePit < pitIndex && (currentIndex > pitIndex || currentIndex <= lastStonePit)) {
                    pit.setNumberOfStones(currentNumberOfStones + numberOfStonesToPutInEachPit + 1);
                } else if (lastStonePit == pitIndex) {
                    pit.setNumberOfStones(currentNumberOfStones + 1);
                }
                pit.setNumberOfStones(pit.getNumberOfStones() + numberOfStonesToPutInEachPit);
            }

            mapPit.put(currentIndex, pit);
        }

        //lastly update the selected Pit
        Pit selectedPitFromMap = mapPit.get(pitIndex);
        selectedPitFromMap.setNumberOfStones(selectedPitFromMap.getNumberOfStones() - initialStones);
        mapPit.put(selectedPitFromMap.getPitIndex(), selectedPitFromMap);

        return mapPit;
    }

    /**
     * Check game ended game.
     *
     * @param game the game
     * @return the game
     */
    public Game checkGameEnded(Game game) {

        if (getSumOfStonesOfPlayer1(game.getId()) == 0) {
            int sum = getSumOfStonesOfPlayer2(game.getId());
            Pit pit = pitRepo.getPitByGameIdAndPitIndex(game.getId(), KalahCommonUtil.PLAYER2_HOUSE).orElseThrow(
                    () -> new EntityNotFoundException("Pit Entity not found :" + KalahCommonUtil.PLAYER2_HOUSE));
            pit.setNumberOfStones(pit.getNumberOfStones() + sum);
            game.setGameStatus(GameStatus.FINISHED);
            pitRepo.save(pit);
        } else if (getSumOfStonesOfPlayer2(game.getId()) == 0) {
            int sum = getSumOfStonesOfPlayer1(game.getId());
            Pit pit = pitRepo.getPitByGameIdAndPitIndex(game.getId(), KalahCommonUtil.PLAYER1_HOUSE).orElseThrow(
                    () -> new EntityNotFoundException("Pit Entity not found :" + KalahCommonUtil.PLAYER1_HOUSE));
            pit.setNumberOfStones(pit.getNumberOfStones() + sum);
            game.setGameStatus(GameStatus.FINISHED);
            pitRepo.save(pit);
        }

        return game;
    }
}
