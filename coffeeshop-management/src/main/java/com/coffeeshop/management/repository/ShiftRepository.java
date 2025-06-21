package com.coffeeshop.management.repository;

import com.coffeeshop.management.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Integer> {
    List<Shift> findByUser_UserId(Integer userId);
    List<Shift> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
}