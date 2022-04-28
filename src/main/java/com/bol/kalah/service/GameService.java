package com.bol.kalah.service;

import com.bol.kalah.dao.GameDao;
import com.bol.kalah.dto.CreateGameReply;
import com.bol.kalah.dto.GameStatus;
import com.bol.kalah.dto.MakeAMoveReply;
import com.bol.kalah.error.exception.WrongGameStatusException;
import com.bol.kalah.model.Game;
import com.bol.kalah.model.Pit;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.bol.kalah.dto.GameStatus.PLAYER1_TURN;
import static com.bol.kalah.dto.GameStatus.PLAYER2_TURN;
import static com.bol.kalah.util.KalahCommonUtil.HOUSE_PITS;
import static com.bol.kalah.util.KalahCommonUtil.INITIAL_STONE_ON_HOUSE;
import static com.bol.kalah.util.KalahCommonUtil.INITIAL_STONE_ON_PIT;
import static com.bol.kalah.util.KalahCommonUtil.PIT_END_INDEX;
import static com.bol.kalah.util.KalahCommonUtil.PIT_START_INDEX;
import static com.bol.kalah.util.KalahCommonUtil.PLAYER1_HOUSE;
import static com.bol.kalah.util.KalahCommonUtil.PLAYER2_HOUSE;
import static com.bol.kalah.util.KalahCommonUtil.getLastStonePit;
import static com.bol.kalah.util.KalahCommonUtil.getNumberOfStonesToPutInEachPit;
import static com.bol.kalah.util.KalahCommonUtil.getPlayerPitToNotPutStone;
import static com.bol.kalah.util.KalahCommonUtil.getPlayerTurn;

/**
 * The type Game service.
 */
@Service
@RequiredArgsConstructor
public class GameService {

    private final GameDao gameDao;
    private final KalahRuleService kalahRuleService;
    private final GameValidationService gameValidationService;

    /**
     * Create a Game initiate pits for it.
     *
     * @param url the url
     * @return the CreateGameReply including gameId
     */
    public CreateGameReply createGame(String url) {

        List<Pit> pits = new ArrayList<>();
        IntStream.range(PIT_START_INDEX, PIT_END_INDEX + 1).sequential()
                .forEach(index -> {
                    Pit pit = new Pit();
                    if (ArrayUtils.contains(HOUSE_PITS, index)) {
                        pit.setNumberOfStones(INITIAL_STONE_ON_HOUSE);
                    } else {
                        pit.setNumberOfStones(INITIAL_STONE_ON_PIT);
                    }
                    pit.setPitIndex(index);
                    pits.add(pit);
                });

        Game game = new Game(pits.toArray(Pit[]::new));
        game.setGameStatus(GameStatus.INIT);
        game = gameDao.saveGame(game);

        return CreateGameReply.builder().gameId(game.getId()).uri(url + "/" + game.getId()).build();
    }

    /**
     * move the stones in board as per the player move
     *
     * @param gameId   the game id
     * @param pitIndex the pit index
     * @param url      the url
     * @return the MakeAMoveReply including current pit status
     */
    @Transactional
    public MakeAMoveReply makeAMoveByPlayer(Long gameId, Integer pitIndex, String url) {

        Game game = gameDao.retrieveGame(gameId);
        GameStatus gameStatus = game.getGameStatus();
        if (gameStatus == GameStatus.INIT) {
            gameStatus = getPlayerTurn(pitIndex);
        }

        int initialStones = gameValidationService.validateGameInputs(pitIndex, gameStatus, gameId);
        int playerPitToNotPutStone = getPlayerPitToNotPutStone(gameStatus);
        int lastStonePit = getLastStonePit(initialStones, pitIndex);
        int numberOfStonesToPutInEachPit = getNumberOfStonesToPutInEachPit(initialStones);

        Map<Integer, Pit> stonesInBoard = kalahRuleService.moveTheStonesInBoard(
                game.getPits(), playerPitToNotPutStone, lastStonePit,
                pitIndex, numberOfStonesToPutInEachPit, initialStones);

        stonesInBoard = kalahRuleService.checkAndUpdateIfLastPitIsEmpty(stonesInBoard, lastStonePit, gameStatus);

        GameStatus nextStatus = getNextTurnOfPlayer(gameStatus, lastStonePit);
        game = updateGame(game, nextStatus, stonesInBoard);
        game = GameEndVerification(game);


        Map<Integer, Integer> resultMap = game.getPits().stream()
                .collect(Collectors.toMap(Pit::getPitIndex, Pit::getNumberOfStones));
        return MakeAMoveReply.builder().id(gameId).uri(url + "/games/" + game.getId()).status(resultMap).build();
    }

    private GameStatus getNextTurnOfPlayer(GameStatus currentGameStatus, int lastStonePit)
            throws WrongGameStatusException {

        if (currentGameStatus == PLAYER1_TURN) {
            return lastStonePit == PLAYER1_HOUSE ? PLAYER1_TURN : PLAYER2_TURN;
        } else if (currentGameStatus == PLAYER2_TURN) {
            return lastStonePit == PLAYER2_HOUSE ? PLAYER2_TURN : PLAYER1_TURN;
        }

        throw new WrongGameStatusException(currentGameStatus.toString());
    }

    private Game updateGame(Game game, GameStatus nextStatus, Map<Integer, Pit> stonesInBoard) {
        game.setGameStatus(nextStatus);
        game.setPits(new ArrayList<>(stonesInBoard.values()));
        return gameDao.saveGame(game);
    }

    private Game GameEndVerification(Game game) {
        game = kalahRuleService.checkAndUpdateIfGameIsEnd(game);
        if (game.getGameStatus() == GameStatus.FINISHED) {
            gameDao.saveGame(game);
        }
        return game;
    }

}
