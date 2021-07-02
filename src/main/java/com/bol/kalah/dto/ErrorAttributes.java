package com.bol.kalah.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Error attributes.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorAttributes {

  private String code;
  private String message;
  private int status;
  private List<String> params;
}
