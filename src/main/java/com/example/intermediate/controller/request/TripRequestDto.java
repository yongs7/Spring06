package com.example.intermediate.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TripRequestDto {
  private String title;
  private String content;
  private String tripStart;
  private String tripEnd;
  private int days;
  private Long total;
}