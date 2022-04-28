package com.bol.kalah.repository;

import com.bol.kalah.model.Pit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repository to interact with Pit Entity
 */
public interface PitRepository extends JpaRepository<Pit, Long> {

    /**
     * Get the sum of Stones in list of Pits
     *
     * @param gameId     the game id
     * @param pitIndexes the pit indexes
     * @return sum of stones in player pit
     */
    @Query("SELECT SUM(p.numberOfStones) FROM Pit p WHERE p.game.id=:gameId AND p.pitIndex IN :pitIndexes ")
    Integer getSumOfStonesInPlayerPit(@Param("gameId") Long gameId, @Param("pitIndexes") int[] pitIndexes);

    /**
     * Get a Individual pit based on game and pit Index
     *
     * @param gameId   the game id
     * @param pitIndex the pit index
     * @return pit by game id and pit index
     */
    @Query("SELECT p FROM Pit p WHERE p.game.id=:gameId AND p.pitIndex=:pitIndex")
    Optional<Pit> getPitByGameIdAndPitIndex(@Param("gameId") Long gameId, @Param("pitIndex") Integer pitIndex);

}
