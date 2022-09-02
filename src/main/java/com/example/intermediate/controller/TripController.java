package com.example.intermediate.controller;

import com.example.intermediate.controller.request.TripRequestDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class TripController {

  private final TripService tripService;

  //여행 생성하기
  @RequestMapping(value = "/trip", method = RequestMethod.POST)
  public ResponseDto<?> createTrip(@RequestBody TripRequestDto requestDto,
                                   HttpServletRequest request) {
    return tripService.createTrip(requestDto, request);
  }

  //여행 상세 페이지 조회하기
  @RequestMapping(value = "/trip/{id}", method = RequestMethod.GET)
  public ResponseDto<?> getTrip(@PathVariable Long id, HttpServletRequest request) {
    return tripService.getTrip(id, request);
  }

  //여행 목록 조회하기
  @RequestMapping(value = "/trip", method = RequestMethod.GET)
  public ResponseDto<?> getAllTrips(HttpServletRequest request) {
    return tripService.getAllTrip(request);
  }

  //여행 삭제하기
  @RequestMapping(value = "/trip/{id}", method = RequestMethod.DELETE)
  public ResponseDto<?> deleteTrip(@PathVariable Long id,
                                   HttpServletRequest request) {
    return tripService.deleteTrip(id, request);
  }
}
