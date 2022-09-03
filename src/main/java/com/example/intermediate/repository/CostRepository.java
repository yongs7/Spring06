package com.example.intermediate.repository;

import com.example.intermediate.domain.Cost;
import com.example.intermediate.domain.Days;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CostRepository extends JpaRepository<Cost, Long> {
    List<Cost> findAllByDays(Days day);
}
