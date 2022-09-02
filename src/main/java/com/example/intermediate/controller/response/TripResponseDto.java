package com.example.intermediate.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TripResponseDto {
  private Long id;
  private String title;
  private String content;
  private String tripStart;
  private String tripEnd;
  private int days;
  private Long total;
  private List<DayResponseDto> dayList = new ArrayList<>();
}
