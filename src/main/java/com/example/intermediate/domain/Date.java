package com.example.intermediate.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Date  {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JoinColumn(name = "trip_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Trip trip;

  @Column(nullable = false)
  private int subTotal;

  public void update(int pay){
    this.subTotal += pay;
  }
}
