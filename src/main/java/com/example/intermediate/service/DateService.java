package com.example.intermediate.service;


import com.example.intermediate.controller.response.CostResponseDto;
import com.example.intermediate.controller.response.DateResponseDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.domain.Cost;
import com.example.intermediate.domain.Date;
import com.example.intermediate.domain.Trip;
import com.example.intermediate.repository.CostRepository;
import com.example.intermediate.repository.DateRepository;
import com.example.intermediate.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DateService {

  private final CostRepository costRepository;
  private final DateRepository dateRepository;
  private final TripRepository tripRepository;

  @Transactional
  public void createDate(Long tripId, int days) {

    Trip trip = isPresentTrip(tripId);
    for (int i = 0; i < days; i++) {
      Date date = Date.builder()
        .trip(trip)
        .subTotal(0)
        .build();
      dateRepository.save(date);
    }
  }

  @Transactional(readOnly = true)
  public ResponseDto<?> getDate(Long id) {
    Date date = isPresentDate(id);
    if (null == date) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 date id 입니다.");
    }

    List<Cost> temp = costRepository.findAllByDate(date);
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
            DateResponseDto.builder()
                    .id(date.getId())
                    .subTotal(date.getSubTotal())
                    .costList(costList)
                    .build()
    );
  }

  @Transactional(readOnly = true)
  public ResponseDto<?> getAllDate(Long tripId) {
    Trip trip = isPresentTrip(tripId);
    if(null == trip){
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 여행 id 입니다.");
    }
    List<Date> dateList = dateRepository.findAllByTripOrderById(trip);
    List<DateResponseDto> dtoList = new ArrayList<>();

    for (Date date:dateList) {
      dtoList.add(
              DateResponseDto.builder()
                      .id(date.getId())
                      .subTotal(date.getSubTotal())
                      .build());
    }
    return ResponseDto.success(dtoList);
  }

  @Transactional(readOnly = true)
  public Date isPresentDate(Long id) {
    Optional<Date> optionalDate = dateRepository.findById(id);
    return optionalDate.orElse(null);
  }

  @Transactional(readOnly = true)
  public Trip isPresentTrip(Long id) {
    Optional<Trip> optionalTrip = tripRepository.findById(id);
    return optionalTrip.orElse(null);
  }

  public void update(int pay, Long dateId){
    Date date = isPresentDate(dateId);
    date.update(pay);
  }

  public void deleteByDate(Long tripId){
    Trip trip = isPresentTrip(tripId);
    List<Date> dateList = dateRepository.findAllByTripOrderById(trip);
    for (int i = 0; i < dateList.size(); i++) {
      costRepository.deleteAllByDate(dateList.get(i));
    }
  }
}
