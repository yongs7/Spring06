package com.example.intermediate.service;

import com.example.intermediate.controller.request.CostRequestDto;
import com.example.intermediate.controller.response.CostResponseDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.controller.response.TripResponseDto;
import com.example.intermediate.domain.Cost;
import com.example.intermediate.domain.Date;
import com.example.intermediate.domain.Member;
import com.example.intermediate.domain.Trip;
import com.example.intermediate.jwt.TokenProvider;
import com.example.intermediate.repository.CostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CostService {

    private final DateService dateService;
    private final CostRepository costRepository;
    private final TokenProvider tokenProvider;
    private final TripService tripService;

    @Transactional
    public ResponseDto<?> createCost(Long dateId, CostRequestDto requestDto, HttpServletRequest request) {
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

        Date date = dateService.isPresentDate(dateId);
        if (null == date) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 date id 입니다.");
        }

        Cost cost = Cost.builder()
                .member(member)
                .date(date)
                .content(requestDto.getContent())
                .pay(requestDto.getPay())
                .build();
        costRepository.save(cost);

        int pay = cost.getPay();
        dateService.update(pay,date.getId());
       tripService.update(pay, date.getTrip().getId());

        return ResponseDto.success(CostResponseDto.builder()
                .id(cost.getId())
                .content(cost.getContent())
                .pay(cost.getPay())
                .build()
        );
    }

    @Transactional
    public ResponseDto<?> deleteCost(Long id, HttpServletRequest request) {
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
        Cost cost = isPresentCost(id);
        if (null == cost) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 지출 id 입니다.");
        }

        if (cost.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제할 수 있습니다.");
        }

        Date date = dateService.isPresentDate(cost.getDate().getId());
        if (null == date) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 date id 입니다.");
        }

        int pay = cost.getPay()*(-1);
        dateService.update(pay,date.getId());
        tripService.update(pay, date.getTrip().getId());

        costRepository.delete(cost);
        return ResponseDto.success("delete success");
    }

    @Transactional(readOnly = true)
    public Cost isPresentCost(Long id) {
        Optional<Cost> optionalCost = costRepository.findById(id);
        return optionalCost.orElse(null);
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}
