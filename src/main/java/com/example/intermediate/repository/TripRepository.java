package com.example.intermediate.repository;


import com.example.intermediate.domain.Member;
import com.example.intermediate.domain.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
  List<Trip> findAllByMemberOrderByTripStartAsc(Member member);
}
