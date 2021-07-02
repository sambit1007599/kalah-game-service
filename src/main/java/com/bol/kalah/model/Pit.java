package com.bol.kalah.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The type Pit.
 */
@Data
@Entity
@Table(name = "pit")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Pit implements Serializable {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The Id.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  /**
   * The Version.
   */
  @Version
  private Long version;

  /**
   * The Pit index.
   */
  @Min(0)
  @Max(14)
  private int pitIndex;

  /**
   * The Number of stones.
   */
  @Min(0)
  private int numberOfStones;

  /**
   * The Game.
   */
  @ManyToOne
  @JoinColumn(name = "game_id", nullable = false)
  private Game game;

  /**
   * Gets game.
   *
   * @return the game
   */
  @JsonIgnore
  public Game getGame() {
    return game;
  }

  /**
   * Sets game.
   *
   * @param game the game
   */
  @JsonIgnore
  public void setGame(Game game) {
    this.game = game;
  }

}
