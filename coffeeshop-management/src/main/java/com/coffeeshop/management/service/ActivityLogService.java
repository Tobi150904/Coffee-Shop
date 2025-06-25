package com.coffeeshop.management.service;

import com.coffeeshop.management.dto.ActivityLogDTO;
import com.coffeeshop.management.exception.ResourceNotFoundException;

import java.util.List;

public interface ActivityLogService {
    ActivityLogDTO createActivityLog(ActivityLogDTO activityLogDTO);
    List<ActivityLogDTO> getAllActivityLogs();
    ActivityLogDTO getActivityLogById(Integer id) throws ResourceNotFoundException;
    List<ActivityLogDTO> getActivityLogsByUserId(Integer userId);
    List<ActivityLogDTO> getActivityLogsByEntityType(String entityType);
}