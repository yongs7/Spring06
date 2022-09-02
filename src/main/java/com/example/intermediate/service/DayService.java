package com.example.intermediate.service;


import com.example.intermediate.controller.response.DayResponseDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.domain.Day;
import com.example.intermediate.domain.Trip;
import com.example.intermediate.jwt.TokenProvider;
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

  private final DayRepository dayRepository;
  private final TripRepository tripRepository;

  @Transactional
  public void createDay(Long tripId, int days) {

    Trip trip = isPresentTrip(tripId);
    for (int i = 0; i < days; i++) {
      Day day = Day.builder()
        .trip(trip)
        .subTotal(0L)
        .build();
      dayRepository.save(day);
    }
  }

  @Transactional(readOnly = true)
  public ResponseDto<?> getDay(Long id) {
    Day day = isPresentDay(id);
    if (null == day) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 day id 입니다.");
    }

    return ResponseDto.success(
            DayResponseDto.builder()
                    .id(day.getId())
                    .subTotal(day.getSubTotal())
                    .build()
    );
  }

  @Transactional(readOnly = true)
  public ResponseDto<?> getAllDay(Long tripId) {
    Trip trip = isPresentTrip(tripId);
    if(null == trip){
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 여행 id 입니다.");
    }
    List<Day> dayList = dayRepository.findAllByTripOrderById(trip);
    List<DayResponseDto> dtoList = new ArrayList<>();

    for (Day day:dayList) {
      dtoList.add(
              DayResponseDto.builder()
                      .id(day.getId())
                      .subTotal(day.getSubTotal())
                      .build());
    }
    return ResponseDto.success(dtoList);
  }

  @Transactional(readOnly = true)
  public Day isPresentDay(Long id) {
    Optional<Day> optionalDay = dayRepository.findById(id);
    return optionalDay.orElse(null);
  }

  @Transactional(readOnly = true)
  public Trip isPresentTrip(Long id) {
    Optional<Trip> optionalTrip = tripRepository.findById(id);
    return optionalTrip.orElse(null);
  }


}
