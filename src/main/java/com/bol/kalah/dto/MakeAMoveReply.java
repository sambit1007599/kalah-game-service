package com.bol.kalah.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The type Make a move reply.
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MakeAMoveReply {

  /**
   * The Id.
   */
  private Long id;
  /**
   * The Uri.
   */
  private String uri;
  /**
   * The Status.
   */
  private Map<Integer, Integer> status;

}
