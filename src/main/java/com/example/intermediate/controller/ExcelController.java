package com.example.intermediate.controller;


import com.example.intermediate.service.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class ExcelController {

 private final ExcelService excelService;

 @RequestMapping(value = "/excel/{id}", method = RequestMethod.GET)
 public void downloadExcel(@PathVariable Long id,
                                     HttpServletRequest request, HttpServletResponse response) throws IOException {
  excelService.downloadExcel(id, request, response);
 }
}