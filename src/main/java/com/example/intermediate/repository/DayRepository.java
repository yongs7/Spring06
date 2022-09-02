package com.example.intermediate.repository;


import com.example.intermediate.domain.Day;
import com.example.intermediate.domain.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DayRepository extends JpaRepository<Day, Long> {
  List<Day> findAllByTrip(Trip trip);
  void deleteAllByTrip(Trip trip);
  List<Day> findAllByTripOrderById(Trip trip);
}
