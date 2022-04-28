package com.bol.kalah.dao;

import com.bol.kalah.error.exception.InvalidGameIdException;
import com.bol.kalah.model.Game;
import com.bol.kalah.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameDao {

    private final GameRepository gameRepository;

    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }

    public Game retrieveGame(Long gameId) {
        return gameRepository.findById(gameId).orElseThrow(() -> new InvalidGameIdException(gameId.toString()));
    }
}
