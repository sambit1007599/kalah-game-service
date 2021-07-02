package com.bol.kalah.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The type Create game reply.
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateGameReply {

  /**
   * The Game id.
   */
  private Long gameId;
  /**
   * The Uri.
   */
  private String uri;
}
