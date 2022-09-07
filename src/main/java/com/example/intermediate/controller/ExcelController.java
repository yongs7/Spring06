package com.example.intermediate.controller;

import com.example.intermediate.controller.request.CostRequestDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.service.CostService;
import com.example.intermediate.service.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class ExcelController {

    private final ExcelService excelService;

    @RequestMapping(value = "/excel/{id}", method = RequestMethod.GET)
    public ResponseDto<?> downloadExcel(@PathVariable Long id,
                                        HttpServletRequest request, HttpServletResponse response) {
        return excelService.downloadExcel(id, request, response);
    }

}
