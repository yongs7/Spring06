package com.example.intermediate.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Trip {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 여행 이름(제목)
  @Column(nullable = false)
  private String title;

  // 여행 소개 내용
  @Column(nullable = false)
  private String content;

  // 여행 시작일
  @Column(nullable = false)
  private String tripStart;

  // 여행 종료일
  @Column(nullable = false)
  private String tripEnd;

  // 여행 기간
  @Column(nullable = false)
  private int days;

  // 여행 기간 모든 날짜 총 지출 비용
  @Column(nullable = false)
  private int total;

  // 여행 일자별 지출을 담고 있는 date의 리스트
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Date> dateList = new ArrayList<>();

  //여행을 작성한 member
  @JoinColumn(name = "member_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;

  // cost 생성 및 삭제에 의한 total 갱신
  public void update(int pay){
    this.total += pay;
  }

}
