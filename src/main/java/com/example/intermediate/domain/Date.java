package com.example.intermediate.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// 여행의 date.
// ex) 2박 3일의 여행 일정인 경우 1일차 date, 2일차 date, 3일차 date 총 3개의 date를 가지고 있다.
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Date  {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  //해당 date를 포함하는 trip
  @JoinColumn(name = "trip_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Trip trip;

  //해당 date의 전체 cost 비용 합
  @Column(nullable = false)
  private int subTotal;

  //cost 삭제 및 생성에 의한 비용 변경시 subtotal 갱신
  public void update(int pay){
    this.subTotal += pay;
  }
}
