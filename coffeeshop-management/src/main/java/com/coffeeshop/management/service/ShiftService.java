package com.coffeeshop.management.service;

import com.coffeeshop.management.dto.ShiftDTO;
import com.coffeeshop.management.exception.ResourceNotFoundException;
import java.util.List;

public interface ShiftService {
    ShiftDTO createShift(ShiftDTO shiftDTO) throws ResourceNotFoundException;
    List<ShiftDTO> getAllShifts();
    ShiftDTO getShiftById(Integer id) throws ResourceNotFoundException;
    ShiftDTO updateShift(Integer id, ShiftDTO shiftDTO) throws ResourceNotFoundException;
    void deleteShift(Integer id) throws ResourceNotFoundException;
    List<ShiftDTO> getShiftsByUserId(Integer userId) throws ResourceNotFoundException;
}