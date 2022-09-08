package com.example.intermediate.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//Trip 목록 조회용 DTO
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TripListResponseDto {
  private Long id;
  private String title;
  private String tripStart;
  private String tripEnd;
}
