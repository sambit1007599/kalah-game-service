package com.bol.kalah.service;

import com.bol.kalah.dao.PitDao;
import com.bol.kalah.dto.GameStatus;
import com.bol.kalah.error.exception.EmptyPitException;
import com.bol.kalah.error.exception.GameOverException;
import com.bol.kalah.error.exception.InvalidPitIndexException;
import com.bol.kalah.error.exception.WrongGameStatusException;
import com.bol.kalah.model.Pit;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

import static com.bol.kalah.dto.GameStatus.FINISHED;
import static com.bol.kalah.util.KalahCommonUtil.HOUSE_PITS;
import static com.bol.kalah.util.KalahCommonUtil.getPlayerTurn;

@Service
@RequiredArgsConstructor
public class GameValidationService {

    private final PitDao pitDao;

    public int validateGameInputs(int pitIndex, GameStatus gameStatus, Long gameId) {
        validateGameStatus(pitIndex, gameStatus);
        return isValidPit(gameId, pitIndex);
    }

    private void validateGameStatus(int pitIndex, GameStatus gameStatus) {

        if (getPlayerTurn(pitIndex) != gameStatus) {
            throw new WrongGameStatusException(gameStatus.toString());
        }

        if (gameStatus == FINISHED) {
            throw new GameOverException(gameStatus.toString());
        }
    }

    private int isValidPit(Long gameId, int pitIndex) {

        if (ArrayUtils.contains(HOUSE_PITS, pitIndex)) {
            throw new InvalidPitIndexException(String.valueOf(pitIndex));
        }

        Pit selectedPit = pitDao.retrievePitDetails(gameId, pitIndex)
                .orElseThrow(() -> new EntityNotFoundException("Pit Entity not found :" + pitIndex));

        int initialStones = selectedPit.getNumberOfStones();

        if (initialStones == 0) {
            throw new EmptyPitException(String.valueOf(pitIndex));
        }
        return initialStones;
    }
}
