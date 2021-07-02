package com.bol.kalah.model;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.bol.kalah.dto.GameStatus;

/**
 * The type Game.
 */
@Data
@Entity
@Table(name = "game")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "pits")
public class Game implements Serializable {

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
   * The Date created.
   */
  @Column(nullable = false)
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private ZonedDateTime dateCreated = ZonedDateTime.now();

  /**
   * The Date updated.
   */
  @Column(nullable = false)
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private ZonedDateTime dateUpdated = ZonedDateTime.now();

  /**
   * The Game status.
   */
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private GameStatus gameStatus;

  /**
   * The Pits.
   */
  @Size(min = 14, max = 14)
  @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Pit> pits;

  /**
   * Update time.
   */
  @PreUpdate
  public void updateTime() {
    this.setDateUpdated(ZonedDateTime.now());
  }

  /**
   * Sets time.
   */
  @PrePersist
  public void setTime() {
    this.setDateCreated(ZonedDateTime.now());
    this.setDateUpdated(ZonedDateTime.now());
  }

  /**
   * Instantiates a new Game.
   *
   * @param pits the pits
   */
  public Game(Pit... pits) {
    this.pits = Stream.of(pits).collect(Collectors.toList());
    this.pits.forEach(pit -> pit.setGame(this));
  }

}
