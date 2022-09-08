package com.example.intermediate.service;


import com.example.intermediate.controller.request.TripRequestDto;
import com.example.intermediate.controller.response.*;
import com.example.intermediate.domain.Cost;
import com.example.intermediate.domain.Date;
import com.example.intermediate.domain.Member;
import com.example.intermediate.domain.Trip;
import com.example.intermediate.jwt.TokenProvider;
import com.example.intermediate.repository.CostRepository;
import com.example.intermediate.repository.DateRepository;
import com.example.intermediate.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TripService {

  private final TokenProvider tokenProvider;
  private final TripRepository tripRepository;
  private final DateRepository dateRepository;
  private final DateService dateService;
  private final CostRepository costRepository;

  //trip 생성 메서드
  @Transactional
  public ResponseDto<?> createTrip(TripRequestDto requestDto, HttpServletRequest request) {

    //토큰 유효성 검사, 유효하면 해당 member 가져옴 아니면 null
    Member member = isValidateAccess(request);
    if (null == member) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
              "로그인이 필요합니다.");
    }

    //dto에 담긴 정보로 Trip 생성
    Trip trip = Trip.builder()
        .title(requestDto.getTitle())
        .content(requestDto.getContent())
        .days(requestDto.getDays())
        .tripStart(requestDto.getTripStart())
        .tripEnd(requestDto.getTripEnd())
        .total(0)
        .member(member)
        .build();

    tripRepository.save(trip);

    // trip에 포함할 date 생성
    dateService.createDate(trip);
    return ResponseDto.success(
        TripResponseDto.builder()
            .id(trip.getId())
            .title(trip.getTitle())
            .content(trip.getContent())
            .tripStart(trip.getTripStart())
            .tripEnd(trip.getTripEnd())
            .days(trip.getDays())
            .total(trip.getTotal())
            .build()
    );
  }

  // 입력받은 id의 trip 상세 정보가 담긴 dto 반환 메서드
  @Transactional(readOnly = true)
  public ResponseDto<?> getTrip(Long id, HttpServletRequest request) {

    Member member = isValidateAccess(request);
    if (null == member) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
              "로그인이 필요합니다.");
    }

    //해당 id를 가진 trip이 있는지 확인. 있으면 해당 trip을 받고 없으면 null
    Trip trip = isPresentTrip(id);
    if (null == trip) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
    }

    // trip과 연결된 date들의 List
    List<Date> dateList = dateRepository.findAllByTrip(trip);
    List<DateResponseDto> dateResponseDtoList = new ArrayList<>();

    //date 정보를 담은 dto 생성
    for (Date date : dateList) {

      //date와 연결된 cost List
      List<Cost> temp = costRepository.findAllByDate(date);
      List<CostResponseDto> costList = new ArrayList<>();

      //costList에 담긴 cost의 정보를 담은 dto 생성 후 List에 add
      for(Cost cost : temp){
        costList.add(CostResponseDto.builder()
                .id(cost.getId())
                .content(cost.getContent())
                .pay(cost.getPay())
                .build()
        );
      }
      //costList와 date 정보를 담은 dto 생성 후 dateList에 add
      dateResponseDtoList.add(
              DateResponseDto.builder()
                      .id(date.getId())
                      .subTotal(date.getSubTotal())
                      .costList(costList)
                      .build()
      );
    }

    //trip 정보를 담은 dto 반환
    return ResponseDto.success(
        TripResponseDto.builder()
                .id(trip.getId())
                .title(trip.getTitle())
                .content(trip.getContent())
                .tripStart(trip.getTripStart())
                .tripEnd(trip.getTripEnd())
                .days(trip.getDays())
                .total(trip.getTotal())
                .dateList(dateResponseDtoList)
                .build()
    );
  }

  //사용자가 작성한 모든 trip의 목록 반환
  @Transactional(readOnly = true)
  public ResponseDto<?> getAllTrip(HttpServletRequest request) {

    Member member = isValidateAccess(request);
    if (null == member) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
              "로그인이 필요합니다.");
    }

    //목록에 표시할 간략한 정보만 담긴 Trip dto 를 생성하여 반환
    //해당 member가 작성한 trip의 리스트를 여행 시작을 기준으로 오름차순으로 정렬하여 반환한다.
    List<Trip> tripList = tripRepository.findAllByMemberOrderByTripStartAsc(member);
    List<TripListResponseDto> dtoList = new ArrayList<>();

    for (Trip trip:tripList) {
      dtoList.add(
              TripListResponseDto.builder()
                      .id(trip.getId())
                      .title(trip.getTitle())
                      .tripStart(trip.getTripStart())
                      .tripEnd(trip.getTripEnd())
                      .build()
      );
    }
    return ResponseDto.success(dtoList);
  }

  //trip 삭제 메서드. trip에 포함된 하위 요소들도 모두 같이 삭제된다.
  @Transactional
  public ResponseDto<?> deleteTrip(Long id, HttpServletRequest request) {

    Member member = isValidateAccess(request);
    if (null == member) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
              "로그인이 필요합니다.");
    }

    Trip trip = isPresentTrip(id);
    if (null == trip) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
    }

    //trip과 연결된 date들의 하위 요소 삭제
    dateService.deleteByDate(trip.getId());
    //trip과 연결된 date 삭제
    dateRepository.deleteAllByTrip(trip);
    //trip 삭제
    tripRepository.delete(trip);

    return ResponseDto.success("delete success");
  }

  //trip 존재 여부 확인 메서드. 해당 id의 trip이 존재하면 trip을 반환하고 없으면 null
  @Transactional(readOnly = true)
  public Trip isPresentTrip(Long id) {
    Optional<Trip> optionalTrip = tripRepository.findById(id);
    return optionalTrip.orElse(null);
  }

  //토큰 유효성 검사하는 메서드. 정상적인 접근(토큰이 들어있으며 유효함)이면 해당 member 반환, 아니면 null
  public Member isValidateAccess(HttpServletRequest request){

    //헤더에 Authorization, RefrehToken 값이 없거나 유효하지 않으면 null
    if (null == request.getHeader("RefreshToken") ||
            !tokenProvider.validateToken(request.getHeader("RefreshToken")) ||
            null == request.getHeader("Authorization")) {
      return null;
    }

    // 유효성 검사 후 해당하는 member 반환
    return tokenProvider.getMemberFromAuthentication();
  }

  //cost의 생성 및 삭제에 의한 비용 변경시 total 갱신하는 메서드
  public void update(int pay, Trip trip) {
    trip.update(pay);
  }
}
