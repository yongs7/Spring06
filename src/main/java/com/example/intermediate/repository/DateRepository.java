package com.example.intermediate.repository;


import com.example.intermediate.domain.Date;
import com.example.intermediate.domain.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DateRepository extends JpaRepository<Date, Long> {
  List<Date> findAllByTrip(Trip trip);
  void deleteAllByTrip(Trip trip);
  List<Date> findAllByTripOrderById(Trip trip);
}
