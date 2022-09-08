package com.example.intermediate.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CostResponseDto {
    private Long id;    //x일
    private String content; //지출내용
    private int pay;    //지출금액

}
