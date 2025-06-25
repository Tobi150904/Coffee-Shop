package com.coffeeshop.management.service.impl;

import com.coffeeshop.management.dto.ActivityLogDTO;
import com.coffeeshop.management.model.ActivityLog;
import com.coffeeshop.management.repository.ActivityLogRepository;
import com.coffeeshop.management.service.ActivityLogService;
import com.coffeeshop.management.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ActivityLogServiceImpl implements ActivityLogService {

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Override
    public ActivityLogDTO createActivityLog(ActivityLogDTO activityLogDTO) {
        ActivityLog activityLog = new ActivityLog();
        BeanUtils.copyProperties(activityLogDTO, activityLog);
        ActivityLog savedActivityLog = activityLogRepository.save(activityLog);
        return convertToDto(savedActivityLog);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityLogDTO> getAllActivityLogs() {
        return activityLogRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ActivityLogDTO getActivityLogById(Integer id) throws ResourceNotFoundException {
        return activityLogRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("ActivityLog not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityLogDTO> getActivityLogsByUserId(Integer userId) {
        return activityLogRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityLogDTO> getActivityLogsByEntityType(String entityType) {
        return activityLogRepository.findByEntityType(entityType).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ActivityLogDTO convertToDto(ActivityLog activityLog) {
        ActivityLogDTO activityLogDTO = new ActivityLogDTO();
        BeanUtils.copyProperties(activityLog, activityLogDTO);
        if (activityLog.getUser() != null) {
            activityLogDTO.setUserId(activityLog.getUser().getUserId());
        }
        return activityLogDTO;
    }
}
