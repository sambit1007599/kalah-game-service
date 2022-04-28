package com.bol.kalah.dao;

import com.bol.kalah.model.Pit;
import com.bol.kalah.repository.PitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * The type Pit dao.
 */
@Component
@RequiredArgsConstructor
public class PitDao {

    private final PitRepository pitRepository;

    /**
     * Save pit.
     *
     * @param pit the pit
     */
    public void savePit(Pit pit) {
        pitRepository.save(pit);
    }

    /**
     * Retrieve pit details optional.
     *
     * @param gameId   the game id
     * @param pitIndex the pit index
     * @return the optional
     */
    public Optional<Pit> retrievePitDetails(Long gameId, int pitIndex) {
        return pitRepository.getPitByGameIdAndPitIndex(gameId, pitIndex);
    }
}
