package com.example.intermediate.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

//Trip 상세 조회용 DTO
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
  private int total;
  private List<DateResponseDto> dateList = new ArrayList<>();
}
