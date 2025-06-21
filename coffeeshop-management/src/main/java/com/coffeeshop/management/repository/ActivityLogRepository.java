package com.coffeeshop.management.repository;

import com.coffeeshop.management.model.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Integer> {
    List<ActivityLog> findByUserId(Integer userId);
    List<ActivityLog> findByEntityType(String entityType);
}