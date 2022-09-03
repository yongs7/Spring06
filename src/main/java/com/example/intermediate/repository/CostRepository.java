package com.example.intermediate.repository;

import com.example.intermediate.domain.Cost;
import com.example.intermediate.domain.Date;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CostRepository extends JpaRepository<Cost, Long> {
    List<Cost> findAllByDate(Date date);
    void deleteAllByDate(Date date);
}
