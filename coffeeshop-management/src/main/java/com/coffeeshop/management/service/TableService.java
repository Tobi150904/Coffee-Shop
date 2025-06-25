package com.coffeeshop.management.service;

import com.coffeeshop.management.dto.TableDTO;
import com.coffeeshop.management.exception.ResourceNotFoundException;
import java.util.List;

public interface TableService {
    TableDTO createTable(TableDTO tableDTO);
    List<TableDTO> getAllTables();
    TableDTO getTableById(Integer id) throws ResourceNotFoundException;
    TableDTO updateTable(Integer id, TableDTO tableDTO) throws ResourceNotFoundException;
    void deleteTable(Integer id) throws ResourceNotFoundException;
    TableDTO getTableByTableNumber(String tableNumber) throws ResourceNotFoundException;
    List<TableDTO> getTablesByStatus(String status);
}