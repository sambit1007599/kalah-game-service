package com.bol.kalah.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bol.kalah.model.Game;

/**
 * Repository to interact with Game Entity
 */
public interface GameRepository extends JpaRepository<Game, Long> {

}
