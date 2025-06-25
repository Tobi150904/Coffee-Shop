package com.coffeeshop.management.service.impl;

import com.coffeeshop.management.dto.ShiftDTO;
import com.coffeeshop.management.model.Shift;
import com.coffeeshop.management.model.User;
import com.coffeeshop.management.repository.ShiftRepository;
import com.coffeeshop.management.repository.UserRepository;
import com.coffeeshop.management.service.ShiftService;
import com.coffeeshop.management.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ShiftServiceImpl implements ShiftService {

    private final ShiftRepository shiftRepository;
    private final UserRepository userRepository;

    @Autowired
    public ShiftServiceImpl(ShiftRepository shiftRepository, 
                          UserRepository userRepository) {
        this.shiftRepository = shiftRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ShiftDTO createShift(ShiftDTO shiftDTO) throws ResourceNotFoundException {
        validateShiftDTO(shiftDTO);
        
        Shift shift = new Shift();
        BeanUtils.copyProperties(shiftDTO, shift);
        
        User user = userRepository.findById(shiftDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + shiftDTO.getUserId()));
        shift.setUser(user);
        
        Shift savedShift = shiftRepository.save(shift);
        return convertToDto(savedShift);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShiftDTO> getAllShifts() {
        return shiftRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ShiftDTO getShiftById(Integer id) throws ResourceNotFoundException {
        return shiftRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Shift not found with id: " + id));
    }

    @Override
    public ShiftDTO updateShift(Integer id, ShiftDTO shiftDTO) throws ResourceNotFoundException {
        validateShiftDTO(shiftDTO);
        
        return shiftRepository.findById(id).map(existingShift -> {
            BeanUtils.copyProperties(shiftDTO, existingShift, "shiftId");
            
            User user = userRepository.findById(shiftDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + shiftDTO.getUserId()));
            existingShift.setUser(user);
            
            Shift updatedShift = shiftRepository.save(existingShift);
            return convertToDto(updatedShift);
        }).orElseThrow(() -> new ResourceNotFoundException("Shift not found with id: " + id));
    }

    @Override
    public void deleteShift(Integer id) throws ResourceNotFoundException {
        if (!shiftRepository.existsById(id)) {
            throw new ResourceNotFoundException("Shift not found with id: " + id);
        }
        shiftRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShiftDTO> getShiftsByUserId(Integer userId) throws ResourceNotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        
        return shiftRepository.findByUser_UserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ShiftDTO convertToDto(Shift shift) {
        ShiftDTO shiftDTO = new ShiftDTO();
        BeanUtils.copyProperties(shift, shiftDTO);
        if (shift.getUser() != null) {
            shiftDTO.setUserId(shift.getUser().getUserId());
        }
        return shiftDTO;
    }

    private void validateShiftDTO(ShiftDTO shiftDTO) {
        if (shiftDTO.getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (shiftDTO.getStartTime() == null) {
            throw new IllegalArgumentException("Start time cannot be null");
        }
        if (shiftDTO.getEndTime() != null && shiftDTO.getEndTime().isBefore(shiftDTO.getStartTime())) {
            throw new IllegalArgumentException("End time cannot be before start time");
        }
    }
}