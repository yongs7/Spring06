package com.example.intermediate.repository;

import com.example.intermediate.domain.Cost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CostRepository extends JpaRepository<Cost, Long> {
}
