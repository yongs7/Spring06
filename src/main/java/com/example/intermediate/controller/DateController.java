package com.example.intermediate.controller;


import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.service.DateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class DateController {

  private final DateService dateService;

  @RequestMapping(value = "/date/{id}", method = RequestMethod.GET)
  public ResponseDto<?> getDate(@PathVariable Long id) {
    return dateService.getDate(id);
  }

  @RequestMapping(value = "/date/all/{id}", method = RequestMethod.GET)
  public ResponseDto<?> getAllDate(@PathVariable Long id) {
    return dateService.getAllDate(id);
  }
}
