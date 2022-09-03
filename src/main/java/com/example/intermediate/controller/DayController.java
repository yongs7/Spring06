package com.example.intermediate.controller;


import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.service.DayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class DayController {

  private final DayService dayService;

  @RequestMapping(value = "/day/{id}", method = RequestMethod.GET)
  public ResponseDto<?> getDay(@PathVariable Long id) {
    return dayService.getDay(id);
  }

  @RequestMapping(value = "/days/{id}", method = RequestMethod.GET)
  public ResponseDto<?> getAllDay(@PathVariable Long id) {
    return dayService.getAllDay(id);
  }
}
