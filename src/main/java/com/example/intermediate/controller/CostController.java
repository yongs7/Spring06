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

    @RequestMapping(value = "/costs", method = RequestMethod.POST)  //지출생성 api, x일 바디로 지정
    public ResponseDto<?> createCost(@RequestBody CostRequestDto requestDto,
                                     HttpServletRequest request) {
        return costService.createCost(requestDto, request);
    }

    @RequestMapping(value = "/costs/{id}", method = RequestMethod.DELETE)   //지출삭제 api
    public ResponseDto<?> deleteCost(@PathVariable Long id,
                                     HttpServletRequest request) {
        return costService.deleteCost(id, request);
    }
}
