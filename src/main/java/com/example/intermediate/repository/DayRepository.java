package com.example.intermediate.repository;


import com.example.intermediate.domain.Days;
import com.example.intermediate.domain.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DayRepository extends JpaRepository<Days, Long> {
  List<Days> findAllByTrip(Trip trip);
  void deleteAllByTrip(Trip trip);
  List<Days> findAllByTripOrderById(Trip trip);
}
