package com.example.intermediate.service;


import com.example.intermediate.controller.request.TripRequestDto;
import com.example.intermediate.controller.response.DayResponseDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.controller.response.TripListResponseDto;
import com.example.intermediate.controller.response.TripResponseDto;
import com.example.intermediate.domain.Days;
import com.example.intermediate.domain.Member;
import com.example.intermediate.domain.Trip;
import com.example.intermediate.jwt.TokenProvider;
import com.example.intermediate.repository.DayRepository;
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
  private final DayRepository dayRepository;
  private final DayService dayService;
  @Transactional
  public ResponseDto<?> createTrip(TripRequestDto requestDto, HttpServletRequest request) {
    if (null == request.getHeader("Refresh-Token")) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
              "로그인이 필요합니다.");
    }

    if (null == request.getHeader("Authorization")) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
              "로그인이 필요합니다.");
    }

    Member member = validateMember(request);
    if (null == member) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }

    Trip trip = Trip.builder()
        .title(requestDto.getTitle())
        .content(requestDto.getContent())
        .days(requestDto.getDays())
        .tripStart(requestDto.getTripStart())
        .tripEnd(requestDto.getTripEnd())
        .total(0L)
        .member(member)
        .build();
    tripRepository.save(trip);
    dayService.createDay(trip.getId(), trip.getDays());
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

  @Transactional(readOnly = true)
  public ResponseDto<?> getTrip(Long id, HttpServletRequest request) {    if (null == request.getHeader("Refresh-Token")) {
    return ResponseDto.fail("MEMBER_NOT_FOUND",
            "로그인이 필요합니다.");
  }

    if (null == request.getHeader("Authorization")) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
              "로그인이 필요합니다.");
    }

    Member member = validateMember(request);
    if (null == member) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }
    Trip trip = isPresentTrip(id);
    if (null == trip) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
    }

    List<Days> dayList = dayRepository.findAllByTrip(trip);
    List<DayResponseDto> dayResponseDtoList = new ArrayList<>();

    for (Days day : dayList) {

      dayResponseDtoList.add(
              DayResponseDto.builder()
                      .id(day.getId())
                      .subTotal(day.getSubTotal())
                      .build()
      );
    }

    return ResponseDto.success(
        TripResponseDto.builder()
                .id(trip.getId())
                .title(trip.getTitle())
                .content(trip.getContent())
                .tripStart(trip.getTripStart())
                .tripEnd(trip.getTripEnd())
                .days(trip.getDays())
                .total(trip.getTotal())
                .dayList(dayResponseDtoList)
                .build()
    );
  }

  @Transactional(readOnly = true)
  public ResponseDto<?> getAllTrip(HttpServletRequest request) {
    if (null == request.getHeader("Refresh-Token")) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
              "로그인이 필요합니다.");
    }

    if (null == request.getHeader("Authorization")) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
              "로그인이 필요합니다.");
    }

    Member member = validateMember(request);
    if (null == member) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }

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


  @Transactional
  public ResponseDto<?> deleteTrip(Long id, HttpServletRequest request) {
    if (null == request.getHeader("Refresh-Token")) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
              "로그인이 필요합니다.");
    }

    if (null == request.getHeader("Authorization")) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
              "로그인이 필요합니다.");
    }

    Member member = validateMember(request);
    if (null == member) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }
    Trip trip = isPresentTrip(id);
    if (null == trip) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
    }

    dayRepository.deleteAllByTrip(trip);
    tripRepository.delete(trip);
    return ResponseDto.success("delete success");
  }

  @Transactional(readOnly = true)
  public Trip isPresentTrip(Long id) {
    Optional<Trip> optionalTrip = tripRepository.findById(id);
    return optionalTrip.orElse(null);
  }

  @Transactional
  public Member validateMember(HttpServletRequest request) {
    if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
      return null;
    }
    return tokenProvider.getMemberFromAuthentication();
  }
}
