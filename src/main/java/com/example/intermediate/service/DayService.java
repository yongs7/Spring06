package com.example.intermediate.service;


import com.example.intermediate.controller.response.CostResponseDto;
import com.example.intermediate.controller.response.DayResponseDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.domain.Cost;
import com.example.intermediate.domain.Days;
import com.example.intermediate.domain.Trip;
import com.example.intermediate.jwt.TokenProvider;
import com.example.intermediate.repository.CostRepository;
import com.example.intermediate.repository.DayRepository;
import com.example.intermediate.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DayService {

  private final CostRepository costRepository;
  private final DayRepository dayRepository;
  private final TripRepository tripRepository;

  @Transactional
  public void createDay(Long tripId, int days) {

    Trip trip = isPresentTrip(tripId);
    for (int i = 0; i < days; i++) {
      Days day = Days.builder()
        .trip(trip)
        .subTotal(0L)
        .build();
      dayRepository.save(day);
    }
  }

  @Transactional(readOnly = true)
  public ResponseDto<?> getDay(Long id) {
    Days day = isPresentDay(id);
    if (null == day) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 day id 입니다.");
    }

    List<Cost> temp = costRepository.findAllByDays(day);
    List<CostResponseDto> costList = new ArrayList<>();

    for(Cost cost : temp){
      costList.add(CostResponseDto.builder()
              .id(cost.getId())
              .content(cost.getContent())
              .pay(cost.getPay())
              .build()
      );
    }
    return ResponseDto.success(
            DayResponseDto.builder()
                    .id(day.getId())
                    .subTotal(day.getSubTotal())
                    .costList(costList)
                    .build()
    );
  }

  @Transactional(readOnly = true)
  public ResponseDto<?> getAllDay(Long tripId) {
    Trip trip = isPresentTrip(tripId);
    if(null == trip){
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 여행 id 입니다.");
    }
    List<Days> dayList = dayRepository.findAllByTripOrderById(trip);
    List<DayResponseDto> dtoList = new ArrayList<>();

    for (Days day:dayList) {
      dtoList.add(
              DayResponseDto.builder()
                      .id(day.getId())
                      .subTotal(day.getSubTotal())
                      .build());
    }
    return ResponseDto.success(dtoList);
  }

  @Transactional(readOnly = true)
  public Days isPresentDay(Long id) {
    Optional<Days> optionalDay = dayRepository.findById(id);
    return optionalDay.orElse(null);
  }

  @Transactional(readOnly = true)
  public Trip isPresentTrip(Long id) {
    Optional<Trip> optionalTrip = tripRepository.findById(id);
    return optionalTrip.orElse(null);
  }


}
