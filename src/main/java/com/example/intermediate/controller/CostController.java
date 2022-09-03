package com.example.intermediate.controller;

import com.example.intermediate.controller.request.CostRequestDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.service.CostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class CostController {

    private final CostService costService;

    @RequestMapping(value = "/cost/{id}", method = RequestMethod.POST)
    public ResponseDto<?> createCost(@PathVariable Long id, @RequestBody CostRequestDto requestDto,
                                     HttpServletRequest request) {
        return costService.createCost(id, requestDto, request);
    }

    @RequestMapping(value = "/cost/{id}", method = RequestMethod.DELETE)
    public ResponseDto<?> deleteCost(@PathVariable Long id,
                                     HttpServletRequest request) {
        return costService.deleteCost(id, request);
    }
}