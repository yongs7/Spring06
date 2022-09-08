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

  /*
  * date 생성 메서드
  * 입력받은 trip에 days 만큼의 date를 만들어 dateList를 생성한다.
  * */
  @Transactional
  public void createDate(Trip trip) {
    List<Date> dateList = new ArrayList<>();

    //해당 trip의 cost가 없는 date를 days만큼 생성하여 dateList에 더한다.
    for (int i = 0; i < trip.getDays(); i++) {
      Date date = Date.builder()
        .trip(trip)
        .subTotal(0)
        .build();
      dateList.add(date);
    }
    dateRepository.saveAll(dateList);
  }

  //id에 해당하는 date 조회
  @Transactional(readOnly = true)
  public ResponseDto<?> getDate(Long id) {
    Date date = isPresentDate(id);
    if (null == date) { //해당하는 id의 date가 반환되지 않았으면 fail 반환
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 date id 입니다.");
    }

    List<Cost> temp = costRepository.findAllByDate(date);
    List<CostResponseDto> costList = new ArrayList<>();

    //date에 담긴 전체 cost의 정보를 cost 응답 dto에 담아 costList 생성
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

  //trip id에 해당하는
  @Transactional(readOnly = true)
  public ResponseDto<?> getAllDate(Long tripId) {

    Trip trip = isPresentTrip(tripId);
    if(null == trip){ //tripId에 해당하는 trip이 없으면 fail
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 여행 id 입니다.");
    }

    //trip id에 해당하는 date를 id 순으로 담은 List
    List<Date> dateList = dateRepository.findAllByTripOrderById(trip);
    List<DateResponseDto> dtoList = new ArrayList<>();

    for (Date date:dateList) {

      List<Cost> temp = costRepository.findAllByDate(date);
      List<CostResponseDto> costList = new ArrayList<>();

      //date에 담긴 전체 cost의 정보를 cost 응답 dto에 담아 costList 생성
      for(Cost cost : temp){
        costList.add(CostResponseDto.builder()
                .id(cost.getId())
                .content(cost.getContent())
                .pay(cost.getPay())
                .build()
        );
      }

      //date 정보를 담고있고 위에서 만든 costList 담은 dto 생성 후 dateList에 add
      dtoList.add(
              DateResponseDto.builder()
                      .id(date.getId())
                      .costList(costList)
                      .subTotal(date.getSubTotal())
                      .build());
    }
    return ResponseDto.success(dtoList);
  }

  //입력받은 id에 해당하는 date 존재하면 date 반환, 없으면 null
  @Transactional(readOnly = true)
  public Date isPresentDate(Long id) {
    Optional<Date> optionalDate = dateRepository.findById(id);
    return optionalDate.orElse(null);
  }

  //입력받은 id에 해당하는 trip 존재하면 trip 반환, 없으면 null
  @Transactional(readOnly = true)
  public Trip isPresentTrip(Long id) {
    Optional<Trip> optionalTrip = tripRepository.findById(id);
    return optionalTrip.orElse(null);
  }

  //cost 추가 혹은 삭제에 의해 비용 변경시 subTotal 갱신
  public void update(int pay, Long dateId){
    Date date = isPresentDate(dateId);
    date.update(pay);
  }

  //date가 속해있는 trip 삭제시 삭제 대상이 되는 date의 cost 모두 삭제
  public void deleteByDate(Long tripId){
    Trip trip = isPresentTrip(tripId); //trip 존재 체크
    List<Date> dateList = dateRepository.findAllByTripOrderById(trip); //해당 trip과 연결된 모든 date List
    for (Date date : dateList) {
      costRepository.deleteAllByDate(date); //date의 모든 cost 삭제
    }
  }
}
